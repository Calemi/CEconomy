package com.calemi.ceconomy.screen.handler.slot;

import com.calemi.ceconomy.api.item.IPlaceInCurrencyContainer;
import com.calemi.ceconomy.api.item.ValuableItem;
import com.calemi.ceconomy.api.item.ValuableItemHelper;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class ValuableItemSlot extends Slot {

    public ValuableItemSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        ValuableItem value = ValuableItemHelper.getItemValue(stack.getItem());
        return (value != null && value.getValue() > 0) || stack.getItem() instanceof IPlaceInCurrencyContainer;
    }
}
