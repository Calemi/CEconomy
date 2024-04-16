package com.calemi.ceconomy.packet;

import com.calemi.ceconomy.registry.PacketRegistry;
import com.calemi.ceconomy.screen.handler.EnderBankScreenHandler;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

public class EnderBankSyncPacket {

    public static void send(ServerPlayerEntity player, long amount) {

        PacketByteBuf buf = PacketByteBufs.create();

        buf.writeLong(amount);

        ServerPlayNetworking.send(player, PacketRegistry.ENDER_BANK_SYNC, buf);
    }

    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {

        long currency = buf.readLong();

        ClientPlayerEntity player = client.player;

        if (player.currentScreenHandler instanceof EnderBankScreenHandler enderBankScreenHandler) {
            enderBankScreenHandler.setCurrency(currency);
        }
    }
}
