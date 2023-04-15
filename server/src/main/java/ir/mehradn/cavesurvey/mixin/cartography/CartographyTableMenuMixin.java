package ir.mehradn.cavesurvey.mixin.cartography;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import ir.mehradn.cavesurvey.item.ModItems;
import ir.mehradn.cavesurvey.util.upgrades.ServerCaveMapUpgrade;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CartographyTableMenu.class)
public abstract class CartographyTableMenuMixin extends AbstractContainerMenu {
    @Shadow @Final private ContainerLevelAccess access;
    @Shadow @Final private ResultContainer resultContainer;

    protected CartographyTableMenuMixin(@Nullable MenuType<?> menuType, int i) {
        super(menuType, i);
    }

    @WrapOperation(method = "slotsChanged", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/CartographyTableMenu;setupResultSlot(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)V"))
    private void checkMapType(CartographyTableMenu instance, ItemStack map, ItemStack upgradeItem, ItemStack output, Operation<Void> original) {
        if (map.is(ModItems.FILLED_CAVE_MAP))
            setupResultSlotForCaveMaps(map, upgradeItem, output);
        else
            original.call(instance, map, upgradeItem, output);
    }

    @WrapOperation(method = "quickMoveStack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"))
    private boolean supportQuickMove(ItemStack instance, Item item, Operation<Boolean> original) {
        if (item == Items.FILLED_MAP && instance.is(ModItems.FILLED_CAVE_MAP))
            return true;
        if (item == Items.PAPER)
            for (ServerCaveMapUpgrade upgrade : ServerCaveMapUpgrade.ALL_UPGRADES)
                if (upgrade.acceptsItem(instance))
                    return true;
        return original.call(instance, item);
    }

    private void setupResultSlotForCaveMaps(ItemStack map, ItemStack upgradeItem, ItemStack output) {
        this.access.execute(((level, blockPos) -> {
            ItemStack newStack = null;
            for (ServerCaveMapUpgrade upgrade : ServerCaveMapUpgrade.ALL_UPGRADES) {
                if (upgrade.acceptsItem(upgradeItem) && upgrade.valid(map, level)) {
                    newStack = upgrade.upgrade(map);
                    this.broadcastChanges();
                    break;
                }
            }

            if (newStack == null) {
                this.resultContainer.removeItemNoUpdate(2);
                this.broadcastChanges();
                return;
            }
            if (!ItemStack.matches(output, newStack)) {
                this.resultContainer.setItem(2, newStack);
                this.broadcastChanges();
            }
        }));
    }
}
