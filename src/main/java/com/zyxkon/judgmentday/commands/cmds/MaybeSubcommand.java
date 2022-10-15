package com.zyxkon.judgmentday.commands.cmds;

import com.zyxkon.judgmentday.Main;
import com.zyxkon.judgmentday.commands.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MaybeSubcommand extends SubCommand {
    static Main plugin;
    private final static String cmdName = "kingbreaker";
    public MaybeSubcommand(Main plugin){
        MaybeSubcommand.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        commandSender.sendMessage("hello! this is ur friendly neighborhood maybe subcmd, the command name is "+cmdName+" btw");
        Player p = Bukkit.getPlayer(strings[0]);
        if (p == null){
            commandSender.sendMessage("yea...no player found, just telling u that");
            return true;
        }
        commandSender.sendMessage("oops, player found! his name is "+strings[0]);
        p.sendMessage("hey dumbass someone wants to speak to you");
        return true;
    }
    public String getName(){
        return cmdName;
    }
}
