package com.itndev.hordecore.NameTag;

import com.itndev.hordecore.HordeCore;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class UpdateNames {

    public static void run() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for(UUID uuid : Cache.UUID_ENTITYID.keySet()) {
                    //Cache.UUID_ENTITYID.get(uuid).updateOnline();
                }
            }
        }.runTaskTimerAsynchronously(HordeCore.getInstance(), 1L ,1L);
    }
}
