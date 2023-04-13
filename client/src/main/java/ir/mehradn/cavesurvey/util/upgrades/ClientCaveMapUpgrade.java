package ir.mehradn.cavesurvey.util.upgrades;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import ir.mehradn.cavesurvey.CaveSurveyClient;
import ir.mehradn.cavesurvey.mixin.client.accessor.CartographyTableScreenAccessor;
import ir.mehradn.cavesurvey.util.CaveMapTagManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.inventory.CartographyTableScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

@Environment(EnvType.CLIENT)
public interface ClientCaveMapUpgrade extends CaveMapUpgrade {
    ResourceLocation CARTOGRAPHY_TEXTURE =
        new ResourceLocation(CaveSurveyClient.MOD_ID, "textures/gui/container/cave_cartography_table.png");

    Cloning CLONING = new Cloning();
    Extending EXTENDING = new Extending();
    Improving IMPROVING = new Improving();
    Locking LOCKING = new Locking();
    List<ClientCaveMapUpgrade> ALL_UPGRADES = List.of(CLONING, EXTENDING, IMPROVING, LOCKING);

    void renderCurrentMap(PoseStack poseStack, ItemStack mapStack, int leftPos, int topPos, MapRenderer mapRenderer);

    default void renderResultingMap(PoseStack poseStack, ItemStack mapStack, int leftPos, int topPos, MapRenderer mapRenderer) {
        renderCurrentMap(poseStack, mapStack, leftPos, topPos, mapRenderer);
    }

    @Environment(EnvType.CLIENT)
    interface MapRenderer {
        void render(int x, int y, float scale);
    }

    @Environment(EnvType.CLIENT)
    class Cloning implements CaveMapUpgrade.Cloning, ClientCaveMapUpgrade {
        public void renderCurrentMap(PoseStack poseStack, ItemStack mapStack, int leftPos, int topPos, MapRenderer mapRenderer) {
            RenderSystem.setShaderTexture(0, CARTOGRAPHY_TEXTURE);
            CartographyTableScreen.blit(poseStack, leftPos + 83, topPos + 13, 132, 0, 50, 50);
            mapRenderer.render(leftPos + 86, topPos + 16, 0.34f);
            RenderSystem.setShaderTexture(0, CARTOGRAPHY_TEXTURE);
            poseStack.pushPose();
            poseStack.translate(0.0f, 0.0f, 1.0f);
            CartographyTableScreen.blit(poseStack, leftPos + 67, topPos + 29, 132, 0, 50, 50);
            mapRenderer.render(leftPos + 70, topPos + 32, 0.34f);
            poseStack.popPose();
        }
    }

    @Environment(EnvType.CLIENT)
    class Extending implements CaveMapUpgrade.Extending, ClientCaveMapUpgrade {
        public void renderCurrentMap(PoseStack poseStack, ItemStack mapStack, int leftPos, int topPos, MapRenderer mapRenderer) {
            RenderSystem.setShaderTexture(0, CARTOGRAPHY_TEXTURE);
            CartographyTableScreen.blit(poseStack, leftPos + 67, topPos + 13, 66, 0, 66, 66);
            mapRenderer.render(leftPos + 85, topPos + 31, 0.226f);
        }
    }

    @Environment(EnvType.CLIENT)
    class Improving implements CaveMapUpgrade.Improving, ClientCaveMapUpgrade {
        public void renderCurrentMap(PoseStack poseStack, ItemStack mapStack, int leftPos, int topPos, MapRenderer mapRenderer) {
            RenderSystem.setShaderTexture(0, CARTOGRAPHY_TEXTURE);
            CartographyTableScreen.blit(poseStack, leftPos + 67, topPos + 13, 0, 0, 66, 66);
            mapRenderer.render(leftPos + 71, topPos + 17, 0.45f);
            RenderSystem.setShaderTexture(0, CARTOGRAPHY_TEXTURE);
            poseStack.pushPose();
            poseStack.translate(0.0f, 0.0f, 1.0f);
            int eye = 132 + 16 * CaveMapTagManager.getVisionLevel(mapStack);
            CartographyTableScreen.blit(poseStack, leftPos + 112, topPos + 60, eye, 50, 16, 16);
            poseStack.popPose();
        }

        public void renderResultingMap(PoseStack poseStack, ItemStack mapStack, int leftPos, int topPos, MapRenderer mapRenderer) {
            ItemStack newStack = upgrade(mapStack);
            renderCurrentMap(poseStack, newStack, leftPos, topPos, mapRenderer);
        }
    }

    @Environment(EnvType.CLIENT)
    class Locking implements CaveMapUpgrade.Locking, ClientCaveMapUpgrade {
        public void renderCurrentMap(PoseStack poseStack, ItemStack mapStack, int leftPos, int topPos, MapRenderer mapRenderer) {
            RenderSystem.setShaderTexture(0, CARTOGRAPHY_TEXTURE);
            CartographyTableScreen.blit(poseStack, leftPos + 67, topPos + 13, 0, 0, 66, 66);
            mapRenderer.render(leftPos + 71, topPos + 17, 0.45f);
            RenderSystem.setShaderTexture(0, CartographyTableScreenAccessor.getBgLocation());
            poseStack.pushPose();
            poseStack.translate(0.0f, 0.0f, 1.0f);
            CartographyTableScreen.blit(poseStack, leftPos + 66, topPos + 12, 0, 166, 66, 66);
            poseStack.popPose();
        }
    }
}
