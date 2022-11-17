package com.itndev.hordecore.Utils;

import com.itndev.hordecore.HordeCore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

public class HideNameTag {

    private static ScoreboardManager manager = Bukkit.getScoreboardManager();

    private static Scoreboard board = manager.getNewScoreboard();

    private static Team defaultTeam = board.registerNewTeam("defaultHideTeam");

    public static void setup() {
        defaultTeam.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
    }

    public static void startThread() {
        new BukkitRunnable() {

            @Override
            public void run() {
                defaultTeam.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
                Bukkit.getOnlinePlayers().forEach(player -> {
                    defaultTeam.addEntry(player.getName());
                    player.setScoreboard(board);
                });
            }
        }.runTaskTimer(HordeCore.getInstance(), 20L, 20L);

    }


}
