package ir.mehradn.cavesurvey.mixin.cartpgraphy;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import ir.mehradn.cavesurvey.item.ModItems;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net.minecraft.world.inventory.CartographyTableMenu$3")
public abstract class CartographyTableMenu$3Mixin extends Slot {
    public CartographyTableMenu$3Mixin(Container container, int i, int j, int k) {
        super(container, i, j, k);
    }

    @ModifyReturnValue(method = "mayPlace", at = @At("RETURN"))
    private boolean acceptCaveMap(boolean original, ItemStack stack) {
        return original || stack.is(ModItems.FILLED_CAVE_MAP);
    }
}
