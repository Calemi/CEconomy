package com.calemi.ceconomy.block.entity;

import com.calemi.ceconomy.api.currency.*;
import com.calemi.ceconomy.api.currency.inventory.BlockCurrencyInventory;
import com.calemi.ceconomy.api.currency.inventory.ICurrencyInventoryBlock;
import com.calemi.ceconomy.api.currency.inventory.ICurrencyInventoryItem;
import com.calemi.ceconomy.api.currency.inventory.ItemCurrencyInventory;
import com.calemi.ceconomy.api.currency.network.ICurrencyNetworkBank;
import com.calemi.ceconomy.api.item.IPlaceInCurrencyContainer;
import com.calemi.ceconomy.api.item.ValuableItem;
import com.calemi.ceconomy.api.item.ValuableItemHelper;
import com.calemi.ceconomy.block.entity.base.CurrencyNetworkBlockEntity;
import com.calemi.ceconomy.config.CEconomyConfig;
import com.calemi.ceconomy.inventory.BankInventory;
import com.calemi.ceconomy.main.CEconomyMain;
import com.calemi.ceconomy.registry.BlockEntityTypeRegistry;
import com.calemi.ceconomy.screen.handler.BankScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BankBlockEntity extends CurrencyNetworkBlockEntity implements ICurrencyNetworkBank, ICurrencyInventoryBlock, BankInventory {

    private final BlockCurrencyInventory currencyInventory = new BlockCurrencyInventory(CEconomyConfig.COMMON.bankCurrencyCapacity);

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(2, ItemStack.EMPTY);

    public static final int WALLET_SLOT = 0;
    public static final int VALUABLE_ITEM_SLOT = 1;

    public BankBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public BankBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityTypeRegistry.BANK, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state, BankBlockEntity bank) {

        BlockCurrencyInventory bankCurrencyInv = bank.getCurrencyInventory();

        ItemStack stack = bank.getStack(VALUABLE_ITEM_SLOT);
        ValuableItem valuableItem = ValuableItemHelper.getItemValue(stack);

        if (valuableItem != null && valuableItem.getValue() > 0) {

            if (bankCurrencyInv.canDepositCurrency(valuableItem.getValue())) {
                bankCurrencyInv.depositCurrency(valuableItem.getValue());
                bank.removeStack(VALUABLE_ITEM_SLOT, 1);
                bank.markDirty();
            }
        }

        else if (stack.getItem() instanceof ICurrencyInventoryItem slotCurrencyItem && stack.getItem() instanceof IPlaceInCurrencyContainer placeInBankItem) {

            ItemCurrencyInventory slotCurrencyInv = slotCurrencyItem.getCurrencyInventory(stack);

            CEconomyMain.LOGGER.info("WALLET");

            long currencyInSlot = slotCurrencyInv.getCurrency();
            long space = bankCurrencyInv.getCurrencyCapacity() - bankCurrencyInv.getCurrency();

            if (placeInBankItem.consumeInCurrencyContainer(stack)) {

                if (CurrencyHelper.tryTransferCurrency(slotCurrencyInv, bankCurrencyInv, currencyInSlot)) {
                    bank.removeStack(VALUABLE_ITEM_SLOT, 1);
                    bank.markDirty();
                }
            }

            else {

                if (slotCurrencyInv.getCurrency() > 0) {
                    CurrencyHelper.transferOrFillCurrency(slotCurrencyInv, bankCurrencyInv, currencyInSlot);
                    bank.markDirty();
                }
            }
        }
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, inventory);
        currencyInventory.setCurrency(CurrencyHelper.readFromNBT(nbt));
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
        CurrencyHelper.writeToNBT(nbt, currencyInventory.getCurrency());
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }



    @Override
    public BlockCurrencyInventory getCurrencyInventory() {
        return currencyInventory;
    }
}
