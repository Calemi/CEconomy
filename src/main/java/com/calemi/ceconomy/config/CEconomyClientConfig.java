package com.calemi.ceconomy.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = "client")
public class CEconomyClientConfig implements ConfigData {

    public boolean walletOverlay = true;
    public WalletOverlayCorner walletOverlayCorner = WalletOverlayCorner.TOP_LEFT;

    public enum WalletOverlayCorner {
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT
    }
}

