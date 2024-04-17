package com.calemi.ceconomy.screen;

import com.calemi.ccore.api.screen.BaseScreen;
import com.calemi.ccore.api.screen.ImageButtonWidget;
import com.calemi.ccore.api.screen.ScrollableLongField;
import com.calemi.ccore.api.sound.SoundHelper;
import com.calemi.ccore.api.sound.SoundProfile;
import com.calemi.ceconomy.api.currency.inventory.ItemCurrencyInventory;
import com.calemi.ceconomy.api.currency.CurrencyHelper;
import com.calemi.ceconomy.config.CEconomyConfig;
import com.calemi.ceconomy.item.CheckItem;
import com.calemi.ceconomy.item.WalletItem;
import com.calemi.ceconomy.main.CEconomyMain;
import com.calemi.ceconomy.main.CEconomyRef;
import com.calemi.ceconomy.packet.CheckWritePacket;
import com.calemi.ceconomy.screen.handler.CheckScreenHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class CheckScreen extends BaseScreen<CheckScreenHandler> {

    private final boolean inMainHand;
    private final ItemStack checkStack;

    private ScrollableLongField writeAmount;

    private ImageButtonWidget btnWrite;

    public CheckScreen(CheckScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);

        backgroundHeight = 151;
        playerInventoryTitleY = 59;

        inMainHand = handler.isMainHand();
        checkStack = handler.getCheckStack();
    }

    @Override
    protected void init() {
        super.init();

        writeAmount = new ScrollableLongField(0, 0, CEconomyConfig.COMMON.checkCurrencyCapacity, (backgroundWidth / 2), 32, drawSystem, () -> {});

        btnWrite = addDrawableChild(new ImageButtonWidget((button) -> writeCheck(),
                getScreenX() + 135, getScreenY() + 25, 57, 15, 19, CEconomyRef.GUI_TEXTURES));
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        super.drawForeground(context, mouseX, mouseY);

        if (checkStack.isEmpty()) {
            CEconomyMain.LOGGER.info("STACK EMPTY");
        }

        btnWrite.setTooltip(Tooltip.of(Text.translatable("text.ceconomy.write_check")));

        MutableText delta = Text.translatable("screen.scroll.change").append(": ").append(CurrencyHelper.formatCurrency(writeAmount.getDelta(), true).append(" ").append(Text.translatable("screen.scroll.shift")));
        MutableText current = Text.translatable("screen.scroll.current").append(": ").append(CurrencyHelper.formatCurrency(writeAmount.getValue(), true).setStyle(Style.EMPTY.withColor(Formatting.GOLD)));

        writeAmount.setText(CurrencyHelper.formatCurrency(writeAmount.getValue(), false));
        writeAmount.setTooltip(delta, current);
        writeAmount.render(mouseX, mouseY, context);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        writeAmount.mouseScrolled(mouseX, mouseY, amount);
        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    private void writeCheck() {

        long amount = writeAmount.getValue();
        ItemStack walletStack = CurrencyHelper.getCurrentWallet(MinecraftClient.getInstance().player);

        ItemCurrencyInventory checkCurrencyInv = ((CheckItem) checkStack.getItem()).getCurrencyInventory(checkStack);

        if (amount <= 0) {
            errorSystem.showError(Text.translatable("error.ceconomy.invalid_amount"));
            return;
        }

        if (walletStack.isEmpty()) {
            errorSystem.showError(Text.translatable("error.ceconomy.no_wallet"));
            return;
        }

        ItemCurrencyInventory walletCurrencyInv = ((WalletItem) walletStack.getItem()).getCurrencyInventory(walletStack);

        if (!CurrencyHelper.canTransferCurrency(walletCurrencyInv, checkCurrencyInv, amount)) {
            errorSystem.showError(Text.translatable("error.ceconomy.insufficient_funds"));
            return;
        }

        CheckWritePacket.send(inMainHand, amount);
        SoundHelper.playAtPlayer(MinecraftClient.getInstance().player, new SoundProfile(SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT));
        close();
    }

    @Override
    public Identifier getTexture() {
        return new Identifier(CEconomyRef.MOD_ID, "textures/gui/check.png");
    }
}