package ir.mehradn.cavesurvey.util;

import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

public class CaveMapTagManager {
    public static final String CLIENT_CAVE_MAP_TAG = "cave-survey:cave";
    public static final String CAVE_MAP_VISION_TAG = "vision";

    public static boolean getClientCaveMap(ItemStack stack) {
        return stack.getTag() != null && stack.getTag().getBoolean(CLIENT_CAVE_MAP_TAG);
    }

    public static void setClientCaveMap(ItemStack stack) {
        stack.getOrCreateTag().putBoolean(CLIENT_CAVE_MAP_TAG, true);
    }

    public static int getVisionLevel(ItemStack stack) {
        if (stack.getTag() == null)
            return 0;
        return Mth.clamp(stack.getTag().getInt(CAVE_MAP_VISION_TAG), 0, 2);
    }

    public static void setVisionLevel(ItemStack stack, int visionLevel) {
        stack.getOrCreateTag().putInt(CAVE_MAP_VISION_TAG, Mth.clamp(visionLevel, 0, 2));
    }
}
