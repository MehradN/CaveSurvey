package ir.mehradn.cavesurvey.event;

import ir.mehradn.cavesurvey.item.ModItems;
import ir.mehradn.cavesurvey.item.trade.CustomItemsAndEmeraldsToItems;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Items;

import java.util.List;

public class VillagerTradeModification {
    public static void register() {
        TradeOfferHelper.registerVillagerOffers(VillagerProfession.CARTOGRAPHER, 5, VillagerTradeModification::addCaveMapTrade);
    }

    public static void addCaveMapTrade(List<VillagerTrades.ItemListing> trades) {
        trades.add(new CustomItemsAndEmeraldsToItems(Items.ECHO_SHARD, 1, 57, ModItems.CAVE_MAP, 3, 3, 30, 0.2f));
    }
}
