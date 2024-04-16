package com.calemi.ceconomy.packet;

import com.calemi.ceconomy.api.security.SecurityHelper;
import com.calemi.ceconomy.block.entity.TradingPostBlockEntity;
import com.calemi.ceconomy.registry.PacketRegistry;
import com.calemi.ceconomy.screen.handler.EditTradeScreenHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;

public class TradingPostOpenEditTrade {

    public static void send(BlockPos pos) {

        PacketByteBuf buf = PacketByteBufs.create();

        buf.writeBlockPos(pos);

        ClientPlayNetworking.send(PacketRegistry.TRADING_POST_OPEN_EDIT_TRADE, buf);
    }

    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {

        World world = player.getWorld();

        BlockPos pos = buf.readBlockPos();
        BlockEntity blockEntity = world.getWorldChunk(pos).getBlockEntity(pos, WorldChunk.CreationType.IMMEDIATE);

        if (blockEntity instanceof TradingPostBlockEntity tradingPost) {

            if (SecurityHelper.canAccess(player, tradingPost)) {

                player.openHandledScreen(new ExtendedScreenHandlerFactory() {

                    @Override
                    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
                        buf.writeBlockPos(pos);
                    }

                    @Override
                    public Text getDisplayName() {
                        return Text.translatable("screen.ceconomy.edit_trade");
                    }

                    @Override
                    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
                        return new EditTradeScreenHandler(syncId, inv, tradingPost);
                    }
                });
            }
        }
    }
}
