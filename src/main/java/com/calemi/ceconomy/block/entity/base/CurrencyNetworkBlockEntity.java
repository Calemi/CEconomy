package com.calemi.ceconomy.block.entity.base;

import com.calemi.ceconomy.api.currency.network.ICurrencyNetwork;
import com.calemi.ceconomy.api.security.ISecuredBlock;
import com.calemi.ceconomy.api.security.SecurityProfile;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public class CurrencyNetworkBlockEntity extends BlockEntity implements ISecuredBlock, ICurrencyNetwork {

    private final SecurityProfile securityProfile = new SecurityProfile();

    public CurrencyNetworkBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public SecurityProfile getSecurityProfile() {
        return securityProfile;
    }

    @Override
    public Direction[] getConnectedDirections() {
        return new Direction[] {Direction.UP, Direction.DOWN, Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST};
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        securityProfile.readFromNBT(nbt);

    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        securityProfile.writeToNBT(nbt);
    }

    @Override
    public void markDirty() {
        world.updateListeners(pos, getCachedState(), getCachedState(), 3);
        super.markDirty();
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }
}
