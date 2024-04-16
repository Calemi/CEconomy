package com.calemi.ceconomy.item;

import com.calemi.ccore.api.location.Location;
import com.calemi.ceconomy.api.currency.inventory.CurrencyInventory;
import com.calemi.ceconomy.api.item.IBankCard;
import com.calemi.ceconomy.block.BankBlock;
import com.calemi.ceconomy.block.EnderBankBlock;
import com.calemi.ceconomy.block.entity.BankBlockEntity;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BankCardItem extends Item implements IBankCard {

    public BankCardItem() {
        super(new FabricItemSettings().maxCount(1));
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        MutableText locationText = Text.literal(getLinkedLocation(world, stack).toString()).formatted(Formatting.GOLD);
        tooltip.add(Text.literal("Bank Location: ").formatted(Formatting.GRAY).append(locationText));

        super.appendTooltip(stack, world, tooltip, context);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {

        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();

        if (world.getBlockState(pos).getBlock() instanceof BankBlock || world.getBlockState(pos).getBlock() instanceof EnderBankBlock) {
            setLinkedLocation(context.getWorld(), context.getStack(), new Location(context.getWorld(), context.getBlockPos()));
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    public Location getLinkedLocation(World world, ItemStack stack) {

        Location location = new Location(world, 0, 0, 0);
        NbtCompound nbt = stack.getOrCreateNbt();

        if (nbt.contains("Location")) {
            location = Location.readFromNBT(world, nbt.getCompound("Location"));
        }

        return location;
    }

    public void setLinkedLocation(World world, ItemStack stack, Location location) {

        NbtCompound nbt = stack.getOrCreateNbt();

        NbtCompound locationCompound = new NbtCompound();
        location.writeToNBT(locationCompound);
        nbt.put("Location", locationCompound);

        stack.setNbt(nbt);
    }

    public CurrencyInventory getLinkedCurrencyInventory(PlayerEntity player, ItemStack stack) {

        Location location = getLinkedLocation(player.getWorld(), stack);

        if (!location.isZero()) {

            if (player.getWorld().getWorldChunk(location.getBlockPos()).getBlockEntity(location.getBlockPos(), WorldChunk.CreationType.IMMEDIATE) instanceof BankBlockEntity bank) {
                return bank.getCurrencyInventory();
            }
        }

        return null;
    }
}
