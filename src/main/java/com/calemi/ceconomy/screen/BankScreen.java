package com.calemi.ceconomy.screen;

import com.calemi.ceconomy.api.currency.inventory.BlockCurrencyInventory;
import com.calemi.ceconomy.block.entity.BankBlockEntity;
import com.calemi.ceconomy.packet.BankDepositPacket;
import com.calemi.ceconomy.packet.BankWithdrawPacket;
import com.calemi.ceconomy.registry.BlockRegistry;
import com.calemi.ceconomy.screen.handler.BankScreenHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public class BankScreen extends AbstractBankScreen<BankScreenHandler> {

    private final BankBlockEntity bank;
    private final BlockCurrencyInventory bankCurrencyInv;

    public BankScreen(BankScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);

        backgroundHeight = 171;
        playerInventoryTitleY = 78;

        bank = handler.getBank();
        bankCurrencyInv = bank.getCurrencyInventory();
    }

    @Override
    public long getCurrency() {
        return bankCurrencyInv.getCurrency();
    }

    @Override
    public long getCurrencyCapacity() {
        return bankCurrencyInv.getCurrencyCapacity();
    }

    @Override
    public void deposit(long amount) {
        BankDepositPacket.send(bank.getPos(), amount);
    }

    @Override
    public void withdraw(long amount) {
        BankWithdrawPacket.send(bank.getPos(), amount);
    }

    @Override
    public ItemStack getWalletStack() {
        return bank.getStack(0);
    }

    @Override
    public Block getBankToRender() {
        return BlockRegistry.BANK;
    }
}