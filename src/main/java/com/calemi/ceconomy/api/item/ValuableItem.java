package com.calemi.ceconomy.api.item;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class ValuableItem implements Comparable<ValuableItem> {

    private final String itemKey;
    private final long value;
    private final boolean walletOutput;

    public ValuableItem(String itemKey, long value, boolean walletOutput) {
        this.itemKey = itemKey;
        this.value = value;
        this.walletOutput = walletOutput;
    }

    public String getItemKey() {
        return itemKey;
    }

    public Item getItem() {
        return Registries.ITEM.get(new Identifier(itemKey));
    }

    public long getValue() {
        return value;
    }

    public boolean canWalletDeposit() {
        return walletOutput;
    }

    @Override
    public int compareTo(ValuableItem employee) {
        return (int)(this.value - employee.getValue()) + itemKey.compareTo(employee.getItemKey());
    }

    @Override
    public String toString() {
        return "{itemKey: " + itemKey + ", value: " + value + ", walletDeposit: " + walletOutput + "}";
    }
}
