package com.itndev.hordecore.DistanceManager;

import com.itndev.hordecore.HordeCore;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class DistanceCalculation {

    public ArrayList<Player> nearbyPlayers(Location loc, double distance) {
        ArrayList<Player> list = new ArrayList<>();
        for(Player p : Bukkit.getOnlinePlayers()) {
            if(HordeCore.basicUtils.in_distance(loc, p.getLocation(), distance)) {
                list.add(p);
            }
        }
        return list;
    }
}
