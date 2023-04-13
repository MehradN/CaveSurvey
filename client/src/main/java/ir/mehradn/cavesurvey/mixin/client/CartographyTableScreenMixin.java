package ir.mehradn.cavesurvey.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import ir.mehradn.cavesurvey.util.CaveMapTagManager;
import ir.mehradn.cavesurvey.util.upgrades.ClientCaveMapUpgrade;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CartographyTableScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.CartographyTableMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(CartographyTableScreen.class)
public abstract class CartographyTableScreenMixin extends AbstractContainerScreen<CartographyTableMenu> {
    public CartographyTableScreenMixin(CartographyTableMenu abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
    }

    @Shadow protected abstract void renderMap(PoseStack poseStack, @Nullable Integer mapId, @Nullable MapItemSavedData mapData, int x, int y, float scale);

    @WrapOperation(method = "renderBg", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/CartographyTableScreen;renderResultingMap(Lcom/mojang/blaze3d/vertex/PoseStack;Ljava/lang/Integer;Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData;ZZZZ)V"))
    private void renderResultingCaveMap(CartographyTableScreen instance, PoseStack poseStack, Integer mapId, MapItemSavedData mapData,
                                        boolean hasMap, boolean hasPaper, boolean hasGlassPane, boolean isMaxSize, Operation<Void> original) {
        assert this.minecraft != null;
        ItemStack mapStack = this.menu.slots.get(0).getItem();
        ItemStack upgradeStack = this.menu.slots.get(1).getItem();
        if (!CaveMapTagManager.getClientCaveMap(mapStack)) {
            original.call(instance, poseStack, mapId, mapData, hasMap, hasPaper, hasGlassPane, isMaxSize);
            return;
        }

        ClientCaveMapUpgrade.MapRenderer mapRenderer = (x, y, scale) -> renderMap(poseStack, mapId, mapData, x, y, scale);
        for (ClientCaveMapUpgrade upgrade : ClientCaveMapUpgrade.ALL_UPGRADES) {
            if (upgradeStack.is(upgrade.item())) {
                upgrade.renderResultingMap(poseStack, mapStack, this.leftPos, this.topPos, mapRenderer);
                return;
            }
        }

        if (mapData == null) {
            RenderSystem.setShaderTexture(0, ClientCaveMapUpgrade.CARTOGRAPHY_TEXTURE);
            CartographyTableScreen.blit(poseStack, this.leftPos + 67, this.topPos + 13, 0, 0, 66, 66);
        } else if (mapData.locked) {
            ClientCaveMapUpgrade.LOCKING.renderCurrentMap(poseStack, mapStack, this.leftPos, this.topPos, mapRenderer);
        } else {
            ClientCaveMapUpgrade.IMPROVING.renderCurrentMap(poseStack, mapStack, this.leftPos, this.topPos, mapRenderer);
        }
    }

    @Inject(method = "renderBg", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/MapItem;getMapId(Lnet/minecraft/world/item/ItemStack;)Ljava/lang/Integer;"))
    private void renderDisabledUpgrade(PoseStack poseStack, float partialTick, int mouseX, int mouseY, CallbackInfo ci) {
        assert this.minecraft != null;
        ItemStack mapStack = this.menu.slots.get(0).getItem();
        ItemStack upgradeStack = this.menu.slots.get(1).getItem();

        boolean disabled = false;
        if (CaveMapTagManager.getClientCaveMap(mapStack)) {
            for (ClientCaveMapUpgrade upgrade : ClientCaveMapUpgrade.ALL_UPGRADES)
                if (upgradeStack.is(upgrade.item()) && !upgrade.valid(mapStack, this.minecraft.level))
                    disabled = true;
        } else {
            disabled = CaveMapTagManager.getClientCaveMap(upgradeStack);
            for (ClientCaveMapUpgrade upgrade : ClientCaveMapUpgrade.ALL_UPGRADES)
                if (!upgrade.vanilla() && upgradeStack.is(upgrade.item()))
                    disabled = true;
        }

        if (disabled)
            CartographyTableScreen.blit(poseStack, this.leftPos + 35, this.topPos + 31,
                this.imageWidth + 50, 132, 28, 21);
    }
}
