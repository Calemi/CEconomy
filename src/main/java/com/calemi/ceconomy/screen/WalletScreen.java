package com.calemi.ceconomy.screen;

import com.calemi.ccore.api.general.CCoreMathHelper;
import com.calemi.ccore.api.general.StringHelper;
import com.calemi.ccore.api.inventory.InventoryHelper;
import com.calemi.ccore.api.screen.BaseScreen;
import com.calemi.ccore.api.screen.ImageButtonWidget;
import com.calemi.ccore.api.screen.ScreenRect;
import com.calemi.ceconomy.api.currency.inventory.ItemCurrencyInventory;
import com.calemi.ceconomy.api.item.ValuableItem;
import com.calemi.ceconomy.api.item.ValuableItemHelper;
import com.calemi.ceconomy.api.screen.DrawSystemHelper;
import com.calemi.ceconomy.item.WalletItem;
import com.calemi.ceconomy.main.CEconomyMain;
import com.calemi.ceconomy.main.CEconomyRef;
import com.calemi.ceconomy.packet.WalletDepositItemPacket;
import com.calemi.ceconomy.screen.handler.WalletScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

public class WalletScreen extends BaseScreen<WalletScreenHandler> {

    private final ItemStack walletStack;
    private ItemCurrencyInventory walletCurrencyInv;

    private final ArrayList<ValuableItem> outputItemList;
    private int outputItemIndex = 0;

    private ImageButtonWidget btnDeposit;

    public WalletScreen(WalletScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);

        backgroundHeight = 159;
        playerInventoryTitleY = 66;

        walletStack = handler.getWalletStack();
        outputItemList = ValuableItemHelper.getWalletOutputItemList();

        if (walletStack.getItem() instanceof WalletItem walletItem) {
            walletCurrencyInv = walletItem.getCurrencyInventory(walletStack);
        }
    }

    @Override
    protected void init() {
        super.init();

        btnDeposit = addDrawableChild(new ImageButtonWidget((button) -> {

            ValuableItem itemValue = ValuableItemHelper.getItemValue(getOutputStack().getItem());

            if (!walletCurrencyInv.tryWithdrawCurrency(itemValue.getValue() * getOutputAmount())) {
                errorSystem.showError(Text.translatable("error.ceconomy.insufficient_funds"));
            }

            if (!InventoryHelper.canInsertItem(getPlayer().getInventory(), getOutputStack(), getOutputAmount())) {
                errorSystem.showError(Text.translatable("error.ceconomy.cant_fit_items"));
            }

            else {
                WalletDepositItemPacket.send(outputItemIndex, getOutputAmount());
            }

        }, getScreenX() + 145, getScreenY() + 34, 0, 15, 19, CEconomyRef.GUI_TEXTURES));
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        super.drawForeground(context, mouseX, mouseY);

        if (outputItemList.isEmpty()) {
            return;
        }

        drawSystem.drawItemWithTooltip(getOutputStack(), 127, 35, mouseX, mouseY, context);

        if (walletStack.getItem() instanceof WalletItem wallet) {
            DrawSystemHelper.drawCenteredCurrencyText(walletCurrencyInv.getCurrency(), walletCurrencyInv.getCurrencyCapacity(), 72, 39, mouseX, mouseY, drawSystem, context);
        }

        drawSystem.drawHoveringTooltip(Text.translatable("text.ceconomy.scroll_to_cycle"), true, new ScreenRect(126, 16, 18, 18), mouseX, mouseY, context);
        drawSystem.drawHoveringTooltip(Text.translatable("text.ceconomy.scroll_to_cycle"), true, new ScreenRect(126, 52, 18, 18), mouseX, mouseY, context);
        btnDeposit.setTooltip(Tooltip.of(Text.translatable("text.ceconomy.withdraw_item").append(Text.literal("\nx" + StringHelper.insertCommas(getOutputAmount())).setStyle(Style.EMPTY.withColor(Formatting.GOLD)))));
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {

        ScreenRect depositItemScrollRect = new ScreenRect(getScreenX() + 126, getScreenY() + 16, 18, 54);

        if (depositItemScrollRect.contains((int)mouseX, (int)mouseY)) {

            int depositItemListSize = outputItemList.size();

            outputItemIndex += (int)amount;

            if (outputItemIndex < 0) {
                outputItemIndex = depositItemListSize - 1;
            }

            else if (outputItemIndex >= depositItemListSize) {
                outputItemIndex = 0;
            }
        }

        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    private ItemStack getOutputStack() {
        return outputItemList.get(outputItemIndex).getItem().getDefaultStack();
    }

    private int getOutputAmount() {
        return CCoreMathHelper.getShiftCtrlInt(1, 16, 64, 9 * 64);
    }

    @Override
    public Identifier getTexture() {
        return new Identifier(CEconomyRef.MOD_ID, "textures/gui/wallet.png");
    }
}