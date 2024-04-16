package com.calemi.ceconomy.api.currency.network;

import com.calemi.ccore.api.location.Location;
import com.calemi.ceconomy.api.currency.inventory.ICurrencyInventoryBlock;
import com.calemi.ceconomy.api.general.CurrencyNetworkScanner;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.List;

public class CurrencyNetworkHelper {

    public static ICurrencyInventoryBlock getConnectedBankWithLeastCurrency(Location location) {

        List<ICurrencyNetworkBank> connectedBanks = getConnectedBanks(location);

        ICurrencyInventoryBlock selectedBank = null;
        long currency = Long.MAX_VALUE;

        for (ICurrencyNetworkBank bank : connectedBanks) {

            if (bank instanceof ICurrencyInventoryBlock currencyBlock) {

                if (currencyBlock.getCurrencyInventory().getCurrency() < currency) {

                    selectedBank = currencyBlock;
                    currency = currencyBlock.getCurrencyInventory().getCurrency();
                }
            }
        }

        return selectedBank;
    }

    public static ICurrencyInventoryBlock getConnectedBankWithMostCurrency(Location location) {

        List<ICurrencyNetworkBank> connectedBanks = getConnectedBanks(location);

        ICurrencyInventoryBlock selectedBank = null;
        long currency = -1;

        for (ICurrencyNetworkBank bank : connectedBanks) {

            if (bank instanceof ICurrencyInventoryBlock currencyBlock) {

                if (currencyBlock.getCurrencyInventory().getCurrency() > currency) {

                    selectedBank = currencyBlock;
                    currency = currencyBlock.getCurrencyInventory().getCurrency();
                }
            }
        }

        return selectedBank;
    }

    public static List<ICurrencyNetworkBank> getConnectedBanks(Location location) {

        List<ICurrencyNetworkBank> connectedBanks = new ArrayList<>();

        CurrencyNetworkScanner scanner = new CurrencyNetworkScanner(location);
        scanner.start();

        for (Location collectedLocation : scanner.collectedLocations) {

            if (collectedLocation.getBlockEntity() instanceof ICurrencyNetworkBank bank) {
                connectedBanks.add(bank);
            }
        }

        return connectedBanks;
    }

    public static boolean canConnectInDirection(ICurrencyNetwork currencyNetwork, Direction direction) {

        for (Direction dir : currencyNetwork.getConnectedDirections()) {

            if (dir.equals(direction)) {
                return true;
            }
        }

        return false;
    }
}
