package com.calemi.ceconomy.block.entity;

import com.calemi.ceconomy.registry.BlockEntityTypeRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EnderBankBlockEntity extends BlockEntity {

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
}
