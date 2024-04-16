package com.calemi.ceconomy.packet;

import com.calemi.ceconomy.api.currency.CurrencyHelper;
import com.calemi.ceconomy.api.currency.inventory.ItemCurrencyInventory;
import com.calemi.ceconomy.block.entity.BankBlockEntity;
import com.calemi.ceconomy.item.WalletItem;
import com.calemi.ceconomy.main.CEconomyMain;
import com.calemi.ceconomy.registry.PacketRegistry;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.OverlayMessageS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;

public class BankWithdrawPacket {

    public static void send(BlockPos pos, long transactionAmount) {

        PacketByteBuf buf = PacketByteBufs.create();

        buf.writeBlockPos(pos);
        buf.writeLong(transactionAmount);

        ClientPlayNetworking.send(PacketRegistry.BANK_WITHDRAW, buf);
    }

    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {

        World world = player.getWorld();

        BlockPos pos = buf.readBlockPos();
        long transactionAmount = buf.readLong();

        if (world.getWorldChunk(pos).getBlockEntity(pos, WorldChunk.CreationType.IMMEDIATE) instanceof BankBlockEntity bank) {

            if (bank.getStack(BankBlockEntity.WALLET_SLOT).getItem() instanceof WalletItem walletItem) {

                ItemCurrencyInventory walletCurrencyInv = walletItem.getCurrencyInventory(bank.getStack(BankBlockEntity.WALLET_SLOT));
                CurrencyHelper.transferOrFillCurrency(bank.getCurrencyInventory(), walletCurrencyInv, transactionAmount);
            }

            bank.markDirty();
        }
    }
}
