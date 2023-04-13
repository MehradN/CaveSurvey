package ir.mehradn.cavesurvey.util.upgrades;

import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface ServerCaveMapUpgrade extends CaveMapUpgrade {
    static List<ServerCaveMapUpgrade> ALL_UPGRADES = List.of(
        new ServerCaveMapCloning(),
        new ServerCaveMapExtending(),
        new ServerCaveMapImproving(),
        new ServerCaveMapLocking()
    );

    default boolean acceptsItem(ItemStack stack) {
        return stack.is(item());
    }
}
