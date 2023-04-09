package ir.mehradn.cavesurvey.util.upgrades;

import ir.mehradn.cavesurvey.item.CaveMapItem;
import ir.mehradn.cavesurvey.item.ModItems;
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
        return ModItems.CAVE_MAP;
    }

    public boolean acceptsItem(ItemStack stack) {
        return stack.is(Items.MAP) || stack.is(ModItems.CAVE_MAP);
    }

    public boolean valid(ItemStack stack, Level level) {
        MapItemSavedData data = MapItem.getSavedData(stack, level);
        return data != null && !data.isExplorationMap();
    }

    public ItemStack upgrade(ItemStack stack, RegistryAccess registryAccess) {
        ItemStack newStack = stack.copy();
        newStack.setCount(2);
        CaveMapItem.setVisionLevel(stack, 0);
        return newStack;
    }
}
