package com.calemi.ceconomy.block.entity;

import com.calemi.ceconomy.block.entity.base.CurrencyNetworkBlockEntity;
import com.calemi.ceconomy.registry.BlockEntityTypeRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

public class CurrencyNetworkCableBlockEntity extends CurrencyNetworkBlockEntity {

    public CurrencyNetworkCableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public CurrencyNetworkCableBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityTypeRegistry.CURRENCY_NETWORK_CABLE, pos, state);
    }
}
