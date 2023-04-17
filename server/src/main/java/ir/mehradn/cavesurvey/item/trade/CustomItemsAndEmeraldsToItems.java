package ir.mehradn.cavesurvey.item.trade;

import ir.mehradn.cavesurvey.mixin.accessor.MerchantOfferAccessor;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.trading.MerchantOffer;

public class CustomItemsAndEmeraldsToItems extends VillagerTrades.ItemsAndEmeraldsToItems {
    private final float priceMultiplier;
    public CustomItemsAndEmeraldsToItems(Item fromItem, int fromCount, int emeraldCost, Item toItem, int toCount,
                                         int maxUses, int villagerXp, float priceMultiplier) {
        super(fromItem, fromCount, emeraldCost, toItem, toCount, maxUses, villagerXp);
        this.priceMultiplier = priceMultiplier;
    }

    public MerchantOffer getOffer(Entity trader, RandomSource random) {
        MerchantOffer offer = super.getOffer(trader, random);
        if (offer != null)
            ((MerchantOfferAccessor)offer).setPriceMultiplier(this.priceMultiplier);
        return offer;
    }
}
