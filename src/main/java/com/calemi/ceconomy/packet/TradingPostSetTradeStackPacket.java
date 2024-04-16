package com.calemi.ceconomy.packet;

import com.calemi.ceconomy.block.entity.TradingPostBlockEntity;
import com.calemi.ceconomy.registry.PacketRegistry;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;

public class TradingPostSetTradeStackPacket {

    public static void send(BlockPos pos, ItemStack tradeStack) {

        PacketByteBuf buf = PacketByteBufs.create();

        buf.writeBlockPos(pos);
        buf.writeItemStack(tradeStack);

        ClientPlayNetworking.send(PacketRegistry.TRADING_POST_SET_TRADE_STACK, buf);
    }

    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {

        World world = player.getWorld();

        BlockPos pos = buf.readBlockPos();
        ItemStack tradeStack = buf.readItemStack();

        if (world.getWorldChunk(pos).getBlockEntity(pos, WorldChunk.CreationType.IMMEDIATE) instanceof TradingPostBlockEntity tradingPost) {
            tradingPost.setTradeStack(tradeStack);
            tradingPost.markDirty();
        }
    }
}
