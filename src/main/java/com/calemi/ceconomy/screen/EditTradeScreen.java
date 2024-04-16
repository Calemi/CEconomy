package com.calemi.ceconomy.screen;

import com.calemi.ccore.api.screen.*;
import com.calemi.ceconomy.api.currency.CurrencyHelper;
import com.calemi.ceconomy.api.screen.*;
import com.calemi.ceconomy.block.entity.TradingPostBlockEntity;
import com.calemi.ceconomy.config.CEconomyConfig;
import com.calemi.ceconomy.main.CEconomyRef;
import com.calemi.ceconomy.packet.*;
import com.calemi.ceconomy.screen.handler.EditTradeScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class EditTradeScreen extends BaseScreen<EditTradeScreenHandler> {

    private final TradingPostBlockEntity tradingPost;

    private FakeSlotWidget tradeStackSlot;

    private ToggleImageButtonWidget btnToggleBuyMode;
    private ImageButtonWidget btnBroadcast;

    private ScrollableLongField tradeAmount;
    private ScrollableLongField tradePrice;

    private ImageButtonWidget btnResetTradeAmount;
    private ImageButtonWidget btnResetTradePrice;

    public EditTradeScreen(EditTradeScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);

        backgroundHeight = 232;
        playerInventoryTitleY = 141;

        tradingPost = handler.getTradingPost();
    }

    @Override
    protected void init() {
        super.init();

        tradeStackSlot = addDrawableChild(new FakeSlotWidget(getScreenX() + 80, getScreenY() + 19, tradingPost.getTradeStack(), (button) -> {
            TradingPostSetTradeStackPacket.send(tradingPost.getPos(), tradeStackSlot.getItemStack());
        }));

        btnToggleBuyMode = addDrawableChild(new ToggleImageButtonWidget((button) -> toggleBuyMode(),
                getScreenX() + 123, getScreenY() + 17, 19, 53, 19, 19, !tradingPost.isBuyMode(), CEconomyRef.GUI_TEXTURES));

        if (CEconomyConfig.COMMON.tradingPostBroadcast) {
            btnBroadcast = addDrawableChild(new ImageButtonWidget((button) -> broadcast(),
                    getScreenX() + 145, getScreenY() + 17, 76, 53, 19, CEconomyRef.GUI_TEXTURES));
        }

        tradeAmount = new ScrollableLongField(tradingPost.getTradeAmount(), 1, 1728, (backgroundWidth / 2), 44, drawSystem, () -> {
            setTradeAmount((int)tradeAmount.getValue());
        });

        tradePrice = new ScrollableLongField(tradingPost.getTradePrice(), 0, CEconomyConfig.COMMON.walletCurrencyCapacity, (backgroundWidth / 2), 65, drawSystem, () -> {
            setTradePrice(tradePrice.getValue());
        });

        btnResetTradeAmount = addDrawableChild(new ImageButtonWidget((button) -> setTradeAmount(1),
            getScreenX() + 123, getScreenY() + 38, 0, 53, 19, CEconomyRef.GUI_TEXTURES));

        btnResetTradePrice = addDrawableChild(new ImageButtonWidget((button) -> setTradePrice(0),
                getScreenX() + 123, getScreenY() + 59, 0, 53, 19, CEconomyRef.GUI_TEXTURES));
    }

    private void toggleBuyMode() {
        TradingPostSetBuyModePacket.send(tradingPost.getPos(), !tradingPost.isBuyMode());
    }

    private void broadcast() {
        TradingPostBroadcastPacket.send(tradingPost.getPos());
    }

    private void setTradeAmount(int amount) {
        tradeAmount.setValue(amount);
        TradingPostSetTradeAmountPacket.send(tradingPost.getPos(), amount);
    }

    private void setTradePrice(long price) {
        tradePrice.setValue(price);
        TradingPostSetTradePricePacket.send(tradingPost.getPos(), price);
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        super.drawForeground(context, mouseX, mouseY);

        btnToggleBuyMode.setTooltip(btnToggleBuyMode.isToggled() ? Tooltip.of(Text.translatable("text.ceconomy.selling_capital")) : Tooltip.of(Text.translatable("text.ceconomy.buying_capital")));
        if (btnBroadcast != null) btnBroadcast.setTooltip(Tooltip.of(Text.translatable("text.ceconomy.broadcast")));

        btnResetTradeAmount.setTooltip(Tooltip.of(Text.translatable("text.ceconomy.reset_amount")));
        btnResetTradePrice.setTooltip(Tooltip.of(Text.translatable("text.ceconomy.reset_price")));

        tradeStackSlot.render(mouseX, mouseY, drawSystem, context);

        MutableText amountDelta = Text.translatable("screen.scroll.change").append(": ").append(CurrencyHelper.insertCommasLong(tradeAmount.getDelta(), false)).append(" ").append(Text.translatable("screen.scroll.shift"));
        tradeAmount.setText(Text.literal("" + tradeAmount.getValue()));
        tradeAmount.setTooltip(amountDelta);
        tradeAmount.render(mouseX, mouseY, context);

        MutableText priceDelta = Text.translatable("screen.scroll.change").append(": ").append(CurrencyHelper.formatCurrency(tradePrice.getDelta(), true).append(" ").append(Text.translatable("screen.scroll.shift")));
        MutableText priceCurrent = Text.translatable("screen.scroll.current").append(": ").append(CurrencyHelper.formatCurrency(tradePrice.getValue(), false).formatted(Formatting.GOLD));
        tradePrice.setText(CurrencyHelper.formatCurrency(tradePrice.getValue(), false));
        tradePrice.setTooltip(priceDelta, priceCurrent);
        tradePrice.render(mouseX, mouseY, context);

        Text amountText = Text.translatable("text.ceconomy.amount");
        context.drawText(textRenderer, amountText, 52 - textRenderer.getWidth(amountText), 44, 4210752, false);

        Text priceText = Text.translatable("text.ceconomy.price");
        context.drawText(textRenderer, priceText, 52 - textRenderer.getWidth(priceText), 65, 4210752, false);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        tradeAmount.mouseScrolled(mouseX, mouseY, amount);
        tradePrice.mouseScrolled(mouseX, mouseY, amount);
        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    @Override
    public Identifier getTexture() {
        return new Identifier(CEconomyRef.MOD_ID, "textures/gui/edit_trade.png");
    }
}