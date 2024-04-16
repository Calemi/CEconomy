package com.calemi.ceconomy.packet;

import com.calemi.ceconomy.registry.PacketRegistry;
import com.calemi.ceconomy.screen.BulkTradeScreen;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class TradingPostSendErrorMessage {

    public static void send(ServerPlayerEntity player, String message) {

        PacketByteBuf buf = PacketByteBufs.create();

        buf.writeString(message, 100);

        ServerPlayNetworking.send(player, PacketRegistry.TRADING_POST_SEND_ERROR_MSG, buf);
    }

    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {

        String message = buf.readString(100);

        if (MinecraftClient.getInstance().currentScreen instanceof BulkTradeScreen bulkTradeScreen) {
            bulkTradeScreen.errorSystem.showError(Text.translatable(message));
        }
    }
}
