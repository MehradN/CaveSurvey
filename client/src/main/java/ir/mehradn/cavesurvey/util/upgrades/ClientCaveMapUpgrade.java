package ir.mehradn.cavesurvey.util.upgrades;

import ir.mehradn.cavesurvey.CaveSurveyClient;
import ir.mehradn.cavesurvey.mixin.client.accessor.CartographyTableScreenAccessor;
import ir.mehradn.cavesurvey.util.CaveMapTagManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
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

    void renderCurrentMap(GuiGraphics guiGraphics, ItemStack mapStack, int leftPos, int topPos, MapRenderer mapRenderer);

    default void renderResultingMap(GuiGraphics guiGraphics, ItemStack mapStack, int leftPos, int topPos, MapRenderer mapRenderer) {
        renderCurrentMap(guiGraphics, mapStack, leftPos, topPos, mapRenderer);
    }

    @FunctionalInterface
    @Environment(EnvType.CLIENT)
    interface MapRenderer {
        void render(int x, int y, float scale);
    }

    @Environment(EnvType.CLIENT)
    class Cloning implements CaveMapUpgrade.Cloning, ClientCaveMapUpgrade {
        @Override
        public void renderCurrentMap(GuiGraphics guiGraphics, ItemStack mapStack, int leftPos, int topPos, MapRenderer mapRenderer) {
            guiGraphics.blit(CARTOGRAPHY_TEXTURE, leftPos + 83, topPos + 13, 132, 0, 50, 50);
            mapRenderer.render(leftPos + 86, topPos + 16, 0.34f);
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(0.0f, 0.0f, 1.0f);
            guiGraphics.blit(CARTOGRAPHY_TEXTURE, leftPos + 67, topPos + 29, 132, 0, 50, 50);
            mapRenderer.render(leftPos + 70, topPos + 32, 0.34f);
            guiGraphics.pose().popPose();
        }
    }

    @Environment(EnvType.CLIENT)
    class Extending implements CaveMapUpgrade.Extending, ClientCaveMapUpgrade {
        @Override
        public void renderCurrentMap(GuiGraphics guiGraphics, ItemStack mapStack, int leftPos, int topPos, MapRenderer mapRenderer) {
            guiGraphics.blit(CARTOGRAPHY_TEXTURE, leftPos + 67, topPos + 13, 66, 0, 66, 66);
            mapRenderer.render(leftPos + 85, topPos + 31, 0.226f);
        }
    }

    @Environment(EnvType.CLIENT)
    class Improving implements CaveMapUpgrade.Improving, ClientCaveMapUpgrade {
        @Override
        public void renderCurrentMap(GuiGraphics guiGraphics, ItemStack mapStack, int leftPos, int topPos, MapRenderer mapRenderer) {
            guiGraphics.blit(CARTOGRAPHY_TEXTURE, leftPos + 67, topPos + 13, 0, 0, 66, 66);
            mapRenderer.render(leftPos + 71, topPos + 17, 0.45f);
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(0.0f, 0.0f, 1.0f);
            int eye = 132 + 16 * CaveMapTagManager.getSightLevel(mapStack);
            guiGraphics.blit(CARTOGRAPHY_TEXTURE, leftPos + 112, topPos + 60, eye, 50, 16, 16);
            guiGraphics.pose().popPose();
        }

        @Override
        public void renderResultingMap(GuiGraphics guiGraphics, ItemStack mapStack, int leftPos, int topPos, MapRenderer mapRenderer) {
            ItemStack newStack = upgrade(mapStack);
            renderCurrentMap(guiGraphics, newStack, leftPos, topPos, mapRenderer);
        }
    }

    @Environment(EnvType.CLIENT)
    class Locking implements CaveMapUpgrade.Locking, ClientCaveMapUpgrade {
        @Override
        public void renderCurrentMap(GuiGraphics guiGraphics, ItemStack mapStack, int leftPos, int topPos, MapRenderer mapRenderer) {
            guiGraphics.blit(CARTOGRAPHY_TEXTURE, leftPos + 67, topPos + 13, 0, 0, 66, 66);
            mapRenderer.render(leftPos + 71, topPos + 17, 0.45f);
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(0.0f, 0.0f, 1.0f);
            guiGraphics.blit(CartographyTableScreenAccessor.getBgLocation(),
                leftPos + 66, topPos + 12, 0, 166, 66, 66);
            guiGraphics.pose().popPose();
        }
    }
}
