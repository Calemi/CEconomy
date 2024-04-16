package com.calemi.ceconomy.registry;

import com.calemi.ceconomy.packet.*;
import com.calemi.ceconomy.main.CEconomyRef;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

public class PacketRegistry {

    public static final Identifier WALLET_DEPOSIT_ITEM = new Identifier(CEconomyRef.MOD_ID, "wallet_deposit_item");

    public static final Identifier CHECK_WRITE = new Identifier(CEconomyRef.MOD_ID, "check_write");

    public static final Identifier BANK_DEPOSIT = new Identifier(CEconomyRef.MOD_ID, "bank_deposit");
    public static final Identifier BANK_WITHDRAW = new Identifier(CEconomyRef.MOD_ID, "bank_withdraw");

    public static final Identifier ENDER_BANK_DEPOSIT = new Identifier(CEconomyRef.MOD_ID, "ender_bank_deposit");
    public static final Identifier ENDER_BANK_WITHDRAW = new Identifier(CEconomyRef.MOD_ID, "ender_bank_withdraw");
    public static final Identifier ENDER_BANK_SYNC = new Identifier(CEconomyRef.MOD_ID, "ender_sync_deposit");

    public static final Identifier TRADING_POST_SET_TRADE_STACK = new Identifier(CEconomyRef.MOD_ID, "trading_post_set_trade_stack");
    public static final Identifier TRADING_POST_SET_TRADE_PRICE = new Identifier(CEconomyRef.MOD_ID, "trading_post_set_trade_price");
    public static final Identifier TRADING_POST_SET_TRADE_AMOUNT = new Identifier(CEconomyRef.MOD_ID, "trading_post_set_trade_amount");
    public static final Identifier TRADING_POST_SET_BUY_MODE = new Identifier(CEconomyRef.MOD_ID, "trading_post_set_buy_mode");

    public static final Identifier TRADING_POST_OPEN_EDIT_TRADE = new Identifier(CEconomyRef.MOD_ID, "trading_post_open_edit_trade");
    public static final Identifier TRADING_POST_BULK_TRADE = new Identifier(CEconomyRef.MOD_ID, "trading_post_bulk_trade");
    public static final Identifier TRADING_POST_SEND_ERROR_MSG = new Identifier(CEconomyRef.MOD_ID, "trading_post_send_error_msg");
    public static final Identifier TRADING_POST_BROADCAST = new Identifier(CEconomyRef.MOD_ID, "trading_post_broadcast");

    public static void init() {
        ServerPlayNetworking.registerGlobalReceiver(WALLET_DEPOSIT_ITEM, WalletDepositItemPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(CHECK_WRITE, CheckWritePacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(BANK_DEPOSIT, BankDepositPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(BANK_WITHDRAW, BankWithdrawPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(ENDER_BANK_DEPOSIT, EnderBankDepositPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(ENDER_BANK_WITHDRAW, EnderBankWithdrawPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(TRADING_POST_SET_TRADE_STACK, TradingPostSetTradeStackPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(TRADING_POST_SET_TRADE_PRICE, TradingPostSetTradePricePacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(TRADING_POST_SET_TRADE_AMOUNT, TradingPostSetTradeAmountPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(TRADING_POST_SET_BUY_MODE, TradingPostSetBuyModePacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(TRADING_POST_OPEN_EDIT_TRADE, TradingPostOpenEditTrade::receive);
        ServerPlayNetworking.registerGlobalReceiver(TRADING_POST_BULK_TRADE, TradingPostBulkTradePacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(TRADING_POST_BROADCAST, TradingPostBroadcastPacket::receive);
    }

    public static void initClient() {
        ClientPlayNetworking.registerGlobalReceiver(ENDER_BANK_SYNC, EnderBankSyncPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(TRADING_POST_SEND_ERROR_MSG, TradingPostSendErrorMessage::receive);
    }
}
