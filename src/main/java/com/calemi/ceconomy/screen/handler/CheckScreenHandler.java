package com.calemi.ceconomy.screen.handler;

import com.calemi.ccore.api.screen.handler.BaseScreenHandler;
import com.calemi.ceconomy.registry.ScreenHandlerTypeRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;

public class CheckScreenHandler extends BaseScreenHandler {

    private final boolean isMainHand;
    private final ItemStack checkStack;

    public CheckScreenHandler(int syncId, PlayerInventory playerInv, PacketByteBuf data) {
        this(syncId, playerInv, data.readBoolean(), data.readItemStack());
    }

    public CheckScreenHandler(int syncId, PlayerInventory playerInv, boolean isMainHand, ItemStack checkStack) {
        super(ScreenHandlerTypeRegistry.CHECK, syncId);
        this.isMainHand = isMainHand;
        this.checkStack = checkStack;
        addPlayerInventory(playerInv, 69);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return checkStack != null && !checkStack.isEmpty();
    }

    @Override
    public int getContainerSize() {
        return 0;
    }

    public boolean isMainHand() {
        return isMainHand;
    }

    public ItemStack getCheckStack() {
        return checkStack;
    }
}
