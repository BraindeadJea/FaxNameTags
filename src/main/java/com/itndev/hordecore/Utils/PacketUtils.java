package com.itndev.hordecore.Utils;

import com.comphenix.protocol.events.PacketContainer;
import com.itndev.hordecore.HordeCore;
import com.itndev.hordecore.Wrappers.WrapperPlayServerEntityTeleport;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class PacketUtils {

    public static void broadcastpacketA(PacketContainer packet, Player nosend) {
        for(Player Online : Bukkit.getOnlinePlayers()) {
            if(!Online.equals(nosend)) {
                try {
                    HordeCore.protocolManager.sendServerPacket(Online, packet);
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void broadcastpacketA_TP(WrapperPlayServerEntityTeleport packet, Player nosend) {
        for(Player Online : Bukkit.getOnlinePlayers()) {
            if(!Online.equals(nosend)) {
                packet.sendPacket(Online);
            }
        }
    }

    public static void broadcastpacketB(PacketContainer packet, ArrayList<Player> nosend) {
        for(Player Online : Bukkit.getOnlinePlayers()) {
            if(!nosend.contains(Online)) {
                try {
                    HordeCore.protocolManager.sendServerPacket(Online, packet);
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
