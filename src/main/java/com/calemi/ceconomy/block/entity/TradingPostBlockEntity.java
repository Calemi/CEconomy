package com.calemi.ceconomy.block.entity;

import com.calemi.ccore.api.location.Location;
import com.calemi.ceconomy.block.entity.base.CurrencyNetworkBlockEntity;
import com.calemi.ceconomy.inventory.ImplementedInventory;
import com.calemi.ceconomy.registry.BlockEntityTypeRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TradingPostBlockEntity extends CurrencyNetworkBlockEntity implements ImplementedInventory {

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(27, ItemStack.EMPTY);

    private ItemStack tradeStack = ItemStack.EMPTY;

    private long tradePrice = 0;
    private int tradeAmount = 1;

    private boolean buyMode = false;
    private boolean adminMode = false;

    private int broadcastDelayTicks = 0;

    public TradingPostBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public TradingPostBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityTypeRegistry.TRADING_POST, pos, state);
    }

    public boolean isTradeValid() {
        return !tradeStack.isEmpty();
    }

    public static void tick(World world, BlockPos pos, BlockState state, TradingPostBlockEntity tradingPost) {

        if (tradingPost.broadcastDelayTicks > 0) {
            tradingPost.broadcastDelayTicks--;
        }
    }

    /*
        NBT METHODS
     */

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        Inventories.readNbt(nbt, inventory);

        tradeAmount = nbt.getInt("TradeAmount");
        NbtCompound stackCompound = nbt.getCompound("TradeStack");
        tradeStack = ItemStack.fromNbt(stackCompound);

        tradePrice = nbt.getLong("TradePrice");

        buyMode = nbt.getBoolean("BuyMode");
        adminMode = nbt.getBoolean("AdminMode");

        broadcastDelayTicks = nbt.getInt("BroadcastDelay");
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);

        Inventories.writeNbt(nbt, inventory);

        NbtCompound stackCompound = new NbtCompound();
        tradeStack.writeNbt(stackCompound);
        nbt.put("TradeStack", stackCompound);

        nbt.putLong("TradePrice", tradePrice);
        nbt.putInt("TradeAmount", tradeAmount);

        nbt.putBoolean("AdminMode", adminMode);
        nbt.putBoolean("BuyMode", buyMode);

        nbt.putInt("BroadcastDelay", broadcastDelayTicks);
    }

    /*
        GETTER & SETTER METHODS
     */

    public ItemStack getTradeStack() {
        return tradeStack;
    }

    public void setTradeStack(ItemStack tradeStack) {
        this.tradeStack = tradeStack;
    }

    public long getTradePrice() {
        return tradePrice;
    }

    public void setTradePrice(long tradePrice) {
        this.tradePrice = tradePrice;
    }

    public int getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(int tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    public boolean isBuyMode() {
        return buyMode;
    }

    public void setBuyMode(boolean buyMode) {
        this.buyMode = buyMode;
    }

    public boolean isAdminMode() {
        return adminMode;
    }

    public void setAdminMode(boolean adminMode) {
        this.adminMode = adminMode;
    }

    public int getBroadcastDelayTicks() {
        return broadcastDelayTicks;
    }

    public void setBroadcastDelayTicks(int broadcastDelayTicks) {
        this.broadcastDelayTicks = broadcastDelayTicks;
    }

    /*
        BASE METHODS
     */

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    public Location getLocation() {
        return new Location(getWorld(), getPos());
    }
}
