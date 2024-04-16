package com.calemi.ceconomy.compat;

import dev.ftb.mods.ftbteams.api.FTBTeamsAPI;
import dev.ftb.mods.ftbteams.api.Team;
import dev.ftb.mods.ftbteams.data.PartyTeam;

import java.util.Optional;
import java.util.UUID;

public class FTBTeamsChecker {

    private static final FTBTeamsAPI.API api = FTBTeamsAPI.api();

    public static boolean isInTeamOfTarget(boolean checkAlly, UUID sourcePlayerID, UUID targetPlayerID) {

        Optional<Team> sourceTeam = Optional.empty();
        Optional<Team> targetTeam = Optional.empty();

        sourceTeam = api.getManager().getTeamForPlayerID(sourcePlayerID);
        targetTeam = api.getManager().getTeamForPlayerID(targetPlayerID);

        if (targetTeam.isPresent()) {

            if (checkAlly && targetTeam.get().isPartyTeam()) {
                PartyTeam partyTeam = (PartyTeam) targetTeam.get();
                return partyTeam.isAllyOrBetter(sourcePlayerID);
            }

            if (sourceTeam.isPresent()) {
                return sourceTeam.get().equals(targetTeam.get());
            }
        }

        return false;
    }
}
