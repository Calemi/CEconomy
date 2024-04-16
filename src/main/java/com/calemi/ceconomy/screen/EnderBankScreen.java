package com.calemi.ceconomy.screen;

import com.calemi.ceconomy.api.currency.inventory.EnderCurrencyInventory;
import com.calemi.ceconomy.packet.EnderBankDepositPacket;
import com.calemi.ceconomy.packet.EnderBankWithdrawPacket;
import com.calemi.ceconomy.registry.BlockRegistry;
import com.calemi.ceconomy.screen.handler.EnderBankScreenHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public class EnderBankScreen extends AbstractBankScreen<EnderBankScreenHandler> {

    private final EnderCurrencyInventory currencyInventory;

    public EnderBankScreen(EnderBankScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);

        currencyInventory = new EnderCurrencyInventory(inventory.player);
    }

    @Override
    public long getCurrency() {
        return getScreenHandler().getCurrency();
    }

    @Override
    public long getCurrencyCapacity() {
        return currencyInventory.getCurrencyCapacity();
    }

    @Override
    public void deposit(long amount) {
        EnderBankDepositPacket.send(amount);
    }

    @Override
    public void withdraw(long amount) {
        EnderBankWithdrawPacket.send(amount);
    }

    @Override
    public ItemStack getWalletStack() {
        return getScreenHandler().getBankInv().getStack(0);
    }

    @Override
    public Block getBankToRender() {
        return BlockRegistry.ENDER_BANK;
    }

}