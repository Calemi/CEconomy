package com.calemi.ceconomy.screen.handler;

import com.calemi.ccore.api.screen.handler.BaseScreenHandler;
import com.calemi.ceconomy.block.entity.TradingPostBlockEntity;
import com.calemi.ceconomy.registry.ScreenHandlerTypeRegistry;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;

public class BulkTradeScreenHandler extends BaseScreenHandler {

    private final TradingPostBlockEntity tradingPost;
    private boolean canAccessEdit;

    public BulkTradeScreenHandler(int syncId, PlayerInventory playerInv, PacketByteBuf data) {
        this(syncId, playerInv, playerInv.player.getWorld().getBlockEntity(data.readBlockPos()), data.readBoolean());
    }

    public BulkTradeScreenHandler(int syncId, PlayerInventory playerInv, BlockEntity blockEntity, boolean canAccessEdit) {
        super(ScreenHandlerTypeRegistry.BULK_TRADE, syncId);
        this.tradingPost = (TradingPostBlockEntity) blockEntity;
        this.canAccessEdit = canAccessEdit;
        addPlayerInventory(playerInv, 115);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    @Override
    public int getContainerSize() {
        return 0;
    }

    public TradingPostBlockEntity getTradingPost() {
        return tradingPost;
    }

    public boolean canAccessEdit() {
        return canAccessEdit;
    }
}
