package com.calemi.ceconomy.datagen;

import com.calemi.ceconomy.registry.BlockRegistry;
import com.calemi.ceconomy.registry.ItemRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Block;
import net.minecraft.data.server.loottable.BlockLootTableGenerator;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LeafEntry;
import net.minecraft.loot.function.ApplyBonusLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;

public class BlockLootTableProvider extends FabricBlockLootTableProvider {

    protected BlockLootTableProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        addRaritaniumOreDrops(BlockRegistry.RARITANIUM_ORE);
        addRaritaniumOreDrops(BlockRegistry.DEEPSLATE_RARITANIUM_ORE);
        addCoinStackDrops(BlockRegistry.COPPER_COIN_STACK, ItemRegistry.COPPER_COIN);
        addCoinStackDrops(BlockRegistry.SILVER_COIN_STACK, ItemRegistry.SILVER_COIN);
        addCoinStackDrops(BlockRegistry.GOLD_COIN_STACK, ItemRegistry.GOLD_COIN);
        addCoinStackDrops(BlockRegistry.PLATINUM_COIN_STACK, ItemRegistry.PLATINUM_COIN);
        addCoinStackDrops(BlockRegistry.DIAMOND_COIN_STACK, ItemRegistry.DIAMOND_COIN);
        addCoinStackDrops(BlockRegistry.NETHERITE_COIN_STACK, ItemRegistry.NETHERITE_COIN);
        addDrop(BlockRegistry.CURRENCY_NETWORK_CABLE);
        addDrop(BlockRegistry.CURRENCY_NETWORK_CABLE_BLOCK);
        addDrop(BlockRegistry.CURRENCY_NETWORK_GATE);
        addDrop(BlockRegistry.ENDER_BANK);
        addDrop(BlockRegistry.TRADING_POST);
    }

    public void addRaritaniumOreDrops(Block block) {
        addDrop(block, BlockLootTableGenerator.dropsWithSilkTouch(block, this.applyExplosionDecay(block,
                ((LeafEntry.Builder<?>)
                        ItemEntry.builder(ItemRegistry.RARITANIUM)
                                .apply(SetCountLootFunction
                                        .builder(UniformLootNumberProvider
                                                .create(1.0F, 3.0F))))
                        .apply(ApplyBonusLootFunction.oreDrops(Enchantments.FORTUNE)))));
    }

    public void addCoinStackDrops(Block block, Item drop) {
        addDrop(block, BlockLootTableGenerator.dropsWithSilkTouch(block, this.applyExplosionDecay(block,
                ((LeafEntry.Builder<?>)ItemEntry.builder(drop).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(8.0F, 8.0F)))))));
    }
}
