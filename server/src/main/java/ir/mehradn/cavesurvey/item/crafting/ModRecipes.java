package ir.mehradn.cavesurvey.item.crafting;

import ir.mehradn.cavesurvey.CaveSurvey;
import ir.mehradn.cavesurvey.util.upgrades.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;

public class ModRecipes {
    public static final RecipeSerializer<CaveMapCloningRecipe> CAVE_MAP_CLONING = new SimpleCraftingRecipeSerializer<>(CaveMapCloningRecipe::new);
    public static final RecipeSerializer<CaveMapUpgradeRecipe<ServerCaveMapUpgrade.Extending>> CAVE_MAP_EXTENDING =
        createUpgradeRecipe(ServerCaveMapUpgrade.EXTENDING);
    public static final RecipeSerializer<CaveMapUpgradeRecipe<ServerCaveMapUpgrade.Improving>> CAVE_MAP_IMPROVING =
        createUpgradeRecipe(ServerCaveMapUpgrade.IMPROVING);
    public static final RecipeSerializer<CaveMapUpgradeRecipe<ServerCaveMapUpgrade.Locking>> CAVE_MAP_LOCKING =
        createUpgradeRecipe(ServerCaveMapUpgrade.LOCKING);

    public static void register() {
        registerRecipe("crafting_special_cavemapcloning", CAVE_MAP_CLONING);
        registerRecipe("crafting_special_cavemapextending", CAVE_MAP_EXTENDING);
        registerRecipe("crafting_special_cavemapimproving", CAVE_MAP_IMPROVING);
        registerRecipe("crafting_special_cavemaplocking", CAVE_MAP_LOCKING);
    }

    private static <T extends ServerCaveMapUpgrade> RecipeSerializer<CaveMapUpgradeRecipe<T>> createUpgradeRecipe(T upgrade) {
        RecipeSerializer<CaveMapUpgradeRecipe<T>> serializer = new SimpleCraftingRecipeSerializer<>(
            (resourceLocation, craftingBookCategory) -> new CaveMapUpgradeRecipe<>(resourceLocation, craftingBookCategory, upgrade));
        CaveMapUpgradeRecipe.serializers.put(upgrade.id(), serializer);
        return serializer;
    }

    private static <T extends CraftingRecipe> void registerRecipe(String name, RecipeSerializer<T> recipe) {
        Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, new ResourceLocation(CaveSurvey.MOD_ID, name), recipe);
    }
}
