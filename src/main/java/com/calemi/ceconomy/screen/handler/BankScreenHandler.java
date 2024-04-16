package com.calemi.ceconomy.screen.handler;

import com.calemi.ccore.api.screen.handler.BaseScreenHandler;
import com.calemi.ceconomy.block.entity.BankBlockEntity;
import com.calemi.ceconomy.registry.ScreenHandlerTypeRegistry;
import com.calemi.ceconomy.screen.handler.slot.ValuableItemSlot;
import com.calemi.ceconomy.screen.handler.slot.WalletSlot;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.network.PacketByteBuf;

public class BankScreenHandler extends BaseScreenHandler {

    private final BankBlockEntity bank;

    private final Inventory bankInv;

    public BankScreenHandler(int syncId, PlayerInventory playerInv, PacketByteBuf data) {
        this(syncId, playerInv, playerInv.player.getWorld().getBlockEntity(data.readBlockPos()));
    }

    public BankScreenHandler(int syncId, PlayerInventory playerInv, BlockEntity blockEntity) {
        super(ScreenHandlerTypeRegistry.BANK, syncId);

        bank = (BankBlockEntity) blockEntity;

        checkSize(((Inventory) blockEntity), 2);
        bankInv = ((Inventory) blockEntity);
        bankInv.onOpen(playerInv.player);

        addSlot(new WalletSlot(bankInv, 0, 26, 19));
        addSlot(new ValuableItemSlot(bankInv, 1, 26, 56));

        addPlayerInventory(playerInv, 89);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    @Override
    public int getContainerSize() {
        return 2;
    }

    public BankBlockEntity getBank() {
        return bank;
    }
}
