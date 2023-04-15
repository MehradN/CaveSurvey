package ir.mehradn.cavesurvey.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import ir.mehradn.cavesurvey.util.CaveMapTagManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @WrapOperation(method = "getTooltipLines", at = @At(value = "INVOKE", ordinal = 0, target = "Ljava/util/List;add(Ljava/lang/Object;)Z"))
    private boolean addCaveMapHoverText(List<Component> instance, Object component, Operation<Boolean> original,
                                        Player player, TooltipFlag isAdvanced) {
        original.call(instance, component);

        ItemStack stack = (ItemStack)(Object)this;
        if (!isAdvanced.isAdvanced() || !CaveMapTagManager.getClientCaveMap(stack) || CaveMapTagManager.getLore(stack) == -1)
            return true;

        Integer id = MapItem.getMapId(stack);
        MapItemSavedData data = (id == null || player == null ? null : MapItem.getSavedData(id, player.level));
        CompoundTag tag = stack.getTag();
        if (id == null || data == null || tag == null) {
            if (isAdvanced.isAdvanced())
                instance.add(Component.translatable("filled_map.unknown").withStyle(ChatFormatting.GRAY));
            return true;
        }

        int toBeScaled = tag.getInt(MapItem.MAP_SCALE_TAG);
        boolean toBeLocked = tag.getBoolean(MapItem.MAP_LOCK_TAG);

        if (data.locked || toBeLocked)
            instance.add(Component.translatable("filled_map.locked").withStyle(ChatFormatting.GRAY));

        if (toBeScaled == 0 || !toBeLocked)
            instance.add(Component.translatable("filled_map.id", id).withStyle(ChatFormatting.GRAY));

        int sight = Math.min(CaveMapTagManager.getSightLevel(stack), 2);
        instance.add(Component.translatable("filled_cave_map.sight_range", 1 << (sight + 4)).withStyle(ChatFormatting.GRAY));
        instance.add(Component.translatable("filled_cave_map.sight_level", sight, 2).withStyle(ChatFormatting.GRAY));

        int scale = Math.min(data.scale + toBeScaled, 2);
        instance.add(Component.translatable("filled_map.scale", 1 << scale).withStyle(ChatFormatting.GRAY));
        instance.add(Component.translatable("filled_map.level", scale, 2).withStyle(ChatFormatting.GRAY));

        return true;
    }

    @WrapOperation(method = "getTooltipLines", at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/CompoundTag;getList(Ljava/lang/String;I)Lnet/minecraft/nbt/ListTag;"))
    private ListTag truncateLore(CompoundTag instance, String key, int tagType, Operation<ListTag> original,
                                 Player player, TooltipFlag isAdvanced) {
        ListTag list = original.call(instance, key, tagType);
        if (isAdvanced.isAdvanced()) {
            ItemStack stack = (ItemStack)(Object)this;
            int truncate = Mth.clamp(CaveMapTagManager.getLore(stack), 0, list.size());
            for (int i = 0; i < truncate; i++)
                list.remove(0);
        }
        return list;
    }
}
