package com.calemi.ceconomy.main;

import com.calemi.ceconomy.block.entity.renderer.TradingPostBlockEntityRenderer;
import com.calemi.ceconomy.event.listener.CurrencyEventListener;
import com.calemi.ceconomy.event.listener.WalletOverlayEventListener;
import com.calemi.ceconomy.registry.BlockEntityTypeRegistry;
import com.calemi.ceconomy.registry.PacketRegistry;
import com.calemi.ceconomy.registry.ScreenHandlerTypeRegistry;
import com.calemi.ceconomy.screen.*;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class CEconomyClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {

		CEconomyMain.LOGGER.info("Initializing Client...");

		HandledScreens.register(ScreenHandlerTypeRegistry.WALLET, WalletScreen::new);
		HandledScreens.register(ScreenHandlerTypeRegistry.CHECK, CheckScreen::new);
		HandledScreens.register(ScreenHandlerTypeRegistry.BANK, BankScreen::new);
		HandledScreens.register(ScreenHandlerTypeRegistry.ENDER_BANK, EnderBankScreen::new);
		HandledScreens.register(ScreenHandlerTypeRegistry.EDIT_TRADE, EditTradeScreen::new);
		HandledScreens.register(ScreenHandlerTypeRegistry.BULK_TRADE, BulkTradeScreen::new);

		BlockEntityRendererFactories.register(BlockEntityTypeRegistry.TRADING_POST, TradingPostBlockEntityRenderer::new);

		PacketRegistry.initClient();
		CurrencyEventListener.initClient();
		WalletOverlayEventListener.initClient();
	}
}