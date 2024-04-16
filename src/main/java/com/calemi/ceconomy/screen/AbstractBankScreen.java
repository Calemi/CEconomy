package com.calemi.ceconomy.screen;

import com.calemi.ccore.api.screen.BaseScreen;
import com.calemi.ccore.api.screen.ImageButtonWidget;
import com.calemi.ccore.api.screen.ScrollableLongField;
import com.calemi.ccore.api.screen.handler.BaseScreenHandler;
import com.calemi.ceconomy.api.currency.CurrencyHelper;
import com.calemi.ceconomy.api.currency.inventory.ItemCurrencyInventory;
import com.calemi.ceconomy.api.screen.DrawSystemHelper;
import com.calemi.ceconomy.item.WalletItem;
import com.calemi.ceconomy.main.CEconomyRef;
import net.minecraft.block.Block;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public abstract class AbstractBankScreen<T extends BaseScreenHandler> extends BaseScreen<T> {

    private ScrollableLongField transactionAmount;

    private ImageButtonWidget btnDeposit;
    private ImageButtonWidget btnWithdraw;

    public AbstractBankScreen(T handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);

        backgroundHeight = 171;
        playerInventoryTitleY = 78;
    }

    public abstract long getCurrency();
    public abstract long getCurrencyCapacity();

    public abstract void deposit(long amount);
    public abstract void withdraw(long amount);

    public abstract ItemStack getWalletStack();
    public abstract Block getBankToRender();

    @Override
    protected void init() {
        super.init();

        transactionAmount = new ScrollableLongField(1, 1, getCurrencyCapacity(), (backgroundWidth / 2), 42, drawSystem, () -> {});

        btnDeposit = addDrawableChild(new ImageButtonWidget((button) -> deposit(transactionAmount.getValue()),
                getScreenX() + 39, getScreenY() + 36, 38, 15, 19, CEconomyRef.GUI_TEXTURES));

        btnWithdraw = addDrawableChild(new ImageButtonWidget((button) -> withdraw(transactionAmount.getValue()),
                getScreenX() + 118, getScreenY() + 36, 19, 15, 19, CEconomyRef.GUI_TEXTURES));
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        super.drawForeground(context, mouseX, mouseY);

        btnDeposit.setTooltip(Tooltip.of(Text.translatable("text.ceconomy.deposit")));
        btnWithdraw.setTooltip(Tooltip.of(Text.translatable("text.ceconomy.withdraw")));

        MutableText delta = Text.translatable("screen.scroll.change").append(": ").append(CurrencyHelper.formatCurrency(transactionAmount.getDelta(), true).append(" ").append(Text.translatable("screen.scroll.shift")));
        MutableText current = Text.translatable("screen.scroll.current").append(": ").append(CurrencyHelper.formatCurrency(transactionAmount.getValue(), true).setStyle(Style.EMPTY.withColor(Formatting.GOLD)));
        transactionAmount.setText(CurrencyHelper.formatCurrency(transactionAmount.getValue(), false));
        transactionAmount.setTooltip(delta, current);
        transactionAmount.render(mouseX, mouseY, context);

        DrawSystemHelper.drawCenteredCurrencyText(getCurrency(), getCurrencyCapacity(), backgroundWidth / 2, 61, mouseX, mouseY, drawSystem, context);

        if (!getWalletStack().isEmpty()) {

            if (getWalletStack().getItem() instanceof WalletItem walletItem) {

                ItemCurrencyInventory walletCurrencyInv = walletItem.getCurrencyInventory(getWalletStack());

                DrawSystemHelper.drawCenteredCurrencyText(walletCurrencyInv.getCurrency(), walletCurrencyInv.getCurrencyCapacity(), backgroundWidth / 2, 24, mouseX, mouseY, drawSystem, context);
            }
        }

        drawSystem.drawItemWithTooltip(new ItemStack(getBankToRender()), 134, 56, mouseX, mouseY, context);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        transactionAmount.mouseScrolled(mouseX, mouseY, amount);
        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    @Override
    public Identifier getTexture() {
        return new Identifier(CEconomyRef.MOD_ID, "textures/gui/bank.png");
    }
}