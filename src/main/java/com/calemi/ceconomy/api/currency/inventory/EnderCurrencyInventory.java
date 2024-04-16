package com.calemi.ceconomy.api.currency.inventory;

import com.calemi.ccore.api.IEntityDataSaver;
import com.calemi.ceconomy.config.CEconomyConfig;
import net.minecraft.entity.player.PlayerEntity;

public class EnderCurrencyInventory extends CurrencyInventory {

    private final IEntityDataSaver player;

    public EnderCurrencyInventory(PlayerEntity player) {
        super(CEconomyConfig.COMMON.enderBankCurrencyCapacity);
        this.player = (IEntityDataSaver) player;
    }

    @Override
    public long getCurrency() {
        return player.getPersistentData().getLong("Currency");
    }

    @Override
    public void setCurrency(long amount) {
        player.getPersistentData().putLong("Currency", amount);
    }
}
