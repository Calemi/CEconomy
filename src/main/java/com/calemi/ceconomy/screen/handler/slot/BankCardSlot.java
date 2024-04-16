package com.calemi.ceconomy.screen.handler.slot;

import com.calemi.ceconomy.api.item.IBankCard;
import com.calemi.ceconomy.item.BankCardItem;
import com.calemi.ceconomy.item.WalletItem;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class BankCardSlot extends Slot {

    public BankCardSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
         return stack.getItem() instanceof IBankCard;
    }
}
