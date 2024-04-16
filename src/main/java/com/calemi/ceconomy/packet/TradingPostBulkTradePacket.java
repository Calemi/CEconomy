package com.calemi.ceconomy.packet;

import com.calemi.ccore.api.location.Location;
import com.calemi.ceconomy.api.general.TradeHelper;
import com.calemi.ceconomy.block.entity.TradingPostBlockEntity;
import com.calemi.ceconomy.registry.PacketRegistry;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TradingPostBulkTradePacket {

    public static void send(BlockPos pos, int sets) {

        PacketByteBuf buf = PacketByteBufs.create();

        buf.writeBlockPos(pos);
        buf.writeInt(sets);

        ClientPlayNetworking.send(PacketRegistry.TRADING_POST_BULK_TRADE, buf);
    }

    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {

        World world = player.getWorld();

        BlockPos pos = buf.readBlockPos();
        int sets = buf.readInt();

        if (new Location(world, pos).getBlockEntity() instanceof TradingPostBlockEntity tradingPost) {
            TradeHelper.handleTrade(tradingPost, sets * tradingPost.getTradePrice(), sets * tradingPost.getTradeAmount(), world, player);
        }
    }
}
