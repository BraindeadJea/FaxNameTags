package com.itndev.hordecore.NameTag;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.accessors.Accessors;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.itndev.hordecore.HordeCore;
import com.itndev.hordecore.Utils.BasicUtils;
import com.itndev.hordecore.Utils.CacheUtils;
import com.itndev.hordecore.Utils.PacketUtils;
import com.itndev.hordecore.Wrappers.WrapperPlayServerEntityDestroy;
import com.itndev.hordecore.Wrappers.WrapperPlayServerEntityTeleport;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import javax.swing.text.html.parser.Entity;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PacketSpawnEntity {


    public Player this_player;
    public int this_id;
    public UUID this_uuid;
    public String this_name = "";
    public UUID this_real_uuid;


    public PacketSpawnEntity(Player p) {
        this_player = p;
        int entityIDNew = BasicUtils.getNewID();
        UUID entityUUIDNew = UUID.randomUUID();
        this_id = entityIDNew;
        this_uuid = entityUUIDNew;
        this_real_uuid = p.getUniqueId();
    }

    public void spawn(String name) {

        this_name = name;

        PacketContainer packet = HordeCore.protocolManager.createPacket(PacketType.Play.Server.SPAWN_ENTITY);
        packet.getIntegers().write(0, this_id);
        packet.getUUIDs().write(0, this_uuid);
        packet.getIntegers().write(1, 1);
        packet.getDoubles().write(0, this_player.getLocation().getX());
        packet.getDoubles().write(1, this_player.getLocation().getY() + 1.8);
        packet.getDoubles().write(2, this_player.getLocation().getZ());
        packet.getEntityTypeModifier().write(0, EntityType.ARMOR_STAND);
        PacketUtils.broadcastpacketA(packet, this_player);
        //HordeCore.protocolManager.broadcastServerPacket(packet);
        new Thread(() -> {
            updateOnline(name);
        }).start();
        for(Player p : Bukkit.getOnlinePlayers()) {
            if(p.getUniqueId() != this_real_uuid) {
                CacheUtils.add_watched_object(p.getUniqueId(), this_real_uuid);
            }
        }
    }

    public void spawn_a(String name, Location loc) {
        this_name = name;
        PacketContainer packet = HordeCore.protocolManager.createPacket(PacketType.Play.Server.SPAWN_ENTITY);
        packet.getIntegers().write(0, this_id);
        packet.getUUIDs().write(0, this_uuid);
        packet.getIntegers().write(1, 1);
        packet.getDoubles().write(0, loc.getX());
        packet.getDoubles().write(1, loc.getY() + 1.8);
        packet.getDoubles().write(2, loc.getZ());
        packet.getEntityTypeModifier().write(0, EntityType.ARMOR_STAND);
        PacketUtils.broadcastpacketA(packet, this_player);

        new Thread(() -> {
            updateOnline(name);
        }).start();
        for(Player p : Bukkit.getOnlinePlayers()) {
            if(p.getUniqueId() != this_real_uuid) {
                CacheUtils.add_watched_object(p.getUniqueId(), this_real_uuid);
            }
        }
    }

    public void editName(String name) {
        this_name = name;
        updateOnline(name);
    }

    public void updateOnline(String name) {
        for(Player Online : Bukkit.getOnlinePlayers()) {
            try {
                String finalname = HordeCore.factionsAPI.getFactionTag_DisplayTarget(this_player, Online);
                updateName(Online, finalname);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

        }
    }

    public void spawnfornewplayer(Player Target) throws InvocationTargetException {
        if(this_player == Target) {
            return;
        }
        PacketContainer packet = HordeCore.protocolManager.createPacket(PacketType.Play.Server.SPAWN_ENTITY);
        packet.getIntegers().write(0, this_id);
        packet.getUUIDs().write(0, this_uuid);
        packet.getIntegers().write(1, 1);
        packet.getDoubles().write(0, this_player.getLocation().getX());
        packet.getDoubles().write(1, this_player.getLocation().getY() + 1.8);
        packet.getDoubles().write(2, this_player.getLocation().getZ());
        packet.getEntityTypeModifier().write(0, EntityType.ARMOR_STAND);
        HordeCore.protocolManager.sendServerPacket(Target, packet);
        updateName(Target, HordeCore.factionsAPI.getFactionTag_DisplayTarget(this_player, Target));
        CacheUtils.add_watched_object(Target.getUniqueId(), this_real_uuid);
    }

    public void updateName(Player Target, String name) throws InvocationTargetException {
        Boolean isVisible = true;
        if(name == "") {
            isVisible = false;
        }
        PacketContainer packet = HordeCore.protocolManager.createPacket(PacketType.Play.Server.ENTITY_METADATA);

        WrappedChatComponent wrappedChatComponent = WrappedChatComponent.fromText(HordeCore.basicUtils.colorize(name));
        //Optional<WrappedChatComponent> opt = Optional.of(wrappedChatComponent);

        Optional<?> opt = Optional.of(WrappedChatComponent.fromChatMessage(HordeCore.basicUtils.colorize(name))[0].getHandle());
        WrappedDataWatcher metadata = new WrappedDataWatcher();
        metadata.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(0, WrappedDataWatcher.Registry.get(Byte.class)), (byte) 0x20); //invisible
        metadata.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(2,WrappedDataWatcher.Registry.getChatComponentSerializer(true)), opt);
        //metadata.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(2, WrappedDataWatcher.Registry.getChatComponentSerializer(true)), opt);
        metadata.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(3, WrappedDataWatcher.Registry.get(Boolean.class)), isVisible);
        metadata.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(5, WrappedDataWatcher.Registry.get(Boolean.class)), true);
        metadata.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(15, WrappedDataWatcher.Registry.get(Byte.class)), (byte) (0x01 | 0x08 | 0x10)); //isSmall, noBasePlate, set Marker
        packet.getIntegers().write(0, this_id);
        packet.getWatchableCollectionModifier().write(0, metadata.getWatchableObjects());

        HordeCore.protocolManager.sendServerPacket(Target, packet);
    }


    public void setPassenger() {
        PacketContainer packet = HordeCore.protocolManager.createPacket(PacketType.Play.Server.MOUNT);
        packet.getIntegers().write(0, this_player.getEntityId());
        packet.getIntegers().write(1, 1);
        packet.getModifier().write(0, new int[]{this_id});
    }

    public void teleport(Location loc) {
        WrapperPlayServerEntityTeleport teleportpacket = new WrapperPlayServerEntityTeleport();
        teleportpacket.setEntityID(this_id);
        teleportpacket.setOnGround(false);
        teleportpacket.setX(loc.getX());
        teleportpacket.setY(loc.getY() + 1.7);
        teleportpacket.setZ(loc.getZ());
        teleportpacket.setYaw(0);
        teleportpacket.setPitch(0);
        teleportpacket.broadcastPacket();
        //System.out.println("DEBUG=" + this_id + "/" + loc.getX() + "/" + loc.getY() + "/" + loc.getZ());

        /*PacketContainer packet = HordeCore.protocolManager.createPacket(PacketType.Play.Server.ENTITY_TELEPORT);
        packet.getIntegers().write(0, this_id);
        packet.getDoubles().write(0, loc.getX());
        packet.getDoubles().write(1, loc.getY());
        packet.getDoubles().write(2, loc.getZ());
        packet.getBooleans().write(0, false);
        packet.getBytes().write(0, (byte)0);
        HordeCore.protocolManager.broadcastServerPacket(t);*/
    }

    public void teleport_t(Location loc, Player Target) {
        WrapperPlayServerEntityTeleport teleportpacket = new WrapperPlayServerEntityTeleport();
        teleportpacket.setEntityID(this_id);
        teleportpacket.setOnGround(false);
        teleportpacket.setX(loc.getX());
        teleportpacket.setY(loc.getY() + 1.7);
        teleportpacket.setZ(loc.getZ());
        teleportpacket.setYaw(0);
        teleportpacket.setPitch(0);
        teleportpacket.sendPacket(Target);
        //System.out.println("DEBUG=" + this_id + "/" + loc.getX() + "/" + loc.getY() + "/" + loc.getZ());

        /*PacketContainer packet = HordeCore.protocolManager.createPacket(PacketType.Play.Server.ENTITY_TELEPORT);
        packet.getIntegers().write(0, this_id);
        packet.getDoubles().write(0, loc.getX());
        packet.getDoubles().write(1, loc.getY());
        packet.getDoubles().write(2, loc.getZ());
        packet.getBooleans().write(0, false);
        packet.getBytes().write(0, (byte)0);
        HordeCore.protocolManager.broadcastServerPacket(t);*/
    }

    public void teleport_d(Location loc, Player Target, double diff) {
        WrapperPlayServerEntityTeleport teleportpacket = new WrapperPlayServerEntityTeleport();
        teleportpacket.setEntityID(this_id);
        teleportpacket.setOnGround(false);
        teleportpacket.setX(loc.getX());
        teleportpacket.setY(loc.getY() + diff);
        teleportpacket.setZ(loc.getZ());
        teleportpacket.setYaw(0);
        teleportpacket.setPitch(0);
        teleportpacket.sendPacket(Target);
        //System.out.println("DEBUG=" + this_id + "/" + loc.getX() + "/" + loc.getY() + "/" + loc.getZ());

        /*PacketContainer packet = HordeCore.protocolManager.createPacket(PacketType.Play.Server.ENTITY_TELEPORT);
        packet.getIntegers().write(0, this_id);
        packet.getDoubles().write(0, loc.getX());
        packet.getDoubles().write(1, loc.getY());
        packet.getDoubles().write(2, loc.getZ());
        packet.getBooleans().write(0, false);
        packet.getBytes().write(0, (byte)0);
        HordeCore.protocolManager.broadcastServerPacket(t);*/
    }

    public void destroy() throws InvocationTargetException {
        /*WrapperPlayServerEntityDestroy destroyentity = new WrapperPlayServerEntityDestroy();
        int[] c = new int[1];
        c[0] = this_id;
        destroyentity.setEntityIds(c);
        destroyentity.broadcastPacket();*/
        List<Integer> idlist = new ArrayList<>();
        idlist.add(this_id);
        PacketContainer packet = HordeCore.protocolManager.createPacket(PacketType.Play.Server.ENTITY_DESTROY);
        packet.getIntLists().write(0, idlist);
        HordeCore.protocolManager.broadcastServerPacket(packet);
        //Bukkit.getOnlinePlayers().forEach(player -> CacheUtils.remove_watched_object(player.getUniqueId(), this_uuid));
    }

    public void destroy(Player Target) throws InvocationTargetException {
        /*WrapperPlayServerEntityDestroy destroyentity = new WrapperPlayServerEntityDestroy();
        int[] c = new int[1];
        c[0] = this_id;
        destroyentity.setEntityIds(c);
        destroyentity.broadcastPacket();*/
        List<Integer> idlist = new ArrayList<>();
        idlist.add(this_id);
        PacketContainer packet = HordeCore.protocolManager.createPacket(PacketType.Play.Server.ENTITY_DESTROY);
        packet.getIntLists().write(0, idlist);
        CacheUtils.remove_watched_object(Target.getUniqueId(), this_real_uuid);
        HordeCore.protocolManager.sendServerPacket(Target, packet);
    }

    public Integer getID() {
        return this_id;
    }
}
