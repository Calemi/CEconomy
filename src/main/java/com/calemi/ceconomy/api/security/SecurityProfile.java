package com.calemi.ceconomy.api.security;

import com.calemi.ceconomy.compat.FTBTeamsChecker;
import com.calemi.ceconomy.config.CEconomyConfig;
import com.calemi.ceconomy.main.CEconomyMain;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

import java.util.UUID;

public class SecurityProfile {

    private UUID ownerID = null;
    private String ownerName = "";

    public void setSecurityProfile(SecurityProfile securityProfile) {
        ownerID = securityProfile.getOwnerID();
        ownerName = securityProfile.getOwnerName();
    }

    public void setOwner(PlayerEntity player) {
        ownerID = player.getUuid();
        ownerName = player.getName().getString();
    }

    public boolean canAccess(UUID playerID) {

        if (!CEconomyConfig.COMMON.securitySystem) {
            return true;
        }


        if (CEconomyMain.ftbTeamsLoaded()) {

            if (CEconomyConfig.COMMON.teammateSecurityAccess && FTBTeamsChecker.isInTeamOfTarget(CEconomyConfig.COMMON.allySecurityAccess, playerID, ownerID)) {
                return true;
            }
        }

        return ownerID.equals(playerID);
    }

    public boolean canAccess(SecurityProfile securityProfile) {
        return canAccess(securityProfile.ownerID);
    }

    public UUID getOwnerID() {
        return ownerID;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void readFromNBT(NbtCompound nbt) {
        NbtCompound securityTag = nbt.getCompound("Security");

        if (securityTag.containsUuid("OwnerID")) {
            ownerID = securityTag.getUuid("OwnerID");
        }

        ownerName = securityTag.getString("OwnerName");
    }

    public void writeToNBT(NbtCompound nbt) {
        NbtCompound securityTag = new NbtCompound();

        if (ownerID != null) {
            securityTag.putUuid("OwnerID", getOwnerID());
        }

        securityTag.putString("OwnerName", getOwnerName());
        nbt.put("Security", securityTag);
    }
}
