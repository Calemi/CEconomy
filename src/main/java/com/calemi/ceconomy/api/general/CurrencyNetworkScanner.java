package com.calemi.ceconomy.api.general;

import com.calemi.ccore.api.location.Location;
import com.calemi.ccore.api.scan.BlockScanner;
import com.calemi.ceconomy.api.currency.network.CurrencyNetworkHelper;
import com.calemi.ceconomy.api.currency.network.ICurrencyNetwork;
import com.calemi.ceconomy.api.security.ISecuredBlock;
import com.calemi.ceconomy.config.CEconomyConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.Direction;
import java.util.ArrayList;
import java.util.List;

public class CurrencyNetworkScanner extends BlockScanner {

    private final ISecuredBlock originSecuredBlock;

    public CurrencyNetworkScanner(Location origin) {
        super(origin, CEconomyConfig.COMMON.maxCurrencyNetworkSize);
        originSecuredBlock = (ISecuredBlock) origin.getBlockEntity();
    }

    @Override
    public boolean shouldCollect(Location scannedlocation, BlockState scannedState) {

        BlockEntity blockEntity = scannedlocation.getBlockEntity();

        if (blockEntity instanceof ICurrencyNetwork) {

            if (blockEntity instanceof ISecuredBlock securedBlock) {
                return originSecuredBlock.getSecurityProfile().canAccess(securedBlock.getSecurityProfile());
            }

            return true;
        }

        return false;
    }

    @Override
    public boolean continueOnFailedCollect() {
        return false;
    }

    @Override
    public List<Location> nextLocationsToScan(Location scannedLocation, BlockState scannedState) {

        List<Location> nextLocationsToScan = new ArrayList<>();

        BlockEntity blockEntity = scannedLocation.getBlockEntity();

        if (blockEntity instanceof ICurrencyNetwork currencyNetwork) {

            for (Direction dir : currencyNetwork.getConnectedDirections()) {

                Location dirLocation = scannedLocation.copy();
                dirLocation.relative(dir);

                BlockEntity dirBlockEntity = dirLocation.getBlockEntity();

                if (dirBlockEntity instanceof ICurrencyNetwork dirCurrencyNetwork) {

                    if (CurrencyNetworkHelper.canConnectInDirection(dirCurrencyNetwork, dir.getOpposite())) {
                        nextLocationsToScan.add(dirLocation);
                    }
                }
            }
        }

        return nextLocationsToScan;
    }
}
