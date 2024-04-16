package com.calemi.ceconomy.inventory;

import com.calemi.ceconomy.api.item.ValuableItemHelper;
import com.calemi.ceconomy.api.item.IPlaceInCurrencyContainer;
import com.calemi.ceconomy.api.item.ValuableItem;
import com.calemi.ceconomy.block.entity.BankBlockEntity;
import com.calemi.ceconomy.item.WalletItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface BankInventory extends ImplementedInventory {

    @Override
    default boolean canInsert(int slot, ItemStack stack, @Nullable Direction side) {

        if (slot == BankBlockEntity.WALLET_SLOT) {
            return stack.getItem() instanceof WalletItem;
        }

        if (slot == BankBlockEntity.VALUABLE_ITEM_SLOT) {
            ValuableItem value = ValuableItemHelper.getItemValue(stack.getItem());
            return (value != null && value.getValue() > 0) || stack.getItem() instanceof IPlaceInCurrencyContainer;
        }

        return true;
    }
}