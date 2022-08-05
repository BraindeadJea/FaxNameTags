package com.itndev.hordecore.DistanceManager;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class LastLocation {

    public static HashMap<Player, Location> LastLocation = new HashMap<>();

    public static HashMap<Player, Location> LastPacketLocation = new HashMap<>();

}
