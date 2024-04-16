package com.calemi.ceconomy.packet;

import com.calemi.ccore.api.inventory.InventoryHelper;
import com.calemi.ceconomy.api.currency.CurrencyHelper;
import com.calemi.ceconomy.api.currency.inventory.ItemCurrencyInventory;
import com.calemi.ceconomy.api.item.ValuableItem;
import com.calemi.ceconomy.api.item.ValuableItemHelper;
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
import net.minecraft.world.World;

public class WalletDepositItemPacket {

    public static void send(ItemStack outputStack, int count) {

        PacketByteBuf buf = PacketByteBufs.create();

        buf.writeItemStack(outputStack);
        buf.writeInt(count);

        ClientPlayNetworking.send(PacketRegistry.WALLET_DEPOSIT_ITEM, buf);
    }

    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {

        World world = player.getWorld();

        ItemStack stack = buf.readItemStack();
        int count = buf.readInt();

        stack.setCount(count);

        ValuableItem itemValue = ValuableItemHelper.getItemValue(stack.getItem());

        if (itemValue != null && itemValue.getValue() > 0) {

            ItemStack walletStack = CurrencyHelper.getCurrentWallet(player);

            if (walletStack.getItem() instanceof WalletItem walletItem) {

                ItemCurrencyInventory walletCurrencyInv = walletItem.getCurrencyInventory(walletStack);

                if (walletCurrencyInv.canWithdrawCurrency(itemValue.getValue() * count)) {

                    if (InventoryHelper.canInsertItem(player.getInventory(), stack, count)) {

                        InventoryHelper.insertItem(player.getInventory(), stack, count);
                        walletCurrencyInv.withdrawCurrency(itemValue.getValue() * count);
                    }
                }
            }
        }
    }
}
