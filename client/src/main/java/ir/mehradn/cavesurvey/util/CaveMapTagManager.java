package ir.mehradn.cavesurvey.util;

import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class CaveMapTagManager {
    public static final String POLYMER_ITEM_ID = "Polymer$itemId";
    public static final String CAVE_MAP_ID = "cave-survey:cave_map";
    public static final String FILLED_CAVE_MAP_ID = "cave-survey:filled_cave_map";
    public static final String SIGHT_TAG = "sight";
    public static final String LORE_TAG = "cave-survey$lore";

    public static boolean getClientCaveMap(ItemStack stack) {
        if (stack.getTag() == null)
            return false;
        return (stack.is(Items.MAP) && stack.getTag().getString(POLYMER_ITEM_ID).equals(CAVE_MAP_ID) ||
            stack.is(Items.FILLED_MAP) && stack.getTag().getString(POLYMER_ITEM_ID).equals(FILLED_CAVE_MAP_ID));
    }

    public static int getSightLevel(ItemStack stack) {
        if (stack.getTag() == null)
            return 0;
        return Mth.clamp(stack.getTag().getByte(SIGHT_TAG), 0, 2);
    }

    public static void setSightLevel(ItemStack stack, int sightLevel) {
        stack.getOrCreateTag().putByte(SIGHT_TAG, (byte)Mth.clamp(sightLevel, 0, 2));
    }

    public static void setLore(ItemStack stack, int lore) {
        stack.getOrCreateTag().putByte(LORE_TAG, (byte)(lore + 1));
    }

    public static int getLore(ItemStack stack) {
        if (stack.getTag() == null)
            return 0;
        return (int)stack.getTag().getByte(LORE_TAG) - 1;
    }
}
