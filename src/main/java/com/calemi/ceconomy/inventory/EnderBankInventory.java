package com.calemi.ceconomy.inventory;

import com.calemi.ceconomy.block.entity.EnderBankBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import org.jetbrains.annotations.Nullable;

public class EnderBankInventory extends SimpleInventory {

    @Nullable
    private EnderBankBlockEntity activeBlockEntity;

    public EnderBankInventory() {
        super(2);
    }

    public void setActiveBlockEntity(EnderBankBlockEntity blockEntity) {
        this.activeBlockEntity = blockEntity;
    }

    public boolean isActiveBlockEntity(EnderBankBlockEntity blockEntity) {
        return this.activeBlockEntity == blockEntity;
    }

    public void readNbtList(NbtList nbtList) {
        int i;
        for(i = 0; i < this.size(); ++i) {
            this.setStack(i, ItemStack.EMPTY);
        }

        for(i = 0; i < nbtList.size(); ++i) {
            NbtCompound nbtCompound = nbtList.getCompound(i);
            int j = nbtCompound.getByte("Slot") & 255;
            if (j >= 0 && j < this.size()) {
                this.setStack(j, ItemStack.fromNbt(nbtCompound));
            }
        }

    }

    public NbtList toNbtList() {
        NbtList nbtList = new NbtList();

        for(int i = 0; i < this.size(); ++i) {
            ItemStack itemStack = this.getStack(i);
            if (!itemStack.isEmpty()) {
                NbtCompound nbtCompound = new NbtCompound();
                nbtCompound.putByte("Slot", (byte)i);
                itemStack.writeNbt(nbtCompound);
                nbtList.add(nbtCompound);
            }
        }

        return nbtList;
    }

    public boolean canPlayerUse(PlayerEntity player) {
        return this.activeBlockEntity == null && super.canPlayerUse(player);
    }

    public void onClose(PlayerEntity player) {
        this.activeBlockEntity = null;
    }
}