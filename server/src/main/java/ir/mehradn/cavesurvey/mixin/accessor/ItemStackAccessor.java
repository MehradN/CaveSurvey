package ir.mehradn.cavesurvey.mixin.accessor;

import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ItemStack.class)
public interface ItemStackAccessor {
    @Invoker("getHideFlags")
    int invokeGetHideFlags();

    @Invoker("shouldShowInTooltip")
    static boolean invokeShouldShowInTooltip(int hideFlags, ItemStack.TooltipPart part) {
        throw new LinkageError();
    }
}
