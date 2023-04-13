package ir.mehradn.cavesurvey.item;

import ir.mehradn.cavesurvey.CaveSurveyClient;
import ir.mehradn.cavesurvey.util.CaveMapTagManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

@Environment(EnvType.CLIENT)
public class ModItemProperties {
    public static final ResourceLocation CLIENT_CAVE_MAP_PROPERTY = new ResourceLocation(CaveSurveyClient.MOD_ID, "cave");

    public static void register() {
        ItemProperties.register(Items.MAP, CLIENT_CAVE_MAP_PROPERTY, ModItemProperties::clientCaveMapProperty);
        ItemProperties.register(Items.FILLED_MAP, CLIENT_CAVE_MAP_PROPERTY, ModItemProperties::clientCaveMapProperty);
    }

    private static float clientCaveMapProperty(ItemStack stack, ClientLevel level, LivingEntity entity, int i) {
        return (CaveMapTagManager.getClientCaveMap(stack) ? 1 : 0);
    }
}
