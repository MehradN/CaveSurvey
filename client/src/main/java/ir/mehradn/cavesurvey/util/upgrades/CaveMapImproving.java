package ir.mehradn.cavesurvey.util.upgrades;

import ir.mehradn.cavesurvey.util.CaveMapTagManager;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

public class CaveMapImproving implements CaveMapUpgrade {
    public Integer id() {
        return 2;
    }

    public Item item() {
        return Items.AMETHYST_SHARD;
    }

    public boolean valid(ItemStack stack, Level level) {
        MapItemSavedData data = MapItem.getSavedData(stack, level);
        return data != null && !data.locked && CaveMapTagManager.getVisionLevel(stack) < 2;
    }

    public ItemStack upgrade(ItemStack stack, RegistryAccess registryAccess) {
        int newVision = CaveMapTagManager.getVisionLevel(stack) + 1;
        ItemStack newStack = stack.copy();
        newStack.setCount(1);
        CaveMapTagManager.setVisionLevel(newStack, newVision);
        return newStack;
    }
}
