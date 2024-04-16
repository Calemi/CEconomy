package com.calemi.ceconomy.inventory;

import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;

public class ItemStackInventory implements ImplementedInventory {

    private final ItemStack stack;
    private final DefaultedList<ItemStack> items;

    public ItemStackInventory(ItemStack stack, int size) {
        this.stack = stack;
        this.items = DefaultedList.ofSize(size, ItemStack.EMPTY);

        NbtCompound tag = stack.getSubNbt("Inventory");

        if (tag != null) {
            Inventories.readNbt(tag, items);
        }
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return items;
    }

    @Override
    public void markDirty() {
        NbtCompound tag = stack.getOrCreateSubNbt("Inventory");
        Inventories.writeNbt(tag, items);
    }
}