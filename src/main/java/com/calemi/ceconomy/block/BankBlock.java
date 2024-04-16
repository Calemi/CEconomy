package com.calemi.ceconomy.block;

import com.calemi.ccore.api.message.OverlayMessageHelper;
import com.calemi.ceconomy.api.currency.inventory.BlockCurrencyInventory;
import com.calemi.ceconomy.api.security.SecurityHelper;
import com.calemi.ceconomy.block.entity.BankBlockEntity;
import com.calemi.ceconomy.registry.BlockEntityTypeRegistry;
import com.calemi.ceconomy.registry.BlockRegistry;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BankBlock extends BlockWithEntity {

    public BankBlock() {
        super(BlockRegistry.CURRENCY_NETWORK_BLOCK_SETTINGS);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {

        if (!world.isClient) {

            BlockEntity blockEntity = world.getBlockEntity(pos);

            if (blockEntity instanceof BankBlockEntity bank) {

                if (SecurityHelper.canAccess(player, bank)) {

                    player.openHandledScreen(bank);
                }

                else OverlayMessageHelper.displayErrorMsg(Text.translatable("error.ceconomy.security.cant_access"), player);
            }
        }

        return ActionResult.SUCCESS;
    }

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
        return new BankBlockEntity(pos, state);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(world, type, BlockEntityTypeRegistry.BANK);
    }

    @Nullable
    protected static <T extends BlockEntity> BlockEntityTicker<T> checkType(World world, BlockEntityType<T> givenType, BlockEntityType<? extends BankBlockEntity> expectedType) {
        return world.isClient ? null : checkType(givenType, expectedType, BankBlockEntity::tick);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {

        if (world.getBlockEntity(pos) instanceof BankBlockEntity bank) {
            BlockCurrencyInventory currencyInventory = bank.getCurrencyInventory();
            return (int)(((double)currencyInventory.getCurrency() / (double)currencyInventory.getCurrencyCapacity()) * 15);
        }

        return 0;
    }
}
