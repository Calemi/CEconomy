package com.calemi.ceconomy.api.currency.inventory;

import com.calemi.ceconomy.api.currency.CurrencyHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public class ItemCurrencyInventory extends CurrencyInventory {

    private final ItemStack stack;

    public ItemCurrencyInventory(long capacity, ItemStack stack) {
        super(capacity);
        this.stack = stack;
    }

    @Override
    public long getCurrency() {
        return CurrencyHelper.readFromNBT(stack.getOrCreateNbt());
    }

    @Override
    public void setCurrency(long amount) {
        NbtCompound nbt = stack.getOrCreateNbt();
        CurrencyHelper.writeToNBT(nbt, amount);
        stack.setNbt(nbt);
    }
}
