package com.calemi.ceconomy.api.general;

import com.calemi.ccore.api.inventory.InventoryHelper;
import com.calemi.ccore.api.message.OverlayMessageHelper;
import com.calemi.ccore.api.sound.SoundHelper;
import com.calemi.ccore.api.sound.SoundProfile;
import com.calemi.ceconomy.api.currency.CurrencyHelper;
import com.calemi.ceconomy.api.currency.inventory.ICurrencyInventoryBlock;
import com.calemi.ceconomy.api.currency.inventory.ItemCurrencyInventory;
import com.calemi.ceconomy.api.currency.network.CurrencyNetworkHelper;
import com.calemi.ceconomy.block.entity.TradingPostBlockEntity;
import com.calemi.ceconomy.item.WalletItem;
import com.calemi.ceconomy.packet.TradingPostSendErrorMessage;
import com.calemi.ceconomy.registry.SoundEventRegistry;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;

public class TradeHelper {

    /**
     * Handles the trading system. Decides if the trade is valid and if it is a sell or a purchase.
     */
    public static void handleTrade(TradingPostBlockEntity tradingPost, long tradePrice, int tradeAmount, World world, PlayerEntity player) {

        ItemStack walletStack = CurrencyHelper.getCurrentWallet(player);

        if (!(walletStack.getItem() instanceof WalletItem walletItem)) {
            sendErrorMsg("error.ceconomy.trading_post.no_wallet", player);
            return;
        }

        ItemCurrencyInventory walletCurrencyInventory = walletItem.getCurrencyInventory(walletStack);

        //Generates the base ItemStack to sell.
        ItemStack tradeStack = tradingPost.getTradeStack();
        tradeStack.setCount(1);

        //Checks if the trade is set up properly
        if (!tradingPost.isTradeValid()) {
            sendErrorMsg("error.ceconomy.trading_post.invalid_trade", player);
            return;
        }

        //If the Trading Post is in buy mode, handle a sell.
        if (tradingPost.isBuyMode()) {
            handleSell(tradingPost, tradeStack, tradePrice, tradeAmount, player, walletCurrencyInventory);
        }

        //If not, handle a purchase.
        else {
            handlePurchase(tradingPost, tradeStack, tradePrice, tradeAmount, player, walletCurrencyInventory);
        }
    }


    /**
     * Handles the selling system.
     * PLAYER -> ITEM -> TRADING POST
     */
    public static void handleSell(TradingPostBlockEntity tradingPost, ItemStack sellStack, long sellPrice, int sellAmount, PlayerEntity player, ItemCurrencyInventory walletCurrencyInventory) {

        //Checks if the Player has the required amount of items.
        if (InventoryHelper.countItems(player.getInventory(), sellStack) < sellAmount) {
            sendErrorMsg("error.ceconomy.trading_post.no_items_player", player);
            return;
        }

        //Checks if the Trading Post can fit the amount of items being sold to it.
        if (!InventoryHelper.canInsertItem(tradingPost, sellStack, sellAmount) && !tradingPost.isAdminMode()) {
            sendErrorMsg("error.ceconomy.trading_post.cant_fit_items_trading_post", player);
            return;
        }

        //Checks if the Player's current Wallet can fit added funds.
        if (!walletCurrencyInventory.canDepositCurrency(sellPrice)) {
            sendErrorMsg("error.ceconomy.trading_post.cant_fit_funds_player", player);
            return;
        }

        if (player.getWorld().isClient()) {
            return;
        }

        ICurrencyInventoryBlock bank = CurrencyNetworkHelper.getConnectedBankWithMostCurrency(tradingPost.getLocation());

        //Checks if the connected Bank has enough funds to spend. Bypasses this check if in admin mode or if it's free.
        if (sellPrice > 0 && !tradingPost.isAdminMode()) {

            if (bank == null || !bank.getCurrencyInventory().canWithdrawCurrency(sellPrice)) {
                sendErrorMsg("error.ceconomy.trading_post.no_funds_trading_post", player);
                return;
            }
        }

        //Checks if not in admin mode.
        if (!tradingPost.isAdminMode()) {

            //Adds Items to the Trading Post
            InventoryHelper.insertItem(tradingPost, sellStack, sellAmount);

            //Subtracts funds from the connected Bank.
            if (bank != null) bank.getCurrencyInventory().withdrawCurrency(sellPrice);
        }

        //Removes Items from the Player.
        InventoryHelper.consumeItems(player.getInventory(), sellStack, sellAmount);

        //Adds funds to the Player's current wallet.
        walletCurrencyInventory.depositCurrency(sellPrice);

        SoundHelper.playAtPlayer(player, new SoundProfile(SoundEventRegistry.COIN).setVolume(0.1F));

        tradingPost.markDirty();
        if (bank != null) ((BlockEntity)bank).markDirty();
    }

    /**
     * Handles the purchasing system.
     * TRADING POST -> ITEM -> PLAYER
     */
    public static void handlePurchase(TradingPostBlockEntity tradingPost, ItemStack buyStack, long buyPrice, int buyAmount, PlayerEntity player, ItemCurrencyInventory walletCurrencyInventory) {

        //Checks if the Trading Post has enough stock. Bypasses this check if in admin mode.
        if (InventoryHelper.countItems(tradingPost, buyStack) < buyAmount && !tradingPost.isAdminMode()) {
            sendErrorMsg("error.ceconomy.trading_post.no_items_trading_post", player);
            return;
        }

        //Checks if the Player can fit the amount of items being sold to it.
        if (!InventoryHelper.canInsertItem(player.getInventory(), buyStack, buyAmount)) {
            sendErrorMsg("error.ceconomy.trading_post.cant_fit_items_player", player);
            return;
        }

        //Checks if the Player has enough funds in his current Wallet.
        if (!walletCurrencyInventory.canWithdrawCurrency(buyPrice)) {
            sendErrorMsg("error.ceconomy.trading_post.no_funds_player", player);
            return;
        }

        if (player.getWorld().isClient()) {
            return;
        }

        ICurrencyInventoryBlock bank = CurrencyNetworkHelper.getConnectedBankWithLeastCurrency(tradingPost.getLocation());

        //Checks if the connected Bank can store the possible funds.
        if (buyPrice > 0 && !tradingPost.isAdminMode()) {

            if (bank == null || !bank.getCurrencyInventory().canDepositCurrency(buyPrice)) {
                sendErrorMsg("error.ceconomy.trading_post.cant_fit_funds_trading_post", player);
                return;
            }
        }

        //Adds funds to the connected Bank.
        if (!tradingPost.isAdminMode()) {

            //Removes Items from the Trading Post.
            InventoryHelper.consumeItems(tradingPost, buyStack, buyAmount);

            //Adds funds from the connected Bank.
            if (bank != null) bank.getCurrencyInventory().depositCurrency(buyPrice);
        }

        //Subtracts funds from the Player's current Wallet.
        walletCurrencyInventory.withdrawCurrency(buyPrice);

        //Inserts the stack into the Player's inventory.
        InventoryHelper.insertItem(player.getInventory(), buyStack, buyAmount);

        SoundHelper.playAtPlayer(player, new SoundProfile(SoundEventRegistry.COIN).setVolume(0.1F));

        tradingPost.markDirty();
        if (bank != null) ((BlockEntity)bank).markDirty();
    }

    private static void sendErrorMsg(String message, PlayerEntity player) {

        if (!player.getWorld().isClient()) {
            OverlayMessageHelper.displayErrorMsg(Text.translatable(message), player);
            TradingPostSendErrorMessage.send((ServerPlayerEntity) player, message);
        }
    }
}
