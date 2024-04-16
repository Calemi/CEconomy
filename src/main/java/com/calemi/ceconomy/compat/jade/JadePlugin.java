package com.calemi.ceconomy.compat.jade;

import com.calemi.ceconomy.block.TradingPostBlock;
import net.minecraft.block.Block;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class JadePlugin implements IWailaPlugin {

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(SecuredBlockComponentProvider.INSTANCE, Block.class);
        registration.registerBlockComponent(CurrencyBlockComponentProvider.INSTANCE, Block.class);
        registration.registerBlockComponent(TradingPostComponentProvider.INSTANCE, TradingPostBlock.class);
    }
}