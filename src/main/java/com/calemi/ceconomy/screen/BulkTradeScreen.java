package com.calemi.ceconomy.screen;

import com.calemi.ccore.api.screen.BaseScreen;
import com.calemi.ccore.api.screen.ImageButtonWidget;
import com.calemi.ccore.api.screen.ScrollableLongField;
import com.calemi.ceconomy.api.currency.CurrencyHelper;
import com.calemi.ceconomy.api.screen.DrawSystemHelper;
import com.calemi.ceconomy.block.entity.TradingPostBlockEntity;
import com.calemi.ceconomy.main.CEconomyRef;
import com.calemi.ceconomy.packet.TradingPostBulkTradePacket;
import com.calemi.ceconomy.packet.TradingPostOpenEditTrade;
import com.calemi.ceconomy.screen.handler.BulkTradeScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class BulkTradeScreen extends BaseScreen<BulkTradeScreenHandler> {

    private final TradingPostBlockEntity tradingPost;

    private ImageButtonWidget btnEditTrade;

    private ScrollableLongField sets;
    private ImageButtonWidget btnResetSets;

    public BulkTradeScreen(BulkTradeScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);

        backgroundHeight = 197;
        playerInventoryTitleY = 104;

        tradingPost = handler.getTradingPost();
    }

    @Override
    protected void init() {
        super.init();

        if (getScreenHandler().canAccessEdit()) {

            btnEditTrade = addDrawableChild(new ImageButtonWidget((button) -> {
                TradingPostOpenEditTrade.send(tradingPost.getPos());
            }, getScreenX() + 151, getScreenY() + 6, 57, 53, 19, CEconomyRef.GUI_TEXTURES));
        }

        sets = new ScrollableLongField(1, 1, 1000, (backgroundWidth / 2), 66, drawSystem, () -> {});

        btnResetSets = addDrawableChild(new ImageButtonWidget((button) -> sets.setValue(1),
                getScreenX() + 123, getScreenY() + 60, 0, 53, 19, CEconomyRef.GUI_TEXTURES));

        addDrawableChild(ButtonWidget.builder(tradingPost.isBuyMode() ? Text.translatable("text.ceconomy.sell") : Text.translatable("text.ceconomy.buy"), (button) -> {

            TradingPostBulkTradePacket.send(tradingPost.getPos(), (int)sets.getValue());

        }).dimensions(getScreenX() + 57, getScreenY() + 83, 62, 16).build());
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        super.drawForeground(context, mouseX, mouseY);

        if (btnEditTrade != null) {
            btnEditTrade.setTooltip(Tooltip.of(Text.translatable("text.ceconomy.edit_trade")));
        }

        drawSystem.drawItemWithTooltip(tradingPost.getTradeStack(), 80, 19, mouseX, mouseY, context);

        btnResetSets.setTooltip(Tooltip.of(Text.translatable("text.ceconomy.reset_sets")));

        Text amountText = Text.translatable("text.ceconomy.amount");
        context.drawText(textRenderer, amountText, 52 - textRenderer.getWidth(amountText), 40, 4210752, false);

        Text priceText = Text.translatable("text.ceconomy.price");
        context.drawText(textRenderer, priceText, 52 - textRenderer.getWidth(priceText), 50, 4210752, false);

        drawSystem.drawCenteredText(Text.literal("x" + (tradingPost.getTradeAmount() * sets.getValue())), false, backgroundWidth / 2, 40, context);

        DrawSystemHelper.drawCenteredCurrencyText(tradingPost.getTradePrice() * sets.getValue(), 0, backgroundWidth / 2, 50, mouseX, mouseY, drawSystem, context);

        MutableText amountDelta = Text.translatable("screen.scroll.change").append(": ").append(CurrencyHelper.insertCommasLong(sets.getDelta(), false)).append(" ").append(Text.translatable("screen.scroll.shift"));
        sets.setText(Text.literal("" + sets.getValue()));
        sets.setTooltip(amountDelta);
        sets.render(mouseX, mouseY, context);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        sets.mouseScrolled(mouseX, mouseY, amount);
        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    @Override
    public Identifier getTexture() {
        return new Identifier(CEconomyRef.MOD_ID, "textures/gui/bulk_trade.png");
    }
}