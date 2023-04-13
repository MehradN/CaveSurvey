package ir.mehradn.cavesurvey.util.upgrades;

import ir.mehradn.cavesurvey.util.CaveMapTagManager;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

public class CaveMapCloning implements CaveMapUpgrade {
    public Integer id() {
        return 0;
    }

    public Item item() {
        return Items.MAP;
    }

    public boolean valid(ItemStack mapStack, Level level) {
        MapItemSavedData data = MapItem.getSavedData(mapStack, level);
        return data != null && !data.isExplorationMap();
    }

    public ItemStack upgrade(ItemStack stack, RegistryAccess registryAccess) {
        ItemStack newStack = stack.copy();
        newStack.setCount(2);
        CaveMapTagManager.setVisionLevel(stack, 0);
        return newStack;
    }
}
