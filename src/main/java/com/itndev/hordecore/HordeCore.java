package com.itndev.hordecore;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.itndev.hordecore.DistanceManager.LastLocation;
import com.itndev.hordecore.Factions.FactionsAPI;
import com.itndev.hordecore.Listener.PlayerListener;
import com.itndev.hordecore.NameTag.Cache;
import com.itndev.hordecore.NameTag.PacketSpawnEntity;
import com.itndev.hordecore.Utils.BasicUtils;
import com.itndev.hordecore.Utils.CacheUtils;
import com.itndev.hordecore.Utils.HideNameTag;
import com.itndev.hordecore.Utils.PacketUtils;
import com.itndev.hordecore.Wrappers.WrapperPlayServerEntityTeleport;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class HordeCore extends JavaPlugin {

    public static BasicUtils basicUtils = new BasicUtils();

    public static FactionsAPI factionsAPI = new FactionsAPI();

    public static ProtocolManager protocolManager = null;

    private static HordeCore instance;

    public static HordeCore getInstance() {
        return instance;
    }


    @Deprecated
    private void updateArmorStandLocation() {
        new BukkitRunnable() {
            @Deprecated
            @Override
            public void run() {
                for(UUID uuid : Cache.UUID_ENTITYID.keySet()) {
                    new Thread(() -> {
                        Player tempplayer = Bukkit.getPlayer(uuid);
                        if(tempplayer != null) {

                            if(tempplayer.isRiptiding()) {
                                Cache.UUID_ENTITYID.get(tempplayer.getUniqueId()).teleport(tempplayer.getLocation());

                            }
                            if(tempplayer.getVehicle() != null) {
                                Cache.UUID_ENTITYID.get(tempplayer.getUniqueId()).teleport(tempplayer.getLocation().add(0, 1, 0));
                            }
                            if(true) {
                                WrapperPlayServerEntityTeleport playertp = new WrapperPlayServerEntityTeleport();
                                playertp.setOnGround(false);
                                playertp.setEntityID(tempplayer.getEntityId());
                                playertp.setX(tempplayer.getLocation().getX());
                                playertp.setY(tempplayer.getLocation().getY());
                                playertp.setZ(tempplayer.getLocation().getZ());
                                playertp.setPitch(tempplayer.getLocation().getPitch());
                                playertp.setYaw(tempplayer.getLocation().getYaw());
                                PacketUtils.broadcastpacketA_TP(playertp, tempplayer);
                            }

                            for (UUID uuid2 : Cache.UUID_ENTITYID.keySet()) {
                                Player tempplayer2 = Bukkit.getPlayer(uuid2);
                                if (tempplayer2 != null) {

                                    if(LastLocation.LastLocation.containsKey(tempplayer) && LastLocation.LastLocation.containsKey(tempplayer2)) {
                                        Boolean before = basicUtils.in_distance(LastLocation.LastLocation.get(tempplayer), LastLocation.LastLocation.get(tempplayer2), 45);
                                        Boolean current = basicUtils.in_distance(tempplayer.getLocation(), tempplayer2.getLocation(), 45);
                                        if(!before && current) {
                                            //spawn
                                            try {
                                                Cache.UUID_ENTITYID.get(tempplayer2.getUniqueId()).spawnfornewplayer(tempplayer);
                                                Cache.UUID_ENTITYID.get(tempplayer2.getUniqueId()).updateName(tempplayer, HordeCore.factionsAPI.getFactionTag_DisplayTarget(tempplayer2, tempplayer));
                                            } catch (InvocationTargetException e) {
                                                e.printStackTrace();
                                            }
                                        } else if(!current) {
                                            //destroy
                                            try {
                                                Cache.UUID_ENTITYID.get(tempplayer2.getUniqueId()).destroy(tempplayer);
                                            } catch (InvocationTargetException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                    if(tempplayer != tempplayer2) {
                                        if(!CacheUtils.is_watching(tempplayer.getUniqueId(), tempplayer2.getUniqueId()) && basicUtils.in_distance(tempplayer.getLocation(), tempplayer2.getLocation(), 43)) {
                                            try {
                                                Cache.UUID_ENTITYID.get(tempplayer2.getUniqueId()).spawnfornewplayer(tempplayer);
                                            } catch (InvocationTargetException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }



                            }
                            tempplayer.playerListName(Component.text("&cPREFIX &7" + tempplayer.getName()));

                            LastLocation.LastLocation.put(tempplayer, tempplayer.getLocation());
                            /*try {
                                Cache.UUID_ENTITYID.get(uuid).destroy(tempplayer);
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }*/
                        }
                    }).start();


                }
            }
        }.runTaskTimerAsynchronously(this, 1L, 1L);
        List<PacketType> PacketTyps_d = new ArrayList<>();
        PacketTyps_d.add(PacketType.Play.Server.ENTITY_TELEPORT);
        PacketTyps_d.add(PacketType.Play.Server.REL_ENTITY_MOVE);
        PacketTyps_d.add(PacketType.Play.Server.REL_ENTITY_MOVE_LOOK);
        List<PacketType> PacketTyps_c = new ArrayList<>();
        PacketTyps_c.add(PacketType.Play.Server.ENTITY_METADATA);
        protocolManager.addPacketListener(new PacketAdapter(this, ListenerPriority.HIGHEST, PacketTyps_d) {
            @Override
            public void onPacketSending(PacketEvent event) {
                PacketType packetType = event.getPacketType();
                if(packetType.equals(PacketType.Play.Server.ENTITY_TELEPORT)
                        || packetType.equals(PacketType.Play.Server.REL_ENTITY_MOVE)
                        || packetType.equals(PacketType.Play.Server.REL_ENTITY_MOVE_LOOK)) {

                    PacketContainer packet = event.getPacket();
                    if(PlayerListener.EntityID_To_Player.containsKey(packet.getIntegers().read(0))) {
                        Player Target = PlayerListener.EntityID_To_Player.get(packet.getIntegers().read(0));
                        double diff = 1.8;
                        if (Target.isSneaking()) {
                            diff = 1.4;
                        } else if (Target.isSwimming()) {
                            diff = 1;
                        }
                        if (packetType.equals(PacketType.Play.Server.ENTITY_TELEPORT)) {
                            Cache.UUID_ENTITYID.get(Target.getUniqueId()).teleport_d(Target.getLocation(), event.getPlayer(), diff);
                        } else {
                            Cache.UUID_ENTITYID.get(Target.getUniqueId()).move(Target, packet.getShorts().read(0), packet.getShorts().read(1), packet.getShorts().read(2));
                        }
                        try {
                            Cache.UUID_ENTITYID.get(Target.getUniqueId()).updateName(event.getPlayer(), factionsAPI.getFactionTag_DisplayTarget(Target, event.getPlayer()));
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                        /*WrapperPlayServerEntityTeleport playertp = new WrapperPlayServerEntityTeleport();
                        playertp.setOnGround(Target.isOnGround());
                        playertp.setEntityID(Target.getEntityId());
                        playertp.setX(Target.getLocation().getX());
                        playertp.setY(Target.getLocation().getY());
                        playertp.setZ(Target.getLocation().getZ());
                        playertp.setPitch(Target.getLocation().getPitch());
                        playertp.setYaw(Target.getLocation().getYaw());
                        playertp.sendPacket(event.getPlayer());*/

                        Boolean current = basicUtils.in_distance(event.getPlayer().getLocation(), Target.getLocation(), 45);
                        /*Boolean before = null;
                        if(LastLocation.LastPacketLocation.containsKey(Target) && LastLocation.LastPacketLocation.containsKey(event.getPlayer())) {
                            before = basicUtils.in_distance(LastLocation.LastPacketLocation.get(event.getPlayer()), LastLocation.LastPacketLocation.get(Target), 40);
                            if(!before && current) {
                                try {

                                } catch (InvocationTargetException e) {
                                    e.printStackTrace();
                                }
                            }
                        }*/
                        if(!current) {
                            try {
                                Cache.UUID_ENTITYID.get(Target.getUniqueId()).destroy(event.getPlayer());
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        } else {
                                /*if(LastLocation.LastPacketLocation.containsKey(Target) && LastLocation.LastPacketLocation.containsKey(event.getPlayer()) && !basicUtils.in_distance(LastLocation.LastPacketLocation.get(event.getPlayer()), LastLocation.LastPacketLocation.get(Target), 40)) {
                                    Cache.UUID_ENTITYID.get(Target.getUniqueId()).spawnfornewplayer(event.getPlayer());
                                }*/
                                //Cache.UUID_ENTITYID.get(Target.getUniqueId()).updateName(event.getPlayer(), factionsAPI.getFactionTag_DisplayTarget(Targe
                        }
                        LastLocation.LastPacketLocation.put(event.getPlayer(), event.getPlayer().getLocation());
                    }

                }
            }
        });
        protocolManager.addPacketListener(new PacketAdapter(this, ListenerPriority.HIGHEST, PacketTyps_c) {
            @Override
            public void onPacketSending(PacketEvent event) {
                if(PlayerListener.EntityID_To_Player.containsKey(event.getPacket().getIntegers().read(0))) {
                    Player Target = PlayerListener.EntityID_To_Player.get(event.getPacket().getIntegers().read(0));
                    double diff = 1.8;
                    if (Target.isSneaking()) {
                        diff = 1.4;
                    } else if (Target.isSwimming()) {
                        diff = 1;
                    }
                    Cache.UUID_ENTITYID.get(Target.getUniqueId()).teleport_d(Target.getLocation(), event.getPlayer(), diff);
                }
            }
        });
    }

    @Deprecated
    @Override
    public void onEnable() {
        instance = this;
        protocolManager = ProtocolLibrary.getProtocolManager();
        if(Bukkit.getPluginManager().isPluginEnabled("Factions")) {

        }
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), (Plugin)HordeCore.getInstance());
        updateArmorStandLocation();
        HideNameTag.setup();
        HideNameTag.startThread();

    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
        new Thread(() -> {
            for(PacketSpawnEntity pse : Cache.UUID_ENTITYID.values()) {
                try {
                    pse.destroy();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
