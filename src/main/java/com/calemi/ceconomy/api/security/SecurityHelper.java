package com.calemi.ceconomy.api.security;

import net.minecraft.entity.player.PlayerEntity;

public class SecurityHelper {

    public static boolean canAccess(PlayerEntity player, ISecuredBlock securedBlock) {
        return player.isCreative() || securedBlock.getSecurityProfile().canAccess(player.getUuid());
    }
}
