package com.calemi.ceconomy.main;

import com.calemi.ceconomy.config.CEconomyConfig;
import com.calemi.ceconomy.event.listener.CurrencyEventListener;
import com.calemi.ceconomy.event.listener.SecurityEventListener;
import com.calemi.ceconomy.event.listener.ValuableItemsEventListener;
import com.calemi.ceconomy.registry.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.ResourceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CEconomyMain implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger(CEconomyRef.MOD_NAME);

	@Override
	public void onInitialize() {

		LOGGER.info("Initializing Main...");

		CEconomyConfig.init();

		BlockRegistry.init();
		ItemRegistry.init();
		ItemGroupRegistry.init();
		BlockEntityTypeRegistry.init();
		SoundEventRegistry.init();
		OreRegistry.init();
		ScreenHandlerTypeRegistry.init();
		PacketRegistry.init();
		SecurityEventListener.init();
		CurrencyEventListener.init();
		ValuableItemsEventListener.init();

		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new ValuableItemReloadListener());
	}

	public static boolean ftbTeamsLoaded() {
		return FabricLoader.getInstance().isModLoaded(CEconomyRef.FTB_TEAMS_ID);
	}

	public static boolean trinketsLoaded() {
		return FabricLoader.getInstance().isModLoaded(CEconomyRef.TRINKETS_ID);
	}
}