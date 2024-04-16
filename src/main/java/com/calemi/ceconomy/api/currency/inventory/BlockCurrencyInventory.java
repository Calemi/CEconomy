package com.calemi.ceconomy.api.currency.inventory;

public class BlockCurrencyInventory extends CurrencyInventory {

    private long currency = 0;

    public BlockCurrencyInventory(long capacity) {
        super(capacity);
    }

    @Override
    public long getCurrency() {
        return currency;
    }

    @Override
    public void setCurrency(long amount) {
        currency = amount;
    }
}
