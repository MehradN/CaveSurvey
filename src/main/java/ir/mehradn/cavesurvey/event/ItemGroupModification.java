package ir.mehradn.cavesurvey.event;

import ir.mehradn.cavesurvey.item.ModItems;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.impl.itemgroup.MinecraftItemGroups;
import net.minecraft.world.item.Items;

public class ItemGroupModification {
    public static void register() {
        ItemGroupEvents.modifyEntriesEvent(MinecraftItemGroups.TOOLS_ID).register(ItemGroupModification::addEmptyCaveMap);
    }

    private static void addEmptyCaveMap(FabricItemGroupEntries entries) {
        entries.addAfter(Items.MAP, ModItems.CAVE_MAP);
    }
}
