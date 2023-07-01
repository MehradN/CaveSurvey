package ir.mehradn.cavesurvey.util.upgrades;

import ir.mehradn.cavesurvey.util.CaveMapTagManager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

public interface CaveMapUpgrade {
    Integer id();

    Item item();

    boolean vanilla();

    boolean valid(ItemStack mapStack, Level level);

    ItemStack upgrade(ItemStack mapStack);

    interface Cloning extends CaveMapUpgrade {
        @Override
        default Integer id() {
            return 0;
        }

        @Override
        default Item item() {
            return Items.MAP;
        }

        @Override
        default boolean vanilla() {
            return true;
        }

        @Override
        default boolean valid(ItemStack mapStack, Level level) {
            MapItemSavedData data = MapItem.getSavedData(mapStack, level);
            return data != null && !data.isExplorationMap();
        }

        @Override
        default ItemStack upgrade(ItemStack mapStack) {
            ItemStack newStack = mapStack.copyWithCount(2);
            CaveMapTagManager.setSightLevel(mapStack, 0);
            return newStack;
        }
    }

    interface Extending extends CaveMapUpgrade {
        @Override
        default Integer id() {
            return 1;
        }

        @Override
        default Item item() {
            return Items.PAPER;
        }

        @Override
        default boolean vanilla() {
            return true;
        }

        @Override
        default boolean valid(ItemStack stack, Level level) {
            MapItemSavedData data = MapItem.getSavedData(stack, level);
            return data != null && !data.locked && !data.isExplorationMap() && data.scale < 2;
        }

        @Override
        default ItemStack upgrade(ItemStack mapStack) {
            ItemStack newStack = mapStack.copyWithCount(1);
            newStack.getOrCreateTag().putInt(MapItem.MAP_SCALE_TAG, 1);
            return newStack;
        }
    }

    interface Improving extends CaveMapUpgrade {
        @Override
        default Integer id() {
            return 2;
        }

        @Override
        default Item item() {
            return Items.AMETHYST_SHARD;
        }

        @Override
        default boolean vanilla() {
            return false;
        }

        @Override
        default boolean valid(ItemStack stack, Level level) {
            MapItemSavedData data = MapItem.getSavedData(stack, level);
            return data != null && !data.locked && CaveMapTagManager.getSightLevel(stack) < 2;
        }

        @Override
        default ItemStack upgrade(ItemStack mapStack) {
            int newVision = CaveMapTagManager.getSightLevel(mapStack) + 1;
            ItemStack newStack = mapStack.copyWithCount(1);
            CaveMapTagManager.setSightLevel(newStack, newVision);
            return newStack;
        }
    }

    interface Locking extends CaveMapUpgrade {
        @Override
        default Integer id() {
            return 3;
        }

        @Override
        default Item item() {
            return Items.GLASS_PANE;
        }

        @Override
        default boolean vanilla() {
            return true;
        }

        @Override
        default boolean valid(ItemStack stack, Level level) {
            MapItemSavedData data = MapItem.getSavedData(stack, level);
            return data != null && !data.isExplorationMap() && !data.locked;
        }

        @Override
        default ItemStack upgrade(ItemStack mapStack) {
            ItemStack newStack = mapStack.copyWithCount(1);
            newStack.getOrCreateTag().putBoolean(MapItem.MAP_LOCK_TAG, true);
            CaveMapTagManager.setSightLevel(newStack, 0);
            return newStack;
        }
    }
}
