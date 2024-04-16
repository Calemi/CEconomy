package com.calemi.ceconomy.api.item;

import com.calemi.ceconomy.api.currency.inventory.CurrencyInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface IBankCard {

    CurrencyInventory getLinkedCurrencyInventory(PlayerEntity player, ItemStack stack);
}
