package ir.mehradn.cavesurvey.item;

import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.core.api.item.PolymerItemUtils;
import ir.mehradn.cavesurvey.mixin.accessor.MapItemAccessor;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CaveMapItem extends MapItem implements PolymerItem {

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

    public void update(Level level, Entity viewer, MapItemSavedData data) { } // TODO

    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (level.isClientSide)
            return;
        MapItemSavedData data = getSavedData(stack, level);
        if (data == null)
            return;
        if (entity instanceof Player player)
            data.tickCarriedBy(player, stack);
        // TODO: Add some sort of automatic update
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

        this.update(level, player, data);
        return InteractionResultHolder.success(stack);
    }
}
