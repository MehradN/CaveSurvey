package ir.mehradn.cavesurvey.item.crafting;

import ir.mehradn.cavesurvey.item.ModItems;
import ir.mehradn.cavesurvey.util.upgrades.CaveMapUpgrade;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class CaveMapUpgradeRecipe<T extends CaveMapUpgrade> extends ShapedRecipe {
    public static final HashMap<Integer, RecipeSerializer<?>> serializers = new HashMap<>();
    private final T upgrade;

    public CaveMapUpgradeRecipe(ResourceLocation resourceLocation, CraftingBookCategory craftingBookCategory, T upgrade) {
        super(resourceLocation, "", craftingBookCategory, 3, 3,
            NonNullList.of(Ingredient.EMPTY,
                Ingredient.of(upgrade.item()), Ingredient.of(upgrade.item()), Ingredient.of(upgrade.item()),
                Ingredient.of(upgrade.item()), Ingredient.of(ModItems.FILLED_CAVE_MAP), Ingredient.of(upgrade.item()),
                Ingredient.of(upgrade.item()), Ingredient.of(upgrade.item()), Ingredient.of(upgrade.item())),
            new ItemStack(ModItems.CAVE_MAP));
        this.upgrade = upgrade;
    }

    public boolean matches(CraftingContainer inv, Level level) {
        if (!super.matches(inv, level))
            return false;

        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.is(ModItems.FILLED_CAVE_MAP))
                return this.upgrade.valid(stack, level);
        }
        return false;
    }

    public @NotNull ItemStack assemble(CraftingContainer inv, RegistryAccess registryAccess) {
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.is(ModItems.FILLED_CAVE_MAP))
                return this.upgrade.upgrade(stack, registryAccess);
        }
        return ItemStack.EMPTY;
    }

    public boolean isSpecial() {
        return true;
    }

    public @NotNull RecipeSerializer<?> getSerializer() {
        return serializers.get(this.upgrade.id());
    }
}
