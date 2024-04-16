package com.calemi.ceconomy.screen.handler;

import com.calemi.ccore.api.screen.handler.BaseScreenHandler;
import com.calemi.ceconomy.api.currency.inventory.EnderCurrencyInventory;
import com.calemi.ceconomy.api.screen.handler.ScreenHandlerHelper;
import com.calemi.ceconomy.packet.EnderBankSyncPacket;
import com.calemi.ceconomy.registry.ScreenHandlerTypeRegistry;
import com.calemi.ceconomy.screen.handler.slot.ValuableItemSlot;
import com.calemi.ceconomy.screen.handler.slot.WalletSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.network.ServerPlayerEntity;

public class EnderBankScreenHandler extends BaseScreenHandler {

    private long currency;

    private final Inventory bankInv;

    public EnderBankScreenHandler(int syncId, PlayerInventory playerInv, PacketByteBuf data) {
        this(syncId, playerInv, data.readLong());
    }

    public EnderBankScreenHandler(int syncId, PlayerInventory playerInv, long currency) {
        super(ScreenHandlerTypeRegistry.ENDER_BANK, syncId);

        this.currency = currency;

        bankInv = new SimpleInventory(2);
        bankInv.onOpen(playerInv.player);

        addSlot(new WalletSlot(bankInv, 0, 26, 19));
        addSlot(new ValuableItemSlot(bankInv, 1, 26, 56));

        addPlayerInventory(playerInv, 89);
    }

    @Override
    public void onSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player) {
        super.onSlotClick(slotIndex, button, actionType, player);

        EnderCurrencyInventory enderCurrencyInv = new EnderCurrencyInventory(player);
        ScreenHandlerHelper.handleDepositCurrencySlot(bankInv, 1, enderCurrencyInv);

        if (!player.getWorld().isClient()) EnderBankSyncPacket.send((ServerPlayerEntity) player, enderCurrencyInv.getCurrency());
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        super.onContentChanged(inventory);
    }

    @Override
    public void onClosed(PlayerEntity player) {

        for (int index = 0; index < getContainerSize(); index++) {

            if (!bankInv.getStack(index).isEmpty()) {
                ItemEntity itemEntity = new ItemEntity(player.getWorld(), player.getX(), player.getY(), player.getZ(), bankInv.getStack(index));
                player.getWorld().spawnEntity(itemEntity);
            }
        }

        super.onClosed(player);
    }

    public void setCurrency(long currency) {
        this.currency = currency;
    }

    public long getCurrency() {
        return currency;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    @Override
    public int getContainerSize() {
        return 2;
    }

    public Inventory getBankInv() {
        return bankInv;
    }
}
