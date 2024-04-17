package com.calemi.ceconomy.block;

import com.calemi.ceconomy.api.currency.inventory.EnderCurrencyInventory;
import com.calemi.ceconomy.block.entity.EnderBankBlockEntity;
import com.calemi.ceconomy.registry.BlockEntityTypeRegistry;
import com.calemi.ceconomy.registry.BlockRegistry;
import com.calemi.ceconomy.screen.handler.EnderBankScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class EnderBankBlock extends BlockWithEntity {

    public EnderBankBlock() {
        super(BlockRegistry.CURRENCY_NETWORK_BLOCK_SETTINGS);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {

        if (!world.isClient) {

            player.openHandledScreen(new ExtendedScreenHandlerFactory() {

                @Override
                public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
                    buf.writeLong(new EnderCurrencyInventory(player).getCurrency());
                }

                @Override
                public Text getDisplayName() {
                    return world.getBlockState(pos).getBlock().getName();
                }

                @Nullable
                @Override
                public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
                    return new EnderBankScreenHandler(syncId, playerInventory, new EnderCurrencyInventory(player).getCurrency());
                }
            });
        }

        return ActionResult.SUCCESS;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new EnderBankBlockEntity(pos, state);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(world, type, BlockEntityTypeRegistry.ENDER_BANK);
    }

    @Nullable
    protected static <T extends BlockEntity> BlockEntityTicker<T> checkType(World world, BlockEntityType<T> givenType, BlockEntityType<? extends EnderBankBlockEntity> expectedType) {
        return world.isClient ? null : checkType(givenType, expectedType, EnderBankBlockEntity::tick);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}
