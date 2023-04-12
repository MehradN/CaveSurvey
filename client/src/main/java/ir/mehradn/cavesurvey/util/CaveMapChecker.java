package ir.mehradn.cavesurvey.util;

import net.minecraft.world.item.ItemStack;

public class CaveMapChecker {
    private static final String CAVE_MAP_CLIENT_TAG = "cave-survey";

    public static boolean isCaveMap(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean(CAVE_MAP_CLIENT_TAG);
    }
}
