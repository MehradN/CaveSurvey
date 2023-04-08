package ir.mehradn.cavesurvey.util.upgrades;

import ir.mehradn.cavesurvey.item.CaveMapItem;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class CaveMapImproving implements CaveMapUpgrade {
    public Integer id() {
        return 2;
    }

    public Item item() {
        return Items.AMETHYST_SHARD;
    }

    public boolean valid(ItemStack stack, Level level) {
        return CaveMapItem.getVisionLevel(stack) < 2;
    }

    public ItemStack upgrade(ItemStack stack, RegistryAccess registryAccess) {
        int newVision = Math.min(CaveMapItem.getVisionLevel(stack) + 1, 2);
        ItemStack newStack = stack.copy();
        newStack.setCount(1);
        CaveMapItem.setVisionLevel(newStack, newVision);
        return newStack;
    }
}
