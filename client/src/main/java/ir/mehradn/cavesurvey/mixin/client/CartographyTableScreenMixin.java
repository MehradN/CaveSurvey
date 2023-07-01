package ir.mehradn.cavesurvey.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import ir.mehradn.cavesurvey.util.CaveMapTagManager;
import ir.mehradn.cavesurvey.util.upgrades.ClientCaveMapUpgrade;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CartographyTableScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.CartographyTableMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(CartographyTableScreen.class)
public abstract class CartographyTableScreenMixin extends AbstractContainerScreen<CartographyTableMenu> {
    @Shadow @Final private static ResourceLocation BG_LOCATION;

    public CartographyTableScreenMixin(CartographyTableMenu abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
    }

    @Shadow protected abstract void renderMap(GuiGraphics guiGraphics, @Nullable Integer mapId, @Nullable MapItemSavedData mapData, int x, int y,
                                              float scale);

    @WrapOperation(method = "renderBg", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/CartographyTableScreen;renderResultingMap(Lnet/minecraft/client/gui/GuiGraphics;Ljava/lang/Integer;Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData;ZZZZ)V"))
    private void renderResultingCaveMap(CartographyTableScreen instance, GuiGraphics guiGraphics, Integer mapId, MapItemSavedData mapData,
                                        boolean hasMap, boolean hasPaper, boolean hasGlassPane, boolean isMaxSize, Operation<Void> original) {
        assert this.minecraft != null;
        ItemStack mapStack = this.menu.slots.get(0).getItem();
        ItemStack upgradeStack = this.menu.slots.get(1).getItem();
        if (!CaveMapTagManager.isClientCaveMap(mapStack)) {
            original.call(instance, guiGraphics, mapId, mapData, hasMap, hasPaper, hasGlassPane, isMaxSize);
            return;
        }

        ClientCaveMapUpgrade.MapRenderer mapRenderer = (x, y, scale) -> renderMap(guiGraphics, mapId, mapData, x, y, scale);
        for (ClientCaveMapUpgrade upgrade : ClientCaveMapUpgrade.ALL_UPGRADES) {
            if (upgradeStack.is(upgrade.item())) {
                upgrade.renderResultingMap(guiGraphics, mapStack, this.leftPos, this.topPos, mapRenderer);
                return;
            }
        }

        if (mapData == null)
            guiGraphics.blit(ClientCaveMapUpgrade.CARTOGRAPHY_TEXTURE, this.leftPos + 67, this.topPos + 13, 0, 0, 66, 66);
        else if (mapData.locked)
            ClientCaveMapUpgrade.LOCKING.renderCurrentMap(guiGraphics, mapStack, this.leftPos, this.topPos, mapRenderer);
        else
            ClientCaveMapUpgrade.IMPROVING.renderCurrentMap(guiGraphics, mapStack, this.leftPos, this.topPos, mapRenderer);
    }

    @Inject(method = "renderBg", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/MapItem;getMapId(Lnet/minecraft/world/item/ItemStack;)Ljava/lang/Integer;"))
    private void renderDisabledUpgrade(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY, CallbackInfo ci) {
        assert this.minecraft != null;
        ItemStack mapStack = this.menu.slots.get(0).getItem();
        ItemStack upgradeStack = this.menu.slots.get(1).getItem();

        boolean disabled = false;
        if (CaveMapTagManager.isClientCaveMap(mapStack)) {
            for (ClientCaveMapUpgrade upgrade : ClientCaveMapUpgrade.ALL_UPGRADES)
                if (upgradeStack.is(upgrade.item()) && !upgrade.valid(mapStack, this.minecraft.level))
                    disabled = true;
        } else {
            disabled = CaveMapTagManager.isClientCaveMap(upgradeStack);
            for (ClientCaveMapUpgrade upgrade : ClientCaveMapUpgrade.ALL_UPGRADES)
                if (!upgrade.vanilla() && upgradeStack.is(upgrade.item()))
                    disabled = true;
        }

        if (disabled)
            guiGraphics.blit(BG_LOCATION, this.leftPos + 35, this.topPos + 31, this.imageWidth + 50, 132, 28, 21);
    }
}
