package com.calemi.ceconomy.item;

import com.calemi.ceconomy.api.currency.inventory.CurrencyInventory;
import com.calemi.ceconomy.api.currency.inventory.EnderCurrencyInventory;
import com.calemi.ceconomy.api.item.IBankCard;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class EnderBankCardItem extends Item implements IBankCard {

    public EnderBankCardItem() {
        super(new FabricItemSettings().maxCount(1));
    }

    @Override
    public CurrencyInventory getLinkedCurrencyInventory(PlayerEntity player, ItemStack stack) {
        return new EnderCurrencyInventory(player);
    }
}
