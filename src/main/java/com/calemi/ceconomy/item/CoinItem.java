package com.calemi.ceconomy.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;

public class CoinItem extends BlockItem {

    public CoinItem(Block block) {
        super(block, new FabricItemSettings());
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {

        ItemStack stack = context.getStack();

        if (context.getPlayer() == null || context.getPlayer().isCreative() || stack.getCount() >= 8) {
            return place(new ItemPlacementContext(context));
        }

        return ActionResult.FAIL;
    }

    @Override
    protected boolean place(ItemPlacementContext context, BlockState state) {
        context.getStack().decrement(7);
        return super.place(context, state);
    }
}
