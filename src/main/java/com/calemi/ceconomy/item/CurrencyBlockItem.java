package com.calemi.ceconomy.item;

import com.calemi.ceconomy.api.currency.inventory.ICurrencyInventoryItem;
import com.calemi.ceconomy.api.currency.inventory.ItemCurrencyInventory;
import com.calemi.ceconomy.config.CEconomyConfig;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;

public class CurrencyBlockItem extends BlockItem implements ICurrencyInventoryItem {

    private final long currencyCapacity;

    public CurrencyBlockItem(Block block, Settings settings, long currencyCapacity) {
        super(block, settings);
        this.currencyCapacity = currencyCapacity;
    }

    @Override
    public ItemCurrencyInventory getCurrencyInventory(ItemStack stack) {
        return new ItemCurrencyInventory(currencyCapacity, stack);
    }

    @Override
    public boolean showCapacityInTooltip(ItemStack stack) {
        return true;
    }
}
