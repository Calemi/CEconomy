package com.calemi.ceconomy.block;

import com.calemi.ccore.api.location.Location;
import com.calemi.ceconomy.api.general.TradeHelper;
import com.calemi.ceconomy.api.security.ISecuredBlock;
import com.calemi.ceconomy.api.security.SecurityHelper;
import com.calemi.ceconomy.block.entity.TradingPostBlockEntity;
import com.calemi.ceconomy.registry.BlockEntityTypeRegistry;
import com.calemi.ceconomy.registry.BlockRegistry;
import com.calemi.ceconomy.screen.handler.BulkTradeScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class TradingPostBlock extends BlockWithEntity {

    private static final VoxelShape SHAPE =  Block.createCuboidShape(0.01F, 0.01F, 0.01F, 15.99F, 5, 15.99F);

    public TradingPostBlock() {
        super(BlockRegistry.CURRENCY_NETWORK_BLOCK_SETTINGS);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {

        if (hand == Hand.OFF_HAND) {
            return ActionResult.PASS;
        }

        Location location = new Location(world, pos);

        if (location.getBlockEntity() instanceof TradingPostBlockEntity tradingPost) {

            if (player.isSneaking()) {
                openBulkTradeScreen(player, pos);
            }

            else {
                TradeHelper.handleTrade(tradingPost, tradingPost.getTradePrice(), tradingPost.getTradeAmount(), world, player);
            }
        }

        return ActionResult.SUCCESS;
    }

    private void openBulkTradeScreen(PlayerEntity player, BlockPos pos) {

        if (!player.getWorld().isClient) {

            if (player.getWorld().getBlockEntity(pos) instanceof ISecuredBlock securedBlock) {

                boolean canAccessEdit = SecurityHelper.canAccess(player, securedBlock);

                player.openHandledScreen(new ExtendedScreenHandlerFactory() {

                    @Override
                    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
                        buf.writeBlockPos(pos);
                        buf.writeBoolean(canAccessEdit);
                    }

                    @Override
                    public Text getDisplayName() {
                        return Text.translatable("screen.ceconomy.bulk_trade");
                    }

                    @Override
                    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
                        return new BulkTradeScreenHandler(syncId, inv, player.getWorld().getBlockEntity(pos), canAccessEdit);
                    }
                });
            }
        }
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);

        if (placer instanceof PlayerEntity player) {

            if (player.isCreative() && !player.isSneaking()) {

                if (world.getBlockEntity(pos) instanceof TradingPostBlockEntity tradingPost) {

                    tradingPost.setAdminMode(true);
                    if (!world.isClient()) player.sendMessage(Text.translatable("text.ceconomy.admin_mode").formatted(Formatting.YELLOW));
                }
            }
        }
    }

    /*
        BASE BLOCK METHODS
     */

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {

        if (!state.isOf(newState.getBlock())) {

            BlockEntity blockEntity = world.getBlockEntity(pos);

            if (blockEntity instanceof Inventory inventory) {

                if (world instanceof ServerWorld) {
                    ItemScatterer.spawn(world, pos, inventory);
                }
            }

            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new TradingPostBlockEntity(pos, state);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(world, type, BlockEntityTypeRegistry.TRADING_POST);
    }

    @Nullable
    protected static <T extends BlockEntity> BlockEntityTicker<T> checkType(World world, BlockEntityType<T> givenType, BlockEntityType<? extends TradingPostBlockEntity> expectedType) {
        return world.isClient ? null : checkType(givenType, expectedType, TradingPostBlockEntity::tick);
    }

    @Override
    public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
        return false;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return direction == Direction.DOWN && !state.canPlaceAt(world, pos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }

    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {

        if (world.getBlockEntity(pos) instanceof TradingPostBlockEntity tradingPost) {
            return ScreenHandler.calculateComparatorOutput((Inventory) tradingPost);
        }

        return 0;
    }
}
