package ir.mehradn.cavesurvey.item;

import eu.pb4.polymer.core.api.item.PolymerItem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EmptyCaveMapItem extends ComplexItem implements PolymerItem {
    public EmptyCaveMapItem(Properties properties) {
        super(properties);
    }

    public Item getPolymerItem(ItemStack itemStack, @Nullable ServerPlayer player) {
        return Items.MAP;
    }

    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        if (level.isClientSide)
            return InteractionResultHolder.success(stack);

        if (!player.getAbilities().instabuild)
            stack.shrink(1);
        player.awardStat(Stats.ITEM_USED.get(this));
        player.level.playSound(null, player, SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, player.getSoundSource(), 1.0f, 1.0f);
        player.level.playSound(null, player, SoundEvents.SCULK_CLICKING, player.getSoundSource(), 1.0f, 1.0f);

        // TODO: CHANGE
        ItemStack newStack = MapItem.create(level, player.getBlockX(), player.getBlockZ(), (byte)0, true, false);
        if (stack.isEmpty())
            return InteractionResultHolder.consume(newStack);
        if (!player.getInventory().add(newStack.copy()))
            player.drop(newStack, false);
        return InteractionResultHolder.consume(stack);
    }
}
