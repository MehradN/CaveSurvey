package ir.mehradn.cavesurvey.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import ir.mehradn.cavesurvey.CaveSurveyClient;
import ir.mehradn.cavesurvey.util.CaveMapTagManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {
    private static final RenderType CAVE_MAP_BACKGROUND = RenderType.text(
        new ResourceLocation(CaveSurveyClient.MOD_ID, "textures/map/cave_map_background.png"));
    private static final RenderType CAVE_MAP_BACKGROUND_CHECKERBOARD = RenderType.text(
        new ResourceLocation(CaveSurveyClient.MOD_ID, "textures/map/cave_map_background_checkerboard.png"));

    @Shadow @Final private Minecraft minecraft;

    @WrapOperation(method = "renderMap", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/MultiBufferSource;getBuffer(Lnet/minecraft/client/renderer/RenderType;)Lcom/mojang/blaze3d/vertex/VertexConsumer;"))
    private VertexConsumer useCaveMapTextures(MultiBufferSource instance, RenderType texture, Operation<VertexConsumer> original,
                                              PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, ItemStack stack) {
        if (!CaveMapTagManager.getClientCaveMap(stack))
            return original.call(instance, texture);
        MapItemSavedData data = MapItem.getSavedData(stack, this.minecraft.level);
        return buffer.getBuffer(data == null ? CAVE_MAP_BACKGROUND : CAVE_MAP_BACKGROUND_CHECKERBOARD);
    }
}
