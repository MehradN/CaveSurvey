package ir.mehradn.cavesurvey.mixin.cartography;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import ir.mehradn.cavesurvey.item.crafting.ModRecipes;
import ir.mehradn.cavesurvey.util.upgrades.CaveMapUpgrade;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net.minecraft.world.inventory.CartographyTableMenu$4")
public abstract class CartographyTableMenu$4Mixin extends Slot {
    public CartographyTableMenu$4Mixin(Container container, int i, int j, int k) {
        super(container, i, j, k);
    }

    @ModifyReturnValue(method = "mayPlace", at = @At("RETURN"))
    private boolean acceptCaveMapUpgrades(boolean original, ItemStack stack) {
        for (CaveMapUpgrade upgrade : ModRecipes.CARTOGRAPHY_UPGRADES)
            original = (original || upgrade.acceptsItem(stack));
        return original;
    }
}
