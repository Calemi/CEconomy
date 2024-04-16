package com.calemi.ceconomy.block;

import com.calemi.ceconomy.block.entity.CurrencyNetworkGateBlockEntity;
import com.calemi.ceconomy.registry.BlockEntityTypeRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class CurrencyNetworkGateBlock extends CurrencyNetworkCableFullBlock {

    public static final BooleanProperty CONNECTED = BooleanProperty.of("connected");

    public CurrencyNetworkGateBlock() {

        setDefaultState(stateManager.getDefaultState()
                .with(CONNECTED, false));
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CurrencyNetworkGateBlockEntity(pos, state);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(world, type, BlockEntityTypeRegistry.CURRENCY_NETWORK_GATE);
    }

    @Nullable
    protected static <T extends BlockEntity> BlockEntityTicker<T> checkType(World world, BlockEntityType<T> givenType, BlockEntityType<? extends CurrencyNetworkGateBlockEntity> expectedType) {
        return world.isClient ? null : checkType(givenType, expectedType, CurrencyNetworkGateBlockEntity::tick);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState().with(CONNECTED, true);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(CONNECTED);
    }
}
