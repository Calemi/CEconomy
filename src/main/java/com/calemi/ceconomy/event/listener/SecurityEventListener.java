package com.calemi.ceconomy.event.listener;

import com.calemi.ccore.api.event.BlockPlacePostCallback;
import com.calemi.ccore.api.message.OverlayMessageHelper;
import com.calemi.ceconomy.api.security.ISecuredBlock;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;

public class SecurityEventListener {

    public static void init() {

        BlockPlacePostCallback.EVENT.register((pos, world, player, stack, state) -> {

            BlockEntity blockEntity = world.getBlockEntity(pos);

            if (blockEntity instanceof ISecuredBlock securedBlock) {

                securedBlock.getSecurityProfile().setOwner(player);
                blockEntity.markDirty();
            }

            return ActionResult.PASS;
        });

        PlayerBlockBreakEvents.BEFORE.register(Event.DEFAULT_PHASE, (world, player, pos, state, entity) -> {

            if (world.isClient || player.isCreative()) {
                return true;
            }

            BlockEntity blockEntity = world.getBlockEntity(pos);

            if (blockEntity instanceof ISecuredBlock securedBlock) {

                if (!securedBlock.getSecurityProfile().canAccess(player.getUuid())) {

                    OverlayMessageHelper.displayErrorMsg(Text.translatable("error.ceconomy.security.cant_break"), player);

                    return false;
                }
            }

            return true;
        });
    }
}
