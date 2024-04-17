package com.calemi.ceconomy.packet;

import com.calemi.ceconomy.api.item.ValuableItem;
import com.calemi.ceconomy.registry.PacketRegistry;
import com.calemi.ceconomy.registry.ValuableItemReloadListener;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;

public class ValuableItemsPacket {

    public static void send(ServerPlayerEntity player, ArrayList<ValuableItem> valuableItems) {

        PacketByteBuf buf = PacketByteBufs.create();

        buf.writeInt(valuableItems.size());

        for (ValuableItem valuableItem : valuableItems) {

            buf.writeString(valuableItem.getItemKey(), 100);
            buf.writeLong(valuableItem.getValue());
            buf.writeBoolean(valuableItem.canWalletDeposit());
        }

        ServerPlayNetworking.send(player, PacketRegistry.VALUABLE_ITEMS, buf);
    }

    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {

        ArrayList<ValuableItem> valuableItems = new ArrayList<>();

        int listSize = buf.readInt();

        for (int i = 0; i < listSize; i++) {

            String itemKey = buf.readString(100);
            long value = buf.readLong();
            boolean walletDeposit = buf.readBoolean();

            valuableItems.add(new ValuableItem(itemKey, value, walletDeposit));
        }

        ValuableItemReloadListener.setValuableItems(valuableItems);
    }
}
