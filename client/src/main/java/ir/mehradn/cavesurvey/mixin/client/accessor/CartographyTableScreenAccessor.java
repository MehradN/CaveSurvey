package ir.mehradn.cavesurvey.mixin.client.accessor;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.inventory.CartographyTableScreen;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Environment(value= EnvType.CLIENT)
@Mixin(CartographyTableScreen.class)
public interface CartographyTableScreenAccessor {
    @Accessor("BG_LOCATION")
    static ResourceLocation getBgLocation() {
        throw new LinkageError();
    }
}
