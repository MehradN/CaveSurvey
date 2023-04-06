package ir.mehradn.cavesurvey.item;

import com.google.common.collect.Iterables;
import com.google.common.collect.LinkedHashMultiset;
import com.google.common.collect.Multisets;
import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.core.api.item.PolymerItemUtils;
import ir.mehradn.cavesurvey.mixin.accessor.MapItemAccessor;
import ir.mehradn.cavesurvey.util.CaveMappingAlgorithm;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class CaveMapItem extends MapItem implements PolymerItem {
    public static final int MAP_SIZE = 128;

    public CaveMapItem(Properties properties) {
        super(properties);
    }

    public Item getPolymerItem(ItemStack itemStack, ServerPlayer player) {
        return Items.FILLED_MAP;
    }

    public ItemStack getPolymerItemStack(ItemStack itemStack, TooltipFlag context, ServerPlayer player) {
        ItemStack out = PolymerItemUtils.createItemStack(itemStack, context, player);
        Integer id = getMapId(itemStack);
        if (id != null)
            MapItemAccessor.InvokeStoreMapData(out, id);
        return out;
    }

    public static ItemStack create(Level level, int x, int z, byte scale, boolean trackingPosition, boolean unlimitedTracking) {
        ItemStack stack = new ItemStack(ModItems.FILLED_CAVE_MAP);
        MapItemAccessor.InvokeCreateAndStoreSavedData(stack, level, x, z, scale, trackingPosition, unlimitedTracking, level.dimension());
        return stack;
    }

    public static void updateMap(Level level, Entity viewer, MapItemSavedData data) {
        if (level.dimension() != data.dimension || !(viewer instanceof Player player))
            return;

        BlockPos headPos = player.blockPosition().above();
        int scale = 1 << data.scale;
        int centerX = data.centerX;
        int centerZ = data.centerZ;
        int relativeX = (headPos.getX() - centerX) / scale + MAP_SIZE / 2;
        int relativeZ = (headPos.getZ() - centerZ) / scale + MAP_SIZE / 2;
        int blockRadius = 16; // TODO: Add a system to change this
        int pixelRadius = blockRadius / scale;

        CaveMappingAlgorithm.PixelMatrix matrix = CaveMappingAlgorithm.run(headPos, blockRadius, level);

        for (int pixelX = relativeX - pixelRadius; pixelX < relativeX + pixelRadius; pixelX++) {
            double previousAverageHeight = 0.0;
            for (int pixelZ = relativeZ - pixelRadius; pixelZ < relativeZ + pixelRadius; pixelZ++) {
                if (pixelX < 0 || pixelZ < 0 || pixelX >= MAP_SIZE || pixelZ >= MAP_SIZE)
                    continue;
                int distance = Mth.square(pixelX - relativeX) + Mth.square(pixelZ - relativeZ);
                int realX = (centerX / scale + pixelX - MAP_SIZE / 2) * scale;
                int realZ = (centerZ / scale + pixelZ - MAP_SIZE / 2) * scale;

                double averageHeight = 0.0;
                double averageFluidDepth = 0.0;
                int pixelCount = 0;
                int brightCount = 0;
                LinkedHashMultiset<MaterialColor> innerColors = LinkedHashMultiset.create();
                for (int innerX = 0; innerX < scale; innerX++) {
                    for (int innerZ = 0; innerZ < scale; innerZ++) {
                        int x = realX + innerX;
                        int z = realZ + innerZ;
                        data.checkBanners(level, x, z);

                        CaveMappingAlgorithm.PixelInfo info = matrix.get(x, z);
                        if (info == null)
                            continue;

                        pixelCount++;
                        if (info.reachesSky())
                            brightCount++;

                        averageFluidDepth += info.fluidDepth();
                        averageHeight += info.y();
                        innerColors.add(info.color());
                    }
                }

                if (pixelCount == 0) {
                    previousAverageHeight = 0;
                    continue;
                }
                averageHeight /= pixelCount;
                averageFluidDepth /= pixelCount;

                MaterialColor color = Iterables.getFirst(Multisets.copyHighestCountFirst(innerColors), MaterialColor.NONE);

                int brightnessLevel;
                if (color == MaterialColor.WATER) {
                    double darkness = averageFluidDepth * 0.1 + (double)(pixelX + pixelZ & 1) * 0.2;
                    if (darkness > 0.9)
                        brightnessLevel = 0;
                    else if (darkness > 0.5)
                        brightnessLevel = 1;
                    else
                        brightnessLevel = 2;
                } else {
                    double brightness = (averageHeight - previousAverageHeight) * 4.0 / (double)(scale + 4) + ((double)(pixelX + pixelZ & 1) - 0.5) * 0.4;
                    if (brightness < -0.6)
                        brightnessLevel = 0;
                    else if (brightness < 0.6)
                        brightnessLevel = 1;
                    else
                        brightnessLevel = 2;
                }
                if (brightCount >= pixelCount / 2)
                    brightnessLevel++;

                previousAverageHeight = averageHeight;
                if (pixelCount < Mth.square(scale) / 2 || distance >= Mth.square(pixelRadius))
                    continue;

                MaterialColor.Brightness brightness = Arrays.asList(
                    MaterialColor.Brightness.LOWEST,
                    MaterialColor.Brightness.LOW,
                    MaterialColor.Brightness.NORMAL,
                    MaterialColor.Brightness.HIGH
                ).get(brightnessLevel);
                data.updateColor(pixelX, pixelZ, color.getPackedId(brightness));
            }
        }
    }

    public void update(Level level, Entity viewer, MapItemSavedData data) {
        updateMap(level, viewer, data);
    }

    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (level.isClientSide)
            return;
        MapItemSavedData data = getSavedData(stack, level);
        if (data == null)
            return;
        if (entity instanceof Player player)
            data.tickCarriedBy(player, stack);
    }

    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
        Integer id = getMapId(stack);
        if (id != null && !isAdvanced.isAdvanced() && !stack.hasCustomHoverName())
            tooltipComponents.add(Component.literal("#" + id).withStyle(ChatFormatting.GRAY));
    }

    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        if (level.isClientSide)
            return InteractionResultHolder.success(stack);

        MapItemSavedData data = getSavedData(stack, level);
        if (data == null)
            return InteractionResultHolder.fail(stack);
        if (data.locked)
            return InteractionResultHolder.consume(stack);

        update(level, player, data);
        return InteractionResultHolder.success(stack);
    }
}
