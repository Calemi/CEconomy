package com.calemi.ceconomy.block;

import com.calemi.ceconomy.api.currency.network.ICurrencyNetwork;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CurrencyNetworkCableBlock extends CurrencyNetworkCableFullBlock {

    private static final BooleanProperty UP = BooleanProperty.of("up");
    private static final BooleanProperty DOWN = BooleanProperty.of("down");
    private static final BooleanProperty NORTH = BooleanProperty.of("north");
    private static final BooleanProperty EAST = BooleanProperty.of("east");
    private static final BooleanProperty SOUTH = BooleanProperty.of("south");
    private static final BooleanProperty WEST = BooleanProperty.of("west");
    private static final BooleanProperty DOWNUP = BooleanProperty.of("downup");
    private static final BooleanProperty NORTHSOUTH = BooleanProperty.of("northsouth");
    private static final BooleanProperty EASTWEST = BooleanProperty.of("eastwest");

    private static final VoxelShape CORE_AABB = Block.createCuboidShape(5, 5, 5, 11, 11, 11);
    private static final VoxelShape DOWN_AABB = Block.createCuboidShape(6, 0, 6, 10, 10, 10);
    private static final VoxelShape UP_AABB = Block.createCuboidShape(6, 6, 6, 10, 16, 10);
    private static final VoxelShape NORTH_AABB = Block.createCuboidShape(6, 6, 0, 10, 10, 5);
    private static final VoxelShape EAST_AABB = Block.createCuboidShape(6, 6, 6, 16, 10, 10);
    private static final VoxelShape SOUTH_AABB = Block.createCuboidShape(6, 6, 6, 10, 10, 16);
    private static final VoxelShape WEST_AABB = Block.createCuboidShape(0, 6, 6, 10, 10, 10);
    private static final VoxelShape DOWNUP_AABB = Block.createCuboidShape(6, 0, 6, 10, 16, 10);
    private static final VoxelShape NORTHSOUTH_AABB = Block.createCuboidShape(6, 6, 0, 10, 10, 16);
    private static final VoxelShape EASTWEST_AABB = Block.createCuboidShape(0, 6, 6, 16, 10, 10);

    public CurrencyNetworkCableBlock() {

        setDefaultState(stateManager.getDefaultState()
                .with(UP, false)
                .with(DOWN, false)
                .with(NORTH, false)
                .with(EAST, false)
                .with(SOUTH, false)
                .with(WEST, false)
                .with(DOWNUP, false)
                .with(NORTHSOUTH, false)
                .with(EASTWEST, false));
    }

    private boolean canCableConnectTo (WorldAccess level, BlockPos pos, Direction facing) {
        BlockPos otherPos = pos.offset(facing);
        return canBeConnectedTo(level, otherPos, facing.getOpposite());
    }

    private boolean canBeConnectedTo(WorldAccess level, BlockPos pos, Direction facing) {

        BlockEntity blockEntity = level.getBlockEntity(pos);

        if (blockEntity instanceof ICurrencyNetwork currencyNetwork) {

            for (Direction dir : currencyNetwork.getConnectedDirections()) {

                if (facing == dir) {
                    return true;
                }
            }
        }

        return false;
    }

    private BlockState getState (WorldAccess world, BlockPos pos) {

        boolean down = canCableConnectTo(world, pos, Direction.DOWN);
        boolean up = canCableConnectTo(world, pos, Direction.UP);
        boolean north = canCableConnectTo(world, pos, Direction.NORTH);
        boolean east = canCableConnectTo(world, pos, Direction.EAST);
        boolean south = canCableConnectTo(world, pos, Direction.SOUTH);
        boolean west = canCableConnectTo(world, pos, Direction.WEST);

        boolean downup = down && up && (!north && !east && !south && !west);
        boolean northsouth = north && south && (!down && !up && !east && !west);
        boolean eastwest = east && west && (!north && !south && !down && !up);

        if (downup || northsouth || eastwest) {
            down = false;
            up = false;
            north = false;
            east = false;
            south = false;
            west = false;
        }

        return stateManager.getDefaultState()
                .with(DOWN, down)
                .with(UP, up)
                .with(NORTH, north)
                .with(EAST, east)
                .with(SOUTH, south)
                .with(WEST, west)
                .with(DOWNUP, downup)
                .with(NORTHSOUTH, northsouth)
                .with(EASTWEST, eastwest);
    }

    private VoxelShape getCollision (BlockState state) {

        List<VoxelShape> collidingBoxes = new ArrayList<>();

        if (state.get(DOWN)) collidingBoxes.add(DOWN_AABB);
        if (state.get(UP)) collidingBoxes.add(UP_AABB);
        if (state.get(NORTH)) collidingBoxes.add(NORTH_AABB);
        if (state.get(EAST)) collidingBoxes.add(EAST_AABB);
        if (state.get(SOUTH)) collidingBoxes.add(SOUTH_AABB);
        if (state.get(WEST)) collidingBoxes.add(WEST_AABB);

        if (state.get(DOWNUP)) collidingBoxes.add(DOWNUP_AABB);
        if (state.get(NORTHSOUTH)) collidingBoxes.add(NORTHSOUTH_AABB);
        if (state.get(EASTWEST)) collidingBoxes.add(EASTWEST_AABB);

        if (!state.get(DOWNUP) && !state.get(NORTHSOUTH) && !state.get(EASTWEST)) collidingBoxes.add(CORE_AABB);

        VoxelShape[] shapes = new VoxelShape[collidingBoxes.size()];

        for (int i = 0; i < shapes.length; i++) {
            shapes[i] = collidingBoxes.get(i);
        }

        return VoxelShapes.union(Block.createCuboidShape(0, 0, 0, 0, 0, 0), shapes);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return getCollision(state);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getState(ctx.getWorld(), ctx.getBlockPos());
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return getState(world, pos);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(UP, DOWN, NORTH, SOUTH, EAST, WEST, DOWNUP, NORTHSOUTH, EASTWEST);
    }

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }
}
