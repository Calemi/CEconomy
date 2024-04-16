package com.calemi.ceconomy.datagen;

import com.calemi.ceconomy.registry.BlockRegistry;
import com.calemi.ceconomy.registry.ItemRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.*;

public class ModelProvider extends FabricModelProvider {

    public ModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator generator) {
        generator.registerSimpleCubeAll(BlockRegistry.RARITANIUM_ORE);
        generator.registerSimpleCubeAll(BlockRegistry.DEEPSLATE_RARITANIUM_ORE);
        generator.registerSimpleCubeAll(BlockRegistry.CURRENCY_NETWORK_CABLE_BLOCK);
        generator.registerSingleton(BlockRegistry.BANK, TexturedModel.CUBE_COLUMN);
        generator.registerSingleton(BlockRegistry.ENDER_BANK, TexturedModel.CUBE_COLUMN);
    }

    @Override
    public void generateItemModels(ItemModelGenerator generator) {
        generator.register(ItemRegistry.RARITANIUM, Models.GENERATED);
        generator.register(ItemRegistry.COPPER_COIN, Models.GENERATED);
        generator.register(ItemRegistry.SILVER_COIN, Models.GENERATED);
        generator.register(ItemRegistry.GOLD_COIN, Models.GENERATED);
        generator.register(ItemRegistry.PLATINUM_COIN, Models.GENERATED);
        generator.register(ItemRegistry.DIAMOND_COIN, Models.GENERATED);
        generator.register(ItemRegistry.NETHERITE_COIN, Models.GENERATED);
        generator.register(ItemRegistry.CHEAP_MONEY_BAG, Models.GENERATED);
        generator.register(ItemRegistry.RICH_MONEY_BAG, Models.GENERATED);
        generator.register(ItemRegistry.WALLET, Models.GENERATED);
        generator.register(ItemRegistry.CHECK, Models.GENERATED);
    }
}
