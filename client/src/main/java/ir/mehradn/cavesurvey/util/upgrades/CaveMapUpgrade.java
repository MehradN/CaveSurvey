package ir.mehradn.cavesurvey.util.upgrades;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public interface CaveMapUpgrade {
    static List<CaveMapUpgrade> ALL_UPGRADES = List.of(
        new CaveMapCloning(),
        new CaveMapExtending(),
        new CaveMapImproving(),
        new CaveMapLocking()
    );

    Integer id();

    Item item();

    boolean valid(ItemStack mapStack, Level level);

    ItemStack upgrade(ItemStack stack, RegistryAccess registryAccess);
}
