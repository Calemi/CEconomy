package com.calemi.ceconomy.registry;

import com.calemi.ceconomy.config.CEconomyConfig;
import com.calemi.ceconomy.item.*;
import com.calemi.ceconomy.main.CEconomyMain;
import com.calemi.ceconomy.main.CEconomyRef;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ItemRegistry {

    public static final Item RARITANIUM = regItem("raritanium", new Item(new FabricItemSettings()));

    public static final Item COPPER_COIN = regItem("copper_coin", new CoinItem(BlockRegistry.COPPER_COIN_STACK));
    public static final Item SILVER_COIN = regItem("silver_coin", new CoinItem(BlockRegistry.SILVER_COIN_STACK));
    public static final Item GOLD_COIN = regItem("gold_coin", new CoinItem(BlockRegistry.GOLD_COIN_STACK));
    public static final Item PLATINUM_COIN = regItem("platinum_coin", new CoinItem(BlockRegistry.PLATINUM_COIN_STACK));
    public static final Item DIAMOND_COIN = regItem("diamond_coin", new CoinItem(BlockRegistry.DIAMOND_COIN_STACK));
    public static final Item NETHERITE_COIN = regItem("netherite_coin", new CoinItem(BlockRegistry.NETHERITE_COIN_STACK));

    public static final Item CHEAP_MONEY_BAG = regItem("cheap_money_bag", new MoneyBagItem(CEconomyConfig.COMMON.cheapMoneyBagMinAmount, CEconomyConfig.COMMON.cheapMoneyBagMaxAmount));
    public static final Item RICH_MONEY_BAG = regItem("rich_money_bag", new MoneyBagItem(CEconomyConfig.COMMON.richMoneyBagMinAmount, CEconomyConfig.COMMON.richMoneyBagMaxAmount));

    public static final Item WALLET = regItem("wallet", new WalletItem());
    public static final Item CHECK = regItem("check", new CheckItem());

    public static final Item BANK = regItem("bank", new CurrencyBlockItem(BlockRegistry.BANK, new FabricItemSettings(), CEconomyConfig.COMMON.bankCurrencyCapacity));

    private static Item regItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(CEconomyRef.MOD_ID, name), item);
    }

    public static void init() {
        CEconomyMain.LOGGER.info("Registering Items...");
    }
}