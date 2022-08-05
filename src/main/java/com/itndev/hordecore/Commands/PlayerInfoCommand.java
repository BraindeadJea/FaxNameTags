package com.itndev.hordecore.Commands;

import com.itndev.hordecore.Utils.CmdUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerInfoCommand implements CommandExecutor {

    public static void Register() {
        CmdUtils.registercmd("정보", new PlayerInfoCommand());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {

        } else {
           //sender.sendMessage();
        }
        return false;
    }
}