package com.calemi.ceconomy.block.entity;

import com.calemi.ccore.api.location.Location;
import com.calemi.ceconomy.block.CurrencyNetworkGateBlock;
import com.calemi.ceconomy.block.entity.base.CurrencyNetworkBlockEntity;
import com.calemi.ceconomy.registry.BlockEntityTypeRegistry;
import com.calemi.ceconomy.registry.BlockRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class CurrencyNetworkGateBlockEntity extends CurrencyNetworkBlockEntity {

    private boolean hasChanged = false;

    public CurrencyNetworkGateBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityTypeRegistry.CURRENCY_NETWORK_GATE, pos, state);
    }

    public static void setConnectedState(Location location, boolean value) {

        location.getWorld().setBlockState(location.getBlockPos(), BlockRegistry.CURRENCY_NETWORK_GATE.getDefaultState().with(CurrencyNetworkGateBlock.CONNECTED, value), 3);
    }

    public static void tick(World world, BlockPos pos, BlockState state, CurrencyNetworkGateBlockEntity gate) {

        if (world.getReceivedStrongRedstonePower(pos) > 0) {

            if (!gate.hasChanged) {
                setConnectedState(gate.getLocation(), false);
            }

            gate.hasChanged = true;
        }

        else {

            if (gate.hasChanged) {
                setConnectedState(gate.getLocation(), true);
            }

            gate.hasChanged = false;
        }
    }

    @Override
    public Direction[] getConnectedDirections() {

        if (getLocation() != null && getLocation().getBlock() instanceof CurrencyNetworkGateBlock gate) {

            if (getLocation().getBlockState().get(CurrencyNetworkGateBlock.CONNECTED)) {
                return Direction.values();
            }
        }

        return new Direction[]{};
    }

    public Location getLocation() {
        return new Location(getWorld(), getPos());
    }
}
