package com.calemi.ceconomy.api.currency;

import com.calemi.ceconomy.api.currency.inventory.CurrencyInventory;
import com.calemi.ceconomy.item.WalletItem;
import com.calemi.ceconomy.main.CEconomyMain;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;

public class CurrencyHelper {

    public static boolean tryTransferCurrency(CurrencyInventory sourceInv, CurrencyInventory targetInv, long amount) {

        if (sourceInv.canWithdrawCurrency(amount) && targetInv.canDepositCurrency(amount)) {

            sourceInv.withdrawCurrency(amount);
            targetInv.depositCurrency(amount);

            return true;
        }

        return false;
    }

    public static boolean canTransferCurrency(CurrencyInventory sourceInv, CurrencyInventory targetInv, long amount) {
        return sourceInv.canWithdrawCurrency(amount) && targetInv.canDepositCurrency(amount);
    }

    public static void transferOrFillCurrency(CurrencyInventory sourceInv, CurrencyInventory targetInv, long amount) {

        //Does the item have enough currency. If not, take all.
        if (!sourceInv.canWithdrawCurrency(amount)) {
            amount = sourceInv.getCurrency();
        }

        //Does the block have enough space for the currency. If not, fill it.
        if (!targetInv.canDepositCurrency(amount)) {
            amount = targetInv.getCurrencyCapacity() - targetInv.getCurrency();
        }

        if (amount == 0) return;

        sourceInv.withdrawCurrency(amount);
        targetInv.depositCurrency(amount);
    }

    public static ItemStack getCurrentWallet(PlayerEntity player) {

        //Priority #1 - Held mainhand.
        if (player.getMainHandStack().getItem() instanceof WalletItem) {
            return player.getMainHandStack();
        }

        //Priority #2 - Held offhand.
        if (player.getOffHandStack().getItem() instanceof WalletItem) {
            return player.getOffHandStack();
        }

        //Priority #3 - Curios slot.
        if (CEconomyMain.trinketsLoaded()) {

            ItemStack stack = TrinketsApi.getTrinketComponent(player)
                    .flatMap(component -> Optional.ofNullable(component.getInventory().get("legs")))
                    .flatMap(map -> Optional.ofNullable(map.get("wallet")))
                    .map(inventory -> inventory.getStack(0))
                    .orElse(ItemStack.EMPTY);

            if (stack.getItem() instanceof  WalletItem) {
                return stack;
            }
        }

        //Priority #4 - Inventory (lowest slot id wins).
        for (int i = 0; i < player.getInventory().size(); i++) {

            ItemStack stack = player.getInventory().getStack(i);

            if (stack.getItem() instanceof WalletItem) {
                return stack;
            }
        }

        //No Wallet was found.
        return ItemStack.EMPTY;
    }

    public static MutableText formatCurrency(long amount, boolean showFullNumber) {

        if (!showFullNumber) {

            if (amount > 999999999999999L) {
                return Text.literal(insertCommasLong(amount / 1000000000000000D, true))
                        .append(Text.translatable("text.ceconomy.quadrillion_suffix")).append(" ").append(Text.translatable("text.ceconomy.currency_name"));
            }

            if (amount > 999999999999L) {
                return Text.literal(insertCommasLong(amount / 1000000000000D, true))
                        .append(Text.translatable("text.ceconomy.trillion_suffix")).append(" ").append(Text.translatable("text.ceconomy.currency_name"));
            }

            if (amount > 999999999) {
                return Text.literal(insertCommasLong(amount / 1000000000D, true))
                        .append(Text.translatable("text.ceconomy.billion_suffix")).append(" ").append(Text.translatable("text.ceconomy.currency_name"));
            }

            if (amount > 999999) {
                return Text.literal(insertCommasLong(amount / 1000000D, true))
                        .append(Text.translatable("text.ceconomy.million_suffix")).append(" ").append(Text.translatable("text.ceconomy.currency_name"));
            }
        }

        return Text.literal(insertCommasLong(amount, false)).append(" ").append(Text.translatable("text.ceconomy.currency_name"));
    }

    public static String insertCommasLong(double amount, boolean showDecimals) {
        DecimalFormat formatter = new DecimalFormat(showDecimals ? "#,###.00" : "#,###");
        return formatter.format(amount);
    }

    public static void addCurrencyLore(List<Text> tooltip, String key, long currentCurrency) {
        addCurrencyLore(tooltip, key, currentCurrency, 0);
    }

    public static void addCurrencyLore(List<Text> tooltip, String key, long currentCurrency, long currencyCapacity) {

        MutableText amount = formatCurrency(currentCurrency, false);

        if (currencyCapacity > 0) {
            amount.append(" / ").append(formatCurrency(currencyCapacity, false));
        }

        tooltip.add(Text.translatable("text.ceconomy." + key).setStyle(Style.EMPTY.withColor(Formatting.GRAY)).append(": ").append(amount.setStyle(Style.EMPTY.withColor(Formatting.GOLD))));
    }

    public static long readFromNBT(NbtCompound nbt) {
        NbtCompound currencyCompound = nbt.getCompound("Currency");
        return currencyCompound.getLong("Amount");
    }

    public static void writeToNBT(NbtCompound nbt, long amount) {
        NbtCompound currencyCompound = new NbtCompound();
        currencyCompound.putLong("Amount", amount);
        nbt.put("Currency", currencyCompound);
    }

    public static long getAmountToAdd(long startingValue, long amountToAdd, long maxAmount) {
        return startingValue + amountToAdd > maxAmount ? 0 : amountToAdd;
    }

    public static long getAmountToFill(long startingValue, long amountToAdd, long maxAmount) {
        return startingValue + amountToAdd > maxAmount ? maxAmount - startingValue : 0;
    }
}
