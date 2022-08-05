package com.itndev.hordecore.Factions;

import com.itndev.factions.Utils.FactionUtils;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class FactionsAPI {


    private String Enemy_color = "&c";
    private String Ally_color = "&a";

    public Boolean isEnabled = false;

    public Boolean ifSameTeam(Player p1, Player p2) {
        return FactionUtils.isSameFaction(p1.getUniqueId().toString(), p2.getUniqueId().toString());
        //return false;
    }

    public String getFactionTag(Player Target) {
        String final_tag = "";
        String uuid = Target.getUniqueId().toString();
        if(FactionUtils.isInFaction(uuid)) {
            final_tag =  "&a[ &r" + FactionUtils.getCappedFactionName(FactionUtils.getFactionName(FactionUtils.getPlayerFactionUUID(Target.getUniqueId().toString()))) + " &r&a] &r";
        } else {
            final_tag = Target.getName();
        }
        return final_tag;
    }

    public String getFactionTag_DisplayTarget(Player Target, Player Display) {
        String final_tag = "";
        String uuid = Target.getUniqueId().toString();
        if(FactionUtils.isInFaction(uuid)) {
            String FactionName = FactionUtils.getCappedFactionName(FactionUtils.getFactionName(FactionUtils.getPlayerFactionUUID(uuid)));
            if(ifSameTeam(Target, Display)) {
                final_tag = Ally_color + "[" + FactionName + "] &r" + Ally_color + Target.getName();
            } else {
                final_tag = Enemy_color + "[" + FactionName + "] &r" + Enemy_color + Target.getName();
            }
        } else {
            final_tag = Enemy_color + Target.getName();
        }
        if(Target.isInvisible() || Target.getGameMode() == GameMode.SPECTATOR) {
            final_tag = "";
        }
        return final_tag;
    }
}
