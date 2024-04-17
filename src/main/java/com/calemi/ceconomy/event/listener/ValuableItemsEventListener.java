package com.calemi.ceconomy.event.listener;

import com.calemi.ceconomy.main.CEconomyMain;
import com.calemi.ceconomy.packet.ValuableItemsPacket;
import com.calemi.ceconomy.registry.ValuableItemReloadListener;
import dev.architectury.event.events.common.PlayerEvent;

public class ValuableItemsEventListener {

    public static void init() {

        PlayerEvent.PLAYER_JOIN.register((player) -> {

            CEconomyMain.LOGGER.info("Player joined! Re-syncing Valuable Items to everyone...");
            ValuableItemsPacket.send(player, ValuableItemReloadListener.getValuableItems());
        });
    }
}
