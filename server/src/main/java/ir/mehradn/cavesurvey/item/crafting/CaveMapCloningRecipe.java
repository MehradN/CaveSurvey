package ir.mehradn.cavesurvey.item.crafting;

import eu.pb4.polymer.core.api.item.PolymerRecipe;
import ir.mehradn.cavesurvey.item.ModItems;
import ir.mehradn.cavesurvey.util.upgrades.ServerCaveMapCloning;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class CaveMapCloningRecipe extends CustomRecipe implements PolymerRecipe {
    public static final ServerCaveMapCloning upgrade = new ServerCaveMapCloning();

    public CaveMapCloningRecipe(ResourceLocation resourceLocation, CraftingBookCategory craftingBookCategory) {
        super(resourceLocation, craftingBookCategory);
    }

    public boolean matches(CraftingContainer inv, Level level) {
        ItemStack filled = null;
        int empty = 0;
        int other = 0;
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.is(ModItems.FILLED_CAVE_MAP)) {
                if (filled != null)
                    return false;
                filled = stack;
            } else if (upgrade.acceptsItem(stack)) {
                empty++;
            } else if (!stack.isEmpty()) {
                other++;
            }
        }
        return filled != null && empty > 0 && other == 0 && upgrade.valid(filled, level);
    }

    public @NotNull ItemStack assemble(CraftingContainer inv, RegistryAccess registryAccess) {
        ItemStack filled = null;
        int empty = 0;
        int other = 0;
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.is(ModItems.FILLED_CAVE_MAP)) {
                if (filled != null)
                    return ItemStack.EMPTY;
                filled = stack;
            } else if (upgrade.acceptsItem(stack)) {
                empty++;
            } else if (!stack.isEmpty()) {
                other++;
            }
        }

        if (filled == null || empty == 0 && other > 0)
            return ItemStack.EMPTY;
        ItemStack cloned = upgrade.upgrade(filled, registryAccess);
        cloned.setCount(empty + 1);
        return cloned;
    }

    public boolean canCraftInDimensions(int width, int height) {
        return width >= 1 && height >= 1 && width * height >= 2;
    }

    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModRecipes.CAVE_MAP_CLONING;
    }
}
