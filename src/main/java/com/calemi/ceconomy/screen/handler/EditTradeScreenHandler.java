package com.calemi.ceconomy.screen.handler;

import com.calemi.ccore.api.screen.handler.BaseScreenHandler;
import com.calemi.ceconomy.block.entity.TradingPostBlockEntity;
import com.calemi.ceconomy.registry.ScreenHandlerTypeRegistry;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.slot.Slot;

public class EditTradeScreenHandler extends BaseScreenHandler {

    private final TradingPostBlockEntity tradingPost;

    public EditTradeScreenHandler(int syncId, PlayerInventory playerInv, PacketByteBuf data) {
        this(syncId, playerInv, playerInv.player.getWorld().getBlockEntity(data.readBlockPos()));
    }

    public EditTradeScreenHandler(int syncId, PlayerInventory playerInv, BlockEntity blockEntity) {
        super(ScreenHandlerTypeRegistry.EDIT_TRADE, syncId);

        tradingPost = (TradingPostBlockEntity) blockEntity;

        //Trading Post Inventory
        for (int rowY = 0; rowY < 3; rowY++) {
            for (int rowX = 0; rowX < 9; rowX++) {
                addSlot(new Slot(tradingPost, rowX + rowY * 9, 8 + rowX * 18, 83 + rowY * 18));
            }
        }

        addPlayerInventory(playerInv, 152);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    @Override
    public int getContainerSize() {
        return 27;
    }

    public TradingPostBlockEntity getTradingPost() {
        return tradingPost;
    }
}
