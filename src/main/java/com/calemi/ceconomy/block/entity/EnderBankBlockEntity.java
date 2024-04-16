package com.calemi.ceconomy.block.entity;

import com.calemi.ceconomy.api.currency.inventory.EnderCurrencyInventory;
import com.calemi.ceconomy.registry.BlockEntityTypeRegistry;
import com.calemi.ceconomy.screen.handler.EnderBankScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class EnderBankBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory {

    public EnderBankBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public EnderBankBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityTypeRegistry.ENDER_BANK, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state, EnderBankBlockEntity bank) {

    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
    }

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
}
