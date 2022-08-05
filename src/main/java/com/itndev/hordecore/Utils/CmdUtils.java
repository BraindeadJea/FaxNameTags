package com.itndev.hordecore.Utils;

import com.itndev.hordecore.HordeCore;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;

import java.util.Objects;

public class CmdUtils {

    public static void registercmd(String cmd, CommandExecutor cmdclass) {
        ((PluginCommand) Objects.<PluginCommand>requireNonNull(HordeCore.getInstance().getCommand(cmd))).setExecutor(cmdclass);
    }
}
