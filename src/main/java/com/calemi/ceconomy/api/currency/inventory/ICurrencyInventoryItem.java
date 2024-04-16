package com.calemi.ceconomy.api.currency.inventory;

import net.minecraft.item.ItemStack;

public interface ICurrencyInventoryItem extends ICurrencyInventory {

    ItemCurrencyInventory getCurrencyInventory(ItemStack stack);
    boolean showCapacityInTooltip(ItemStack stack);
}
