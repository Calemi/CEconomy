package com.calemi.ceconomy.api.item;

import com.calemi.ceconomy.registry.ValuableItemReloadListener;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

public class ValuableItemHelper {

    public static ValuableItem getItemValue(ItemStack stack) {
        return getItemValue(stack.getItem());
    }

    public static ValuableItem getItemValue(Item item) {

        for (ValuableItem valuableItem : ValuableItemReloadListener.getValuableItems()) {

            if (valuableItem.getItem() == item) {
                return valuableItem;
            }
        }

        return null;
    }

    public static ArrayList<ValuableItem> getWalletOutputItemList() {
        ArrayList<ValuableItem> list = new ArrayList<>(ValuableItemReloadListener.getValuableItems());
        list.removeIf(valuableItem -> !valuableItem.canWalletDeposit());
        return list;
    }
}
