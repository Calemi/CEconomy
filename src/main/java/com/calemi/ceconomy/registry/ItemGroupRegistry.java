package com.calemi.ceconomy.registry;

import com.calemi.ceconomy.config.CEconomyConfig;
import com.calemi.ceconomy.item.WalletItem;
import com.calemi.ceconomy.main.CEconomyMain;
import com.calemi.ceconomy.main.CEconomyRef;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ItemGroupRegistry {

    private static final ItemStack FULL_WALLET;

    static {
        FULL_WALLET = new ItemStack(ItemRegistry.WALLET);
        ((WalletItem)FULL_WALLET.getItem()).getCurrencyInventory(FULL_WALLET).depositCurrency(CEconomyConfig.COMMON.walletCurrencyCapacity);
    }

    public static final ItemGroup CECONOMY = Registry.register(Registries.ITEM_GROUP, new Identifier(CEconomyRef.MOD_ID, "ceconomy"),
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.ceconomy")).icon(() -> new ItemStack(ItemRegistry.GOLD_COIN)).entries((displayContext, entries) -> {
                entries.add(BlockRegistry.RARITANIUM_ORE);
                entries.add(BlockRegistry.DEEPSLATE_RARITANIUM_ORE);
                entries.add(ItemRegistry.RARITANIUM);
                entries.add(ItemRegistry.COPPER_COIN);
                entries.add(ItemRegistry.SILVER_COIN);
                entries.add(ItemRegistry.GOLD_COIN);
                entries.add(ItemRegistry.PLATINUM_COIN);
                entries.add(ItemRegistry.DIAMOND_COIN);
                entries.add(ItemRegistry.NETHERITE_COIN);
                entries.add(ItemRegistry.CHEAP_MONEY_BAG);
                entries.add(ItemRegistry.RICH_MONEY_BAG);
                entries.add(ItemRegistry.WALLET);
                entries.add(FULL_WALLET);
                entries.add(ItemRegistry.CHECK);
                entries.add(BlockRegistry.CURRENCY_NETWORK_CABLE);
                entries.add(BlockRegistry.CURRENCY_NETWORK_CABLE_BLOCK);
                entries.add(BlockRegistry.CURRENCY_NETWORK_GATE);
                entries.add(BlockRegistry.BANK);
                entries.add(BlockRegistry.ENDER_BANK);
                entries.add(BlockRegistry.TRADING_POST);
    }).build());

    private static void ingredientTab(FabricItemGroupEntries entries) {
        entries.addAfter(Items.DIAMOND, ItemRegistry.RARITANIUM);
    }

    private static void naturalTab(FabricItemGroupEntries entries) {
        entries.addAfter(Items.DEEPSLATE_DIAMOND_ORE, BlockRegistry.RARITANIUM_ORE);
        entries.addAfter(BlockRegistry.RARITANIUM_ORE, BlockRegistry.DEEPSLATE_RARITANIUM_ORE);
    }

    private static void toolsTab(FabricItemGroupEntries entries) {
        entries.addAfter(Items.WRITABLE_BOOK, ItemRegistry.WALLET);
        entries.addAfter(ItemRegistry.WALLET, FULL_WALLET);
        entries.addAfter(FULL_WALLET, ItemRegistry.CHECK);
    }

    private static void functionalTab(FabricItemGroupEntries entries) {
        entries.add(BlockRegistry.BANK);
        entries.add(BlockRegistry.ENDER_BANK);
        entries.add(BlockRegistry.TRADING_POST);
    }

    public static void init() {
        CEconomyMain.LOGGER.info("Registering Item Groups...");

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(ItemGroupRegistry::ingredientTab);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(ItemGroupRegistry::naturalTab);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(ItemGroupRegistry::toolsTab);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(ItemGroupRegistry::functionalTab);
    }
}
