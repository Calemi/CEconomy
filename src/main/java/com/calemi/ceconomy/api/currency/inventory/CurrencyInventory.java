package com.calemi.ceconomy.api.currency.inventory;

import com.calemi.ceconomy.main.CEconomyMain;

public abstract class CurrencyInventory {

    private final long currencyCapacity;

    public CurrencyInventory(long currencyCapacity) {
        this.currencyCapacity = currencyCapacity;
    }

    public abstract long getCurrency();
    public abstract void setCurrency(long amount);

    public long getCurrencyCapacity() {
        return currencyCapacity;
    }

    public boolean canDepositCurrency(long amount) {
        return getCurrency() + amount <= getCurrencyCapacity();
    }

    public void depositCurrency(long amount) {

        if (canDepositCurrency(amount)) {
            setCurrency(getCurrency() + amount);
        }
    }

    public boolean tryDepositCurrency(long amount) {

        if (canDepositCurrency(amount)) {
            depositCurrency(amount);
            return true;
        }

        return false;
    }

    public boolean canWithdrawCurrency(long amount) {
        return getCurrency() >= amount;
    }

    public void withdrawCurrency(long amount) {

        if (canWithdrawCurrency(amount)) {
            setCurrency(getCurrency() - amount);
        }
    }

    public boolean tryWithdrawCurrency(long amount) {

        if (canWithdrawCurrency(amount)) {
            withdrawCurrency(amount);
            return true;
        }

        return false;
    }
}
