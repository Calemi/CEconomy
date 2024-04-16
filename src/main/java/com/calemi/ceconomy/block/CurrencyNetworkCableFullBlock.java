package com.calemi.ceconomy.block;

import com.calemi.ceconomy.block.entity.CurrencyNetworkCableBlockEntity;
import com.calemi.ceconomy.registry.BlockRegistry;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class CurrencyNetworkCableFullBlock extends BlockWithEntity {

    public CurrencyNetworkCableFullBlock() {
        super(BlockRegistry.CURRENCY_NETWORK_BLOCK_SETTINGS);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CurrencyNetworkCableBlockEntity(pos, state);
    }
}
