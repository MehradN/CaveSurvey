package ir.mehradn.cavesurvey.util;

import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class CaveMapTagManager {
    public static final String POLYMER_ITEM_ID = "Polymer$itemId";
    public static final String CAVE_MAP_TAG = "cave-survey:cave_map";
    public static final String FILLED_CAVE_MAP_TAG = "cave-survey:filled_cave_map";
    public static final String CAVE_MAP_VISION_TAG = "vision";
    public static final String LORE_TAG = "cave-survey$lore";

    public static boolean getClientCaveMap(ItemStack stack) {
        if (stack.getTag() == null)
            return false;
        return (stack.is(Items.MAP) && stack.getTag().getString(POLYMER_ITEM_ID).equals(CAVE_MAP_TAG) ||
            stack.is(Items.FILLED_MAP) && stack.getTag().getString(POLYMER_ITEM_ID).equals(FILLED_CAVE_MAP_TAG));
    }

    public static int getVisionLevel(ItemStack stack) {
        if (stack.getTag() == null)
            return 0;
        return Mth.clamp(stack.getTag().getInt(CAVE_MAP_VISION_TAG), 0, 2);
    }

    public static void setVisionLevel(ItemStack stack, int visionLevel) {
        stack.getOrCreateTag().putInt(CAVE_MAP_VISION_TAG, Mth.clamp(visionLevel, 0, 2));
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
