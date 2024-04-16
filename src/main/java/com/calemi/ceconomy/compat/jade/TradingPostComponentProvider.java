package com.calemi.ceconomy.compat.jade;

import com.calemi.ccore.api.general.StringHelper;
import com.calemi.ceconomy.api.currency.CurrencyHelper;
import com.calemi.ceconomy.block.entity.TradingPostBlockEntity;
import com.calemi.ceconomy.main.CEconomyRef;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElementHelper;

public enum TradingPostComponentProvider implements IBlockComponentProvider {

    INSTANCE;

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {

        if (blockAccessor.getBlockEntity() instanceof TradingPostBlockEntity tradingPostBlock) {

            MutableText buyingText = Text.translatable("text.ceconomy.buying_capital");
            MutableText sellingText = Text.translatable("text.ceconomy.selling_capital");

            MutableText tradeText = tradingPostBlock.isBuyMode() ? buyingText : sellingText;

            MutableText amountText = Text.literal("x").append(StringHelper.insertCommas(tradingPostBlock.getTradeAmount())).formatted(Formatting.GOLD);

            MutableText stackText = tradingPostBlock.getTradeStack().getName().copy();

            MutableText forText = Text.translatable("text.ceconomy.for_capital");

            MutableText priceText = CurrencyHelper.formatCurrency(tradingPostBlock.getTradePrice(), true).formatted(Formatting.GOLD);

            IElementHelper helper = IElementHelper.get();

            tooltip.add(tradeText.append(" ").append(amountText).append(" "));
            tooltip.append(helper.smallItem(tradingPostBlock.getTradeStack()));
            tooltip.append(stackText);
            tooltip.add(forText.append(" ").append(priceText));
            tooltip.add(Text.translatable("text.ceconomy.stock").append(":"));
        }
    }

    @Override
    public Identifier getUid() {
        return new Identifier(CEconomyRef.MOD_ID, "currency_block");
    }
}
