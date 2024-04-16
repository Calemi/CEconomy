package com.calemi.ceconomy.screen.handler;

import com.calemi.ccore.api.screen.handler.BaseScreenHandler;
import com.calemi.ceconomy.api.currency.inventory.ItemCurrencyInventory;
import com.calemi.ceconomy.api.screen.handler.ScreenHandlerHelper;
import com.calemi.ceconomy.item.WalletItem;
import com.calemi.ceconomy.registry.ScreenHandlerTypeRegistry;
import com.calemi.ceconomy.screen.handler.slot.ValuableItemSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.slot.SlotActionType;

public class WalletScreenHandler extends BaseScreenHandler {

    private final ItemStack walletStack;
    private final Inventory walletInv;

    public WalletScreenHandler(int syncId, PlayerInventory playerInv, PacketByteBuf data) {
        this(syncId, playerInv, new SimpleInventory(4), data.readItemStack());
    }

    public WalletScreenHandler(int syncId, PlayerInventory playerInv, Inventory walletInv, ItemStack walletStack) {
        super(ScreenHandlerTypeRegistry.WALLET, syncId);

        this.walletStack = walletStack;

        this.walletInv = walletInv;
        addSlot(new ValuableItemSlot(walletInv, 0, 17, 35));

        addPlayerInventory(playerInv, 77);
    }

    @Override
    public void onSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player) {
        super.onSlotClick(slotIndex, button, actionType, player);

        if (walletStack.getItem() instanceof WalletItem walletItem) {

            ItemCurrencyInventory walletCurrencyInv = walletItem.getCurrencyInventory(walletStack);
            ScreenHandlerHelper.handleDepositCurrencySlot(walletInv, 0, walletCurrencyInv);
        }
    }

    @Override
    public void onClosed(PlayerEntity player) {


        super.onClosed(player);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return walletStack != null && !walletStack.isEmpty();
    }

    @Override
    public int getContainerSize() {
        return 1;
    }

    public ItemStack getWalletStack() {
        return walletStack;
    }
}
