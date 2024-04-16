package com.calemi.ceconomy.event.listener;

import com.calemi.ccore.api.event.BlockPlacePostCallback;
import com.calemi.ceconomy.api.currency.CurrencyHelper;
import com.calemi.ceconomy.api.currency.inventory.ICurrencyInventoryBlock;
import com.calemi.ceconomy.api.currency.inventory.ICurrencyInventoryItem;
import com.calemi.ceconomy.api.currency.inventory.ItemCurrencyInventory;
import com.calemi.ceconomy.api.item.ValuableItem;
import com.calemi.ceconomy.api.item.ValuableItemHelper;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;

public class CurrencyEventListener {

    public static void init() {

        /*
            BREAKING CURRENCY BLOCK
         */
        PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, entity) -> {

            if (world.isClient || player.isCreative()) {
                return true;
            }

            BlockEntity blockEntity = world.getBlockEntity(pos);

            if (blockEntity instanceof ICurrencyInventoryBlock currencyBlock) {

                Item droppedItem = world.getBlockState(pos).getBlock().asItem();
                ItemStack droppedStack = new ItemStack(droppedItem);

                if (droppedItem instanceof ICurrencyInventoryItem currencyItem) {
                    currencyItem.getCurrencyInventory(droppedStack).depositCurrency(currencyBlock.getCurrencyInventory().getCurrency());
                    Block.dropStack(world, pos, droppedStack);
                }
            }

            return true;
        });

        /*
            PLACING CURRENCY BLOCK
         */
        BlockPlacePostCallback.EVENT.register((pos, world, player, stack, state) -> {

            if (stack.getItem() instanceof ICurrencyInventoryItem currencyItem) {

                BlockEntity blockEntity = world.getBlockEntity(pos);

                if (blockEntity instanceof ICurrencyInventoryBlock currencyBlock) {

                    currencyBlock.getCurrencyInventory().depositCurrency(currencyItem.getCurrencyInventory(stack).getCurrency());
                }
            }

            return ActionResult.PASS;
        });
    }

    public static void initClient() {

        /*
            CURRENCY TOOLTIP
         */
        ItemTooltipCallback.EVENT.register((stack, context, lines) -> {

            Item item = stack.getItem();

            if (item instanceof ICurrencyInventoryItem currencyItem) {

                ItemCurrencyInventory currencyInv = currencyItem.getCurrencyInventory(stack);

                CurrencyHelper.addCurrencyLore(lines, "currency", currencyInv.getCurrency(), currencyItem.showCapacityInTooltip(stack) ? currencyInv.getCurrencyCapacity() : 0);
            }

            ValuableItem value = ValuableItemHelper.getItemValue(item);

            if (value != null) {
                CurrencyHelper.addCurrencyLore(lines, "value", value.getValue());
                if (stack.getCount() > 1) CurrencyHelper.addCurrencyLore(lines, "total", stack.getCount() * value.getValue());
            }
        });
    }
}
