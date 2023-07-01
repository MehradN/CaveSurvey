package ir.mehradn.cavesurvey.event;

import ir.mehradn.cavesurvey.item.ModItems;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;

public class ItemGroupModification {
    public static void register() {
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(ItemGroupModification::addEmptyCaveMap);
    }

    private static void addEmptyCaveMap(FabricItemGroupEntries entries) {
        entries.addAfter(Items.MAP, ModItems.CAVE_MAP);
    }
}
