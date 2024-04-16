package com.calemi.ceconomy.api.screen.handler;

import com.calemi.ceconomy.api.currency.*;
import com.calemi.ceconomy.api.currency.inventory.CurrencyInventory;
import com.calemi.ceconomy.api.currency.inventory.ICurrencyInventoryItem;
import com.calemi.ceconomy.api.currency.inventory.ItemCurrencyInventory;
import com.calemi.ceconomy.api.item.IPlaceInCurrencyContainer;
import com.calemi.ceconomy.api.item.ValuableItem;
import com.calemi.ceconomy.api.item.ValuableItemHelper;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

public class ScreenHandlerHelper {

    public static void handleDepositCurrencySlot(Inventory inventory, int slotIndex, CurrencyInventory currencyInventory) {

        ItemStack stackInSlot = inventory.getStack(slotIndex);
        ValuableItem valuableItem = ValuableItemHelper.getItemValue(stackInSlot.getItem());

        //Checks if the stack is valuable.
        if (valuableItem != null && valuableItem.getValue() > 0) {

            long amountToAdd = 0;
            int stacksToRemove = 0;

            //Iterates through every count of the Stack. Ex: a stack of 32 will iterate 32 times.
            for (int i = 0; i < stackInSlot.getCount(); i++) {

                //Checks if the Wallet can fit the added money.
                if (currencyInventory.canDepositCurrency(valuableItem.getValue() + amountToAdd)) {
                    amountToAdd += valuableItem.getValue();
                    stacksToRemove++;
                }

                else break;
            }

            currencyInventory.depositCurrency(amountToAdd);
            inventory.removeStack(slotIndex, stacksToRemove);
        }

        else if (stackInSlot.getItem() instanceof ICurrencyInventoryItem slotCurrencyItem && stackInSlot.getItem() instanceof IPlaceInCurrencyContainer placeInCurrencyContainer) {

            ItemCurrencyInventory slotCurrencyInv = slotCurrencyItem.getCurrencyInventory(stackInSlot);

            long currencyInSlot = slotCurrencyInv.getCurrency();
            long space = currencyInventory.getCurrencyCapacity() - currencyInventory.getCurrency();

            if (placeInCurrencyContainer.consumeInCurrencyContainer(stackInSlot)) {

                if (CurrencyHelper.tryTransferCurrency(slotCurrencyInv, currencyInventory, currencyInSlot)) {
                    inventory.removeStack(slotIndex, 1);
                }
            }

            else if (currencyInSlot >= space) {
                CurrencyHelper.tryTransferCurrency(slotCurrencyInv, currencyInventory, space);
            }

            else {
                CurrencyHelper.tryTransferCurrency(slotCurrencyInv, currencyInventory, currencyInSlot);
            }
        }
    }
}
