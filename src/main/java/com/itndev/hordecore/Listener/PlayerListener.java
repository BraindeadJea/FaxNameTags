package com.itndev.hordecore.Listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.itndev.hordecore.DistanceManager.DistanceCalculation;
import com.itndev.hordecore.DistanceManager.LastLocation;
import com.itndev.hordecore.HordeCore;
import com.itndev.hordecore.NameTag.Cache;
import com.itndev.hordecore.NameTag.PacketSpawnEntity;
import com.itndev.hordecore.Utils.CacheUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.UUID;

public class PlayerListener implements Listener {

    public static HashMap<Integer, Player> EntityID_To_Player = new HashMap<>();

    @EventHandler(ignoreCancelled = true)
    public void onTeleport(PlayerTeleportEvent e) {
        if(e.getTo().getWorld() != e.getFrom().getWorld() || e.getTo().distanceSquared(e.getFrom()) > 64) {
            new Thread(() -> {
                if(Cache.UUID_ENTITYID.containsKey(e.getPlayer().getUniqueId())) {
                    Player this_player = e.getPlayer();
                    PacketSpawnEntity pse = Cache.UUID_ENTITYID.get(this_player.getUniqueId());
                    try {
                        pse.destroy();
                        pse.spawn_a("&c[TAG] " + this_player.getName(), e.getTo());
                        pse.destroy(this_player);
                        pse.teleport(e.getTo());
                    } catch (InvocationTargetException invocationTargetException) {
                        invocationTargetException.printStackTrace();
                    }
                    new Thread(() -> {
                        for(Player p : new DistanceCalculation().nearbyPlayers(e.getTo(), 40)) {
                            UUID temp = p.getUniqueId();
                            if(Cache.UUID_ENTITYID.containsKey(temp)) {
                                try {
                                    Cache.UUID_ENTITYID.get(temp).spawnfornewplayer(this_player);
                                } catch (InvocationTargetException invocationTargetException) {
                                    invocationTargetException.printStackTrace();
                                }
                            }
                        }
                    }).start();

                }
            /*
            try {
                Cache.UUID_ENTITYID.get(e.getPlayer().getUniqueId()).destroy();
                Cache.UUID_ENTITYID.get(e.getPlayer().getUniqueId()).spawn("&c[TAG] " + e.getPlayer().getName());
                LastLocation.LastLocation.put(e.getPlayer(), e.getTo());
            } catch (InvocationTargetException invocationTargetException) {
                invocationTargetException.printStackTrace();
            }*/
                //System.out.println(e.getPlayer().getName() + " teleported");
            }).start();
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        EntityID_To_Player.put(e.getPlayer().getEntityId(), e.getPlayer());
        e.getPlayer().setCustomNameVisible(false);
        new Thread(() -> {

            /*
            PacketContainer pc = HordeCore.protocolManager.createPacket(PacketType.Play.Server.PLAYER_LIST_HEADER_FOOTER);

            pc.getChatComponents().write(0, WrappedChatComponent.fromText(HordeCore.basicUtils.colorize("&e&l[ &9Kilo &bOnline &e&l]\n" +
                    "&a총 접속 인원&f :&b @&f / &b@")))
                    .write(1, WrappedChatComponent.fromText(HordeCore.basicUtils.colorize("footer text")));
            try {
                HordeCore.protocolManager.sendServerPacket(e.getPlayer(), pc);
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }*/

            Player connectedplayer = e.getPlayer();
            PacketSpawnEntity pse = new PacketSpawnEntity(e.getPlayer());
            pse.spawn("&c[TAG] " + e.getPlayer().getName());
            try {
                pse.destroy(e.getPlayer());
            } catch (InvocationTargetException invocationTargetException) {
                invocationTargetException.printStackTrace();
            }
            Cache.UUID_ENTITYID.put(e.getPlayer().getUniqueId(), pse);
            for(Player online : Bukkit.getOnlinePlayers()) {
                try {
                    if(Cache.UUID_ENTITYID.containsKey(online.getUniqueId())) {
                        Cache.UUID_ENTITYID.get(online.getUniqueId()).spawnfornewplayer(connectedplayer);
                    }

                } catch (InvocationTargetException invocationTargetException) {
                    invocationTargetException.printStackTrace();
                }
            }
        }).start();
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        EntityID_To_Player.remove(p.getEntityId());
        new Thread(() -> {
            if(Cache.UUID_ENTITYID.containsKey(p.getUniqueId())) {
                try {
                    Cache.UUID_ENTITYID.get(p.getUniqueId()).destroy();
                    Cache.UUID_ENTITYID.remove(p.getUniqueId());
                    LastLocation.LastLocation.remove(p);
                } catch (InvocationTargetException invocationTargetException) {
                    invocationTargetException.printStackTrace();
                }
            }
            CacheUtils.remove_watcher(p.getUniqueId());
        }).start();
    }
}
