package ir.mehradn.cavesurvey.event;

import ir.mehradn.cavesurvey.item.ModItems;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;

public class LootTableModification {
    private static final ResourceLocation ANCIENT_CITY_LOOT_TABLE_ID = new ResourceLocation("minecraft", "chests/ancient_city");

    public static void register() {
        LootTableEvents.MODIFY.register(LootTableModification::addEmptyCaveMap);
    }

    private static void addEmptyCaveMap(ResourceManager resourceManager, LootTables lootManager, ResourceLocation id,
                                        LootTable.Builder tableBuilder, LootTableSource source) {
        if (!id.equals(ANCIENT_CITY_LOOT_TABLE_ID))
            return;
        LootPool.Builder pool = LootPool.lootPool()
            .add(LootItem.lootTableItem(ModItems.CAVE_MAP).setWeight(6))
            .add(EmptyLootItem.emptyItem().setWeight(7));
        tableBuilder.withPool(pool);
    }
}
