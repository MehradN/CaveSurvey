package ir.mehradn.cavesurvey.util.upgrades;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface CaveMapUpgrade {
    Integer id();

    Item item();

    default boolean acceptsItem(ItemStack stack) {
        return stack.is(item());
    }

    boolean valid(ItemStack stack, Level level);

    ItemStack upgrade(ItemStack stack, RegistryAccess registryAccess);
}
