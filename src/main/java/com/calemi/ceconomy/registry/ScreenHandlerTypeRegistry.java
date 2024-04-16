package com.calemi.ceconomy.registry;

import com.calemi.ceconomy.main.CEconomyMain;
import com.calemi.ceconomy.main.CEconomyRef;
import com.calemi.ceconomy.screen.handler.*;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ScreenHandlerTypeRegistry {

    public static final ScreenHandlerType<WalletScreenHandler> WALLET =
            Registry.register(Registries.SCREEN_HANDLER, new Identifier(CEconomyRef.MOD_ID, "wallet"), new ExtendedScreenHandlerType<>(WalletScreenHandler::new));

    public static final ScreenHandlerType<CheckScreenHandler> CHECK =
            Registry.register(Registries.SCREEN_HANDLER, new Identifier(CEconomyRef.MOD_ID, "check"), new ExtendedScreenHandlerType<>(CheckScreenHandler::new));

    public static final ScreenHandlerType<BankScreenHandler> BANK =
            Registry.register(Registries.SCREEN_HANDLER, new Identifier(CEconomyRef.MOD_ID, "bank"), new ExtendedScreenHandlerType<>(BankScreenHandler::new));

    public static final ScreenHandlerType<EnderBankScreenHandler> ENDER_BANK =
            Registry.register(Registries.SCREEN_HANDLER, new Identifier(CEconomyRef.MOD_ID, "ender_bank"), new ExtendedScreenHandlerType<>(EnderBankScreenHandler::new));

    public static final ScreenHandlerType<EditTradeScreenHandler> EDIT_TRADE =
            Registry.register(Registries.SCREEN_HANDLER, new Identifier(CEconomyRef.MOD_ID, "edit_trade"), new ExtendedScreenHandlerType<>(EditTradeScreenHandler::new));

    public static final ScreenHandlerType<BulkTradeScreenHandler> BULK_TRADE =
            Registry.register(Registries.SCREEN_HANDLER, new Identifier(CEconomyRef.MOD_ID, "bulk_trade"), new ExtendedScreenHandlerType<>(BulkTradeScreenHandler::new));

    public static void init() {
        CEconomyMain.LOGGER.info("Registering Screen Handler Types...");
    }
}
