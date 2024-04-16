package com.calemi.ceconomy.compat.jade;

import com.calemi.ceconomy.api.currency.CurrencyHelper;
import com.calemi.ceconomy.api.currency.inventory.BlockCurrencyInventory;
import com.calemi.ceconomy.api.currency.inventory.ICurrencyInventoryBlock;
import com.calemi.ceconomy.main.CEconomyRef;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum CurrencyBlockComponentProvider implements IBlockComponentProvider {

    INSTANCE;

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {

        if (blockAccessor.getBlockEntity() instanceof ICurrencyInventoryBlock currencyBlock) {

            BlockCurrencyInventory blockCurrencyInventory = currencyBlock.getCurrencyInventory();

            MutableText currencyText = CurrencyHelper.formatCurrency(blockCurrencyInventory.getCurrency(),false).append(" / ").append(CurrencyHelper.formatCurrency(blockCurrencyInventory.getCurrencyCapacity(),false)).formatted(Formatting.GOLD);
            tooltip.add(Text.translatable("text.ceconomy.currency").append(": ").append(currencyText));
        }
    }

    @Override
    public Identifier getUid() {
        return new Identifier(CEconomyRef.MOD_ID, "currency_block");
    }
}
