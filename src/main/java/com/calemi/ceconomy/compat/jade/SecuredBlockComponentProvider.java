package com.calemi.ceconomy.compat.jade;

import com.calemi.ceconomy.api.security.ISecuredBlock;
import com.calemi.ceconomy.config.CEconomyConfig;
import com.calemi.ceconomy.main.CEconomyRef;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum SecuredBlockComponentProvider implements IBlockComponentProvider {

    INSTANCE;

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {

        if (!CEconomyConfig.COMMON.securitySystem) {
            return;
        }

        if (blockAccessor.getBlockEntity() instanceof ISecuredBlock securedBlock) {

            if (securedBlock.getSecurityProfile() != null) {

                MutableText ownerName = Text.literal(securedBlock.getSecurityProfile().getOwnerName()).formatted(Formatting.GOLD);
                tooltip.add(Text.translatable("text.ceconomy.secured").append(": ").append(ownerName));
            }
        }
    }

    @Override
    public Identifier getUid() {
        return new Identifier(CEconomyRef.MOD_ID, "secured_block");
    }
}
