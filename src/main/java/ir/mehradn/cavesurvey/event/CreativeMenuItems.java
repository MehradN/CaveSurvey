package ir.mehradn.cavesurvey.event;

import ir.mehradn.cavesurvey.item.ModItems;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.impl.itemgroup.MinecraftItemGroups;
import net.minecraft.world.item.Items;

public class CreativeMenuItems {
    public static void register() {
        ItemGroupEvents.modifyEntriesEvent(MinecraftItemGroups.TOOLS_ID).register(CreativeMenuItems::addTools);
    }

    private static void addTools(FabricItemGroupEntries entries) {
        entries.addAfter(Items.MAP, ModItems.CAVE_MAP); // TODO: FOR SOME REASON DOESN'T WORK
    }
}
