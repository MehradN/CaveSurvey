package ir.mehradn.cavesurvey.util.upgrades;

import ir.mehradn.cavesurvey.item.ModItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;

public interface ServerCaveMapUpgrade extends CaveMapUpgrade {
    Cloning CLONING = new Cloning();
    Extending EXTENDING = new Extending();
    Improving IMPROVING = new Improving();
    Locking LOCKING = new Locking();
    List<ServerCaveMapUpgrade> ALL_UPGRADES = List.of(CLONING, EXTENDING, IMPROVING, LOCKING);

    default boolean acceptsItem(ItemStack stack) {
        return stack.is(item());
    }

    class Cloning implements CaveMapUpgrade.Cloning, ServerCaveMapUpgrade {
        public boolean acceptsItem(ItemStack stack) {
            return stack.is(Items.MAP) || stack.is(ModItems.CAVE_MAP);
        }
    }

    class Extending implements CaveMapUpgrade.Extending, ServerCaveMapUpgrade { }

    class Improving implements CaveMapUpgrade.Improving, ServerCaveMapUpgrade { }

    class Locking implements CaveMapUpgrade.Locking, ServerCaveMapUpgrade { }
}
