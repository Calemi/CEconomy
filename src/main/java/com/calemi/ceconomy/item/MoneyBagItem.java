package com.calemi.ceconomy.item;

import com.calemi.ccore.api.general.CCoreMathHelper;
import com.calemi.ccore.api.item.ItemHelper;
import com.calemi.ccore.api.sound.SoundHelper;
import com.calemi.ccore.api.sound.SoundProfile;
import com.calemi.ceconomy.registry.ItemRegistry;
import com.calemi.ceconomy.registry.SoundEventRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class MoneyBagItem extends Item {

    private final int minAmount;
    private final int maxAmount;

    public MoneyBagItem(int minAmount, int maxAmount) {
        super(new FabricItemSettings());
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {

        ItemStack stack = player.getStackInHand(hand);
        stack.decrement(1);
        giveCoins(world, player);

        if (stack.getItem() == ItemRegistry.RICH_MONEY_BAG) {
            SoundHelper.playAtPlayer(player, new SoundProfile(SoundEventRegistry.RICH_MONEY_BAG).setVolume(0.1F));
        }

        else {
            SoundHelper.playAtPlayer(player, new SoundProfile(SoundEventRegistry.CHEAP_MONEY_BAG).setVolume(0.1F));
        }

        return TypedActionResult.success(stack);
    }

    private void giveCoins(World world, PlayerEntity player) {

        if (!world.isClient()) {

            int amount = minAmount + CCoreMathHelper.random.nextInt(maxAmount - minAmount);

            int netherite = (int)Math.floor((float)amount / 100000);
            amount -= (netherite * 100000);

            int diamond = (int)Math.floor((float)amount / 10000);
            amount -= (diamond * 10000);

            int platinum = (int)Math.floor((float)amount / 1000);
            amount -= (platinum * 1000);

            int gold = (int)Math.floor((float)amount / 100);
            amount -= (gold * 100);

            int silver = (int)Math.floor((float)amount / 10);
            amount -= (silver * 10);

            int copper = amount;

            ItemHelper.giveItem(player, new ItemStack(ItemRegistry.NETHERITE_COIN), netherite);
            ItemHelper.giveItem(player, new ItemStack(ItemRegistry.DIAMOND_COIN), diamond);
            ItemHelper.giveItem(player, new ItemStack(ItemRegistry.PLATINUM_COIN), platinum);
            ItemHelper.giveItem(player, new ItemStack(ItemRegistry.GOLD_COIN), gold);
            ItemHelper.giveItem(player, new ItemStack(ItemRegistry.SILVER_COIN), silver);
            ItemHelper.giveItem(player, new ItemStack(ItemRegistry.COPPER_COIN), copper);
        }
    }
}
