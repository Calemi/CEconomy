package com.calemi.ceconomy.packet;

import com.calemi.ceconomy.api.currency.CurrencyHelper;
import com.calemi.ceconomy.api.currency.inventory.ItemCurrencyInventory;
import com.calemi.ceconomy.item.CheckItem;
import com.calemi.ceconomy.item.WalletItem;
import com.calemi.ceconomy.registry.PacketRegistry;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class CheckWritePacket {

    public static void send(boolean isMainHand, long currency) {

        PacketByteBuf buf = PacketByteBufs.create();

        buf.writeBoolean(isMainHand);
        buf.writeLong(currency);

        ClientPlayNetworking.send(PacketRegistry.CHECK_WRITE, buf);
    }

    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {

        World world = player.getWorld();

        boolean isMainHand= buf.readBoolean();
        long currency = buf.readLong();

        if (currency <= 0) {
            return;
        }

        ItemStack walletStack = CurrencyHelper.getCurrentWallet(player);
        ItemStack checkStack = player.getStackInHand(isMainHand ? Hand.MAIN_HAND : Hand.OFF_HAND);

        if (walletStack.getItem() instanceof WalletItem walletItem && checkStack.getItem() instanceof CheckItem checkItem) {

            ItemCurrencyInventory walletCurrencyInv = walletItem.getCurrencyInventory(walletStack);
            ItemCurrencyInventory checkCurrencyInv = checkItem.getCurrencyInventory(checkStack);

            if (CurrencyHelper.tryTransferCurrency(walletCurrencyInv, checkCurrencyInv, currency)) {
                CheckItem.setWritten(checkStack);
            }
        }
    }
}
