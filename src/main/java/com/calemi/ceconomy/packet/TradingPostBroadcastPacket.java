package com.calemi.ceconomy.packet;

import com.calemi.ccore.api.general.StringHelper;
import com.calemi.ceconomy.api.currency.CurrencyHelper;
import com.calemi.ceconomy.block.entity.TradingPostBlockEntity;
import com.calemi.ceconomy.config.CEconomyConfig;
import com.calemi.ceconomy.registry.PacketRegistry;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;

public class TradingPostBroadcastPacket {

    public static void send(BlockPos pos) {

        PacketByteBuf buf = PacketByteBufs.create();

        buf.writeBlockPos(pos);

        ClientPlayNetworking.send(PacketRegistry.TRADING_POST_BROADCAST, buf);
    }

    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {

        World world = player.getWorld();
        BlockPos pos = buf.readBlockPos();

        if (!CEconomyConfig.COMMON.tradingPostBroadcast) {
            return;
        }

        if (world.getWorldChunk(pos).getBlockEntity(pos, WorldChunk.CreationType.IMMEDIATE) instanceof TradingPostBlockEntity tradingPost) {

            if (tradingPost.getBroadcastDelayTicks() <= 0) {

                broadcast(server, tradingPost);
                tradingPost.setBroadcastDelayTicks(CEconomyConfig.COMMON.tradingPostBroadcastDelayTicks);
            }

            else {
                player.sendMessage(Text.translatable("text.ceconomy.broadcast_error", tradingPost.getBroadcastDelayTicks() / 20).formatted(Formatting.RED));
            }
        }
    }

    private static void broadcast(MinecraftServer server, TradingPostBlockEntity tradingPost) {

        MutableText ownerText = tradingPost.isAdminMode() ? Text.translatable("text.ceconomy.the_server") : Text.literal(tradingPost.getSecurityProfile().getOwnerName());

        MutableText isText = Text.translatable("text.ceconomy.is");

        MutableText buyingText = Text.translatable("text.ceconomy.buying");
        MutableText sellingText = Text.translatable("text.ceconomy.selling");

        MutableText tradeText = tradingPost.isBuyMode() ? buyingText : sellingText;

        MutableText amountText = Text.literal("x").append(StringHelper.insertCommas(tradingPost.getTradeAmount())).formatted(Formatting.GOLD);

        MutableText stackText = tradingPost.getTradeStack().getName().copy();

        MutableText forText = Text.translatable("text.ceconomy.for");

        MutableText priceText = CurrencyHelper.formatCurrency(tradingPost.getTradePrice(), true).formatted(Formatting.GOLD);

        server.getPlayerManager().broadcast(ownerText.append(" ").append(isText).append(" ").append(tradeText).append(" ").append(amountText).append(" ").append(stackText).append(" ").append(forText).append(" ").append(priceText), false);
    }
}
