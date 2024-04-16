package com.calemi.ceconomy.event.listener;

import com.calemi.ceconomy.api.currency.CurrencyHelper;
import com.calemi.ceconomy.api.currency.inventory.ICurrencyInventoryItem;
import com.calemi.ceconomy.api.currency.inventory.ItemCurrencyInventory;
import com.calemi.ceconomy.config.CEconomyClientConfig;
import com.calemi.ceconomy.config.CEconomyConfig;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class WalletOverlayEventListener {

    public static void initClient() {

        HudRenderCallback.EVENT.register((matrixStack, delta) -> {

            MinecraftClient mc = MinecraftClient.getInstance();

            if (!CEconomyConfig.CLIENT.walletOverlay || mc.currentScreen != null || mc.options.debugEnabled) {
                return;
            }

            ItemStack stack = CurrencyHelper.getCurrentWallet(mc.player);

            if (stack.getItem() instanceof ICurrencyInventoryItem currencyItem) {

                ItemCurrencyInventory currencyInventory = currencyItem.getCurrencyInventory(stack);
                Text currencyText = CurrencyHelper.formatCurrency(currencyInventory.getCurrency(), true);

                int x = 5;
                int y = 3;

                int textOffsetX = 20;
                int textOffsetY = 4;

                if (CEconomyConfig.CLIENT.walletOverlayCorner == CEconomyClientConfig.WalletOverlayCorner.TOP_RIGHT || CEconomyConfig.CLIENT.walletOverlayCorner == CEconomyClientConfig.WalletOverlayCorner.BOTTOM_RIGHT) {
                    x = mc.getWindow().getScaledWidth() - 23;
                    textOffsetX = -mc.textRenderer.getWidth(currencyText) - 3;
                }

                if (CEconomyConfig.CLIENT.walletOverlayCorner == CEconomyClientConfig.WalletOverlayCorner.BOTTOM_LEFT || CEconomyConfig.CLIENT.walletOverlayCorner == CEconomyClientConfig.WalletOverlayCorner.BOTTOM_RIGHT) {
                    y = mc.getWindow().getScaledHeight() - 20;
                }

                matrixStack.drawItem(stack, x, y);
                matrixStack.drawText(mc.textRenderer, currencyText, x + textOffsetX, y + textOffsetY, Formatting.WHITE.getColorValue(), true);
            }
        });
    }
}
