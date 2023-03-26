package ir.mehradn.cavesurvey.item;

import ir.mehradn.cavesurvey.CaveSurvey;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class ModItems {
    public static final Item CAVE_MAP = new EmptyCaveMapItem(new Item.Properties());

    public static void register() {
        registerItem("cave_map", CAVE_MAP);
    }

    private static void registerItem(String name, Item item) {
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(CaveSurvey.MOD_ID, name), item);
    }
}
