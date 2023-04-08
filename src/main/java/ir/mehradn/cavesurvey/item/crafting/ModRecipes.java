package ir.mehradn.cavesurvey.item.crafting;

import ir.mehradn.cavesurvey.CaveSurvey;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;

public class ModRecipes {
    public static final RecipeSerializer<CaveMapCloningRecipe> CAVE_MAP_CLONING = new SimpleCraftingRecipeSerializer<>(CaveMapCloningRecipe::new);

    public static void register() {
        registerRecipe("crafting_special_cavemapcloning", CAVE_MAP_CLONING);
    }

    private static <T extends CraftingRecipe> void registerRecipe(String name, RecipeSerializer<T> recipe) {
        Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, new ResourceLocation(CaveSurvey.MOD_ID, name), recipe);
    }
}
