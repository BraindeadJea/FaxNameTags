package com.itndev.hordecore.Utils;

import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.util.ArrayList;

public class BasicUtils {

    private static ArrayList<Integer> list = new ArrayList<>();
    private static Integer count = 1;

    public Double distance(Location loc1, Location loc2) {
        if(loc1.getWorld() != loc2.getWorld()) {
            return -1D;
        } else {
            return loc1.distance(loc2);
        }
    }

    public Boolean in_distance(Location loc1, Location loc2, double distance) {
        if(loc1.getWorld() != loc2.getWorld()) {
            return false;
        } else {
            return loc1.distance(loc2) < distance;
        }
    }

    public synchronized static Integer getNewID() {
        while (true) {
            Integer newid = (((int) (Math.random() * 99999) + 1)*1000) + count;
            count++;
            if(!list.contains(newid)) {
                list.add(newid);
                return newid;
            }
        }
    }

    public String colorize(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
