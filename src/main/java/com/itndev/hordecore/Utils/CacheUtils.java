package com.itndev.hordecore.Utils;

import com.itndev.hordecore.NameTag.Cache;

import java.util.ArrayList;
import java.util.UUID;

public class CacheUtils {

    public static Boolean is_watching(UUID watcher, UUID object) {
        if(!Cache.PLAYERS_LOOKING_AT.containsKey(watcher)) {
            return false;
        }
        return Cache.PLAYERS_LOOKING_AT.get(watcher).contains(object);
    }

    public static void add_watched_object(UUID watcher, UUID object) {
        ArrayList<UUID> watching_objects;
        if(Cache.PLAYERS_LOOKING_AT.containsKey(watcher)) {
            watching_objects = Cache.PLAYERS_LOOKING_AT.get(watcher);
        } else {
            watching_objects = new ArrayList<>();
        }
        watching_objects.add(object);
        Cache.PLAYERS_LOOKING_AT.put(watcher, watching_objects);
    }

    public static void remove_watcher(UUID watcher) {
        if(!Cache.PLAYERS_LOOKING_AT.containsKey(watcher)) {
            return;
        }
        Cache.PLAYERS_LOOKING_AT.remove(watcher);
    }

    public static void remove_watched_object(UUID watcher, UUID object) {
        if(!Cache.PLAYERS_LOOKING_AT.containsKey(watcher)) {
            return;
        }
        ArrayList<UUID> watching_objects = Cache.PLAYERS_LOOKING_AT.get(watcher);
        watching_objects.remove(object);
        Cache.PLAYERS_LOOKING_AT.put(watcher, watching_objects);
    }

    public static void remove_watched_object_all(ArrayList<UUID> watchers, UUID object) {
        watchers.forEach(watcher -> remove_watched_object(watcher, object));
    }
}
