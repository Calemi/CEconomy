package com.calemi.ceconomy.registry;

import com.calemi.ceconomy.block.*;
import com.calemi.ceconomy.main.CEconomyMain;
import com.calemi.ceconomy.main.CEconomyRef;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class BlockRegistry {

    public static final FabricBlockSettings CURRENCY_NETWORK_BLOCK_SETTINGS = FabricBlockSettings.create().mapColor(MapColor.GRAY).requiresTool().strength(2F, 1F).sounds(BlockSoundGroup.METAL);

    public static final Block RARITANIUM_ORE = regBlock("raritanium_ore", new RaritaniumOreBlock(3.0F, BlockSoundGroup.STONE, MapColor.STONE_GRAY));
    public static final Block DEEPSLATE_RARITANIUM_ORE = regBlock("deepslate_raritanium_ore", new RaritaniumOreBlock(4.5F, BlockSoundGroup.DEEPSLATE, MapColor.DEEPSLATE_GRAY));

    public static final Block COPPER_COIN_STACK = regBlockSolo("copper_coin_stack", new CoinStackBlock());
    public static final Block SILVER_COIN_STACK = regBlockSolo("silver_coin_stack", new CoinStackBlock());
    public static final Block GOLD_COIN_STACK = regBlockSolo("gold_coin_stack", new CoinStackBlock());
    public static final Block PLATINUM_COIN_STACK = regBlockSolo("platinum_coin_stack", new CoinStackBlock());
    public static final Block DIAMOND_COIN_STACK = regBlockSolo("diamond_coin_stack", new CoinStackBlock());
    public static final Block NETHERITE_COIN_STACK = regBlockSolo("netherite_coin_stack", new CoinStackBlock());

    public static final Block CURRENCY_NETWORK_CABLE = regBlock("currency_network_cable", new CurrencyNetworkCableBlock());
    public static final Block CURRENCY_NETWORK_CABLE_BLOCK = regBlock("currency_network_cable_block", new CurrencyNetworkCableFullBlock());

    public static final Block CURRENCY_NETWORK_GATE = regBlock("currency_network_gate", new CurrencyNetworkGateBlock());

    public static final Block BANK = regBlockSolo("bank", new BankBlock());
    public static final Block ENDER_BANK = regBlock("ender_bank", new EnderBankBlock());

    public static final Block TRADING_POST = regBlock("trading_post", new TradingPostBlock());

    private static Block regBlockSolo(String name, Block block) {
        return Registry.register(Registries.BLOCK, new Identifier(CEconomyRef.MOD_ID, name), block);
    }

    private static Block regBlock(String name, Block block) {
        Registry.register(Registries.ITEM, new Identifier(CEconomyRef.MOD_ID, name), new BlockItem(block, new FabricItemSettings()));
        return Registry.register(Registries.BLOCK, new Identifier(CEconomyRef.MOD_ID, name), block);
    }

    public static void init() {
        CEconomyMain.LOGGER.info("Registering Blocks...");
    }
}
