package com.calemi.ceconomy.api.screen;

import com.calemi.ccore.api.screen.DrawSystem;
import com.calemi.ccore.api.screen.ScreenRect;
import com.calemi.ceconomy.api.currency.CurrencyHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;

public class DrawSystemHelper {

    public static void drawCenteredCurrencyText(long currency, long capacityCurrency, int x, int y, int mouseX, int mouseY, DrawSystem drawSystem, DrawContext context) {

        MutableText text = CurrencyHelper.formatCurrency(currency, false);
        int textWidth = drawSystem.getTextRenderer().getWidth(text);

        drawSystem.drawCenteredText(text, false, x, y, context);

        MutableText tooltipText = CurrencyHelper.formatCurrency(currency, true).formatted(Formatting.GOLD);

        drawSystem.drawHoveringTooltip(tooltipText, true, new ScreenRect(x - (textWidth / 2), y, textWidth, 8), mouseX, mouseY, context);
    }
}
