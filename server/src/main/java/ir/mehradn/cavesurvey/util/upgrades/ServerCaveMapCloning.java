package ir.mehradn.cavesurvey.util.upgrades;

import ir.mehradn.cavesurvey.item.ModItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ServerCaveMapCloning extends CaveMapCloning implements ServerCaveMapUpgrade {
    public boolean acceptsItem(ItemStack stack) {
        return stack.is(Items.MAP) || stack.is(ModItems.CAVE_MAP);
    }
}
