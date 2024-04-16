package com.calemi.ceconomy.packet;

import com.calemi.ceconomy.api.currency.CurrencyHelper;
import com.calemi.ceconomy.api.currency.inventory.EnderCurrencyInventory;
import com.calemi.ceconomy.api.currency.inventory.ItemCurrencyInventory;
import com.calemi.ceconomy.item.WalletItem;
import com.calemi.ceconomy.registry.PacketRegistry;
import com.calemi.ceconomy.screen.handler.EnderBankScreenHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

public class EnderBankWithdrawPacket {

    public static void send(long transactionAmount) {

        PacketByteBuf buf = PacketByteBufs.create();

        buf.writeLong(transactionAmount);

        ClientPlayNetworking.send(PacketRegistry.ENDER_BANK_WITHDRAW, buf);
    }

    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {

        World world = player.getWorld();

        long transactionAmount = buf.readLong();

        EnderCurrencyInventory enderCurrencyInv = new EnderCurrencyInventory(player);

        if (player.currentScreenHandler instanceof EnderBankScreenHandler bankScreenHandler) {

            ItemStack walletStack = bankScreenHandler.getBankInv().getStack(0);

            if (walletStack.getItem() instanceof WalletItem walletItem) {

                ItemCurrencyInventory walletCurrencyInv = walletItem.getCurrencyInventory(walletStack);
                CurrencyHelper.transferOrFillCurrency(enderCurrencyInv, walletCurrencyInv, transactionAmount);

                EnderBankSyncPacket.send(player, enderCurrencyInv.getCurrency());
            }
        }
    }
}
