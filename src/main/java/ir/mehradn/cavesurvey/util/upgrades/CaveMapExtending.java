package ir.mehradn.cavesurvey.util.upgrades;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

public class CaveMapExtending implements CaveMapUpgrade {
    public Integer id() {
        return 1;
    }

    public Item item() {
        return Items.PAPER;
    }

    public boolean valid(ItemStack stack, Level level) {
        MapItemSavedData data = MapItem.getSavedData(stack, level);
        return data != null && !data.locked && !data.isExplorationMap() && data.scale < 2;
    }

    public ItemStack upgrade(ItemStack stack, RegistryAccess registryAccess) {
        ItemStack newStack = stack.copy();
        newStack.setCount(1);
        newStack.getOrCreateTag().putInt(MapItem.MAP_SCALE_TAG, 1);
        return newStack;
    }
}
