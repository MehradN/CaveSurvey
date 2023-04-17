package ir.mehradn.cavesurvey.mixin.accessor;

import net.minecraft.world.item.trading.MerchantOffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MerchantOffer.class)
public interface MerchantOfferAccessor {
    @Accessor("priceMultiplier")
    void setPriceMultiplier(float priceMultiplier);
}
