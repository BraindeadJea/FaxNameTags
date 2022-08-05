package com.itndev.hordecore.NameTag;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Cache {

    public static ConcurrentHashMap<UUID, PacketSpawnEntity> UUID_ENTITYID = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<Player, Integer> player_stand_id = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<UUID, ArrayList<UUID>> PLAYERS_LOOKING_AT = new ConcurrentHashMap<>();
}
