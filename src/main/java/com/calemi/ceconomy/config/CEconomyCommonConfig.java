package com.calemi.ceconomy.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = "common")
public class CEconomyCommonConfig implements ConfigData {

    public boolean securitySystem = true;
    public boolean teammateSecurityAccess = true;
    public boolean allySecurityAccess = true;

    public int maxCurrencyNetworkSize = 2048;

    public long walletCurrencyCapacity = 9000000000000000L;
    public long checkCurrencyCapacity = 9000000000000000L;

    public long bankCurrencyCapacity = 9000000000000000L;
    public long enderBankCurrencyCapacity = 9000000000000000L;

    public int tradingPostBroadcastDelayTicks = 20 * 10;
    public boolean tradingPostBroadcast = true;

    public int cheapMoneyBagMinAmount = 10;
    public int cheapMoneyBagMaxAmount = 100;

    public int richMoneyBagMinAmount = 100;
    public int richMoneyBagMaxAmount = 300;
}

