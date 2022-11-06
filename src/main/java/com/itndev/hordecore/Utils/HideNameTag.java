package com.itndev.hordecore.Utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class HideNameTag {

    private static Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();

    private static Team defaultTeam = board.registerNewTeam("defaultHideTeam");

    public static void setup() {
        defaultTeam.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
    }

    public static void hideName(Player p) {
        defaultTeam.addEntry(p.getName());
        p.setScoreboard(board);
    }
}
