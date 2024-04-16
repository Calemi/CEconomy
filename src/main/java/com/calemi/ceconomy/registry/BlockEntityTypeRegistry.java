package com.calemi.ceconomy.registry;

import com.calemi.ceconomy.block.entity.*;
import com.calemi.ceconomy.main.CEconomyMain;
import com.calemi.ceconomy.main.CEconomyRef;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class BlockEntityTypeRegistry {

    public static final BlockEntityType<CurrencyNetworkCableBlockEntity> CURRENCY_NETWORK_CABLE =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(CEconomyRef.MOD_ID, "currency_network_cable"),
                    FabricBlockEntityTypeBuilder.create(CurrencyNetworkCableBlockEntity::new, BlockRegistry.CURRENCY_NETWORK_CABLE, BlockRegistry.CURRENCY_NETWORK_CABLE_BLOCK).build());

    public static final BlockEntityType<CurrencyNetworkGateBlockEntity> CURRENCY_NETWORK_GATE =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(CEconomyRef.MOD_ID, "currency_network_gate"),
                    FabricBlockEntityTypeBuilder.create(CurrencyNetworkGateBlockEntity::new, BlockRegistry.CURRENCY_NETWORK_GATE).build());

    public static final BlockEntityType<BankBlockEntity> BANK =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(CEconomyRef.MOD_ID, "bank"),
                    FabricBlockEntityTypeBuilder.create(BankBlockEntity::new, BlockRegistry.BANK).build());

    public static final BlockEntityType<EnderBankBlockEntity> ENDER_BANK =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(CEconomyRef.MOD_ID, "ender_bank"),
                    FabricBlockEntityTypeBuilder.create(EnderBankBlockEntity::new, BlockRegistry.ENDER_BANK).build());

    public static final BlockEntityType<TradingPostBlockEntity> TRADING_POST =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(CEconomyRef.MOD_ID, "trading_post"),
                    FabricBlockEntityTypeBuilder.create(TradingPostBlockEntity::new, BlockRegistry.TRADING_POST).build());

    public static void init() {
        CEconomyMain.LOGGER.info("Registering Block Entity Types...");
    }
}
