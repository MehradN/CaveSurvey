package ir.mehradn.cavesurvey.item;

import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import ir.mehradn.cavesurvey.CaveSurvey;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class ModItems {
    public static final Item CAVE_MAP = new EmptyCaveMapItem(new Item.Properties().rarity(Rarity.UNCOMMON));
    public static final Item FILLED_CAVE_MAP = new CaveMapItem(new Item.Properties());

    public static void register() {
        registerItem("cave_map", CAVE_MAP);
        registerItem("filled_cave_map", FILLED_CAVE_MAP);
    }

    public static void registerAssets() {
        PolymerResourcePackUtils.addModAssets(CaveSurvey.MOD_ID);
    }

    private static void registerItem(String name, Item item) {
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(CaveSurvey.MOD_ID, name), item);
    }
}
