package com.calemi.ceconomy.datagen;

import com.calemi.ceconomy.registry.BlockRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;

import java.util.concurrent.CompletableFuture;

public class BlockTagProvider extends FabricTagProvider.BlockTagProvider {

    public BlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
                .add(BlockRegistry.RARITANIUM_ORE)
                .add(BlockRegistry.DEEPSLATE_RARITANIUM_ORE)
                .add(BlockRegistry.COPPER_COIN_STACK)
                .add(BlockRegistry.SILVER_COIN_STACK)
                .add(BlockRegistry.GOLD_COIN_STACK)
                .add(BlockRegistry.PLATINUM_COIN_STACK)
                .add(BlockRegistry.DIAMOND_COIN_STACK)
                .add(BlockRegistry.NETHERITE_COIN_STACK)
                .add(BlockRegistry.CURRENCY_NETWORK_CABLE)
                .add(BlockRegistry.CURRENCY_NETWORK_CABLE_BLOCK)
                .add(BlockRegistry.CURRENCY_NETWORK_GATE)
                .add(BlockRegistry.BANK)
                .add(BlockRegistry.ENDER_BANK)
                .add(BlockRegistry.TRADING_POST);
        getOrCreateTagBuilder(BlockTags.NEEDS_IRON_TOOL)
                .add(BlockRegistry.RARITANIUM_ORE)
                .add(BlockRegistry.DEEPSLATE_RARITANIUM_ORE)
                .add(BlockRegistry.CURRENCY_NETWORK_CABLE)
                .add(BlockRegistry.CURRENCY_NETWORK_CABLE_BLOCK)
                .add(BlockRegistry.CURRENCY_NETWORK_GATE)
                .add(BlockRegistry.BANK)
                .add(BlockRegistry.ENDER_BANK)
                .add(BlockRegistry.TRADING_POST);
    }
}
