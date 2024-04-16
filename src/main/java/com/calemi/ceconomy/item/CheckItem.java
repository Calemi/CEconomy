package com.calemi.ceconomy.item;

import com.calemi.ceconomy.api.currency.inventory.ICurrencyInventoryItem;
import com.calemi.ceconomy.api.currency.inventory.ItemCurrencyInventory;
import com.calemi.ceconomy.api.item.IPlaceInCurrencyContainer;
import com.calemi.ceconomy.config.CEconomyConfig;
import com.calemi.ceconomy.screen.handler.CheckScreenHandler;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class CheckItem extends Item implements ICurrencyInventoryItem, IPlaceInCurrencyContainer {

    public CheckItem() {
        super(new FabricItemSettings().maxCount(1));
    }

    public static boolean isWritten(ItemStack stack) {
        return stack.getOrCreateNbt().getBoolean("Written");
    }

    public static void setWritten(ItemStack stack) {
        stack.getOrCreateNbt().putBoolean("Written", true);
    }

    @Override
    public Text getName(ItemStack stack) {
        return isWritten(stack) ? Text.translatable("item.ceconomy.written_check") : super.getName(stack);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {

        if (world.isClient) return TypedActionResult.pass(player.getStackInHand(hand));

        ItemStack stack = player.getStackInHand(hand);

        if (isWritten(stack)) return TypedActionResult.pass(player.getStackInHand(hand));

        player.openHandledScreen(new ExtendedScreenHandlerFactory() {

            @Override
            public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
                buf.writeBoolean(hand == Hand.MAIN_HAND);
                buf.writeItemStack(stack);
            }

            @Override
            public Text getDisplayName() {
                return stack.getName();
            }

            @Override
            public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
                return new CheckScreenHandler(syncId, inv, hand == Hand.MAIN_HAND, stack);
            }
        });

        return TypedActionResult.success(player.getStackInHand(hand));
    }

    @Override
    public ItemCurrencyInventory getCurrencyInventory(ItemStack stack) {
        return new ItemCurrencyInventory(CEconomyConfig.COMMON.checkCurrencyCapacity, stack);
    }

    @Override
    public boolean showCapacityInTooltip(ItemStack stack) {
        return false;
    }

    @Override
    public boolean consumeInCurrencyContainer(ItemStack stack) {
        return isWritten(stack);
    }
}
