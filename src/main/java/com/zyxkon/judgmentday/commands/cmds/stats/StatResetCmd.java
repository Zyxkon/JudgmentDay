package com.zyxkon.judgmentday.commands.cmds.stats;

import com.zyxkon.judgmentday.Counter;
import com.zyxkon.judgmentday.Main;
import com.zyxkon.judgmentday.commands.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StatResetCmd extends SubCommand {
    static Main plugin;
    private final static String cmdName = "reset";
    public StatResetCmd(Main plugin){
        StatResetCmd.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player p;
        try {
            p = Bukkit.getPlayer(strings[0]);
        } catch (IndexOutOfBoundsException exception) {
            try {
                p = (Player) commandSender;
            } catch (ClassCastException e){
                commandSender.sendMessage("You are not a player and you have no stats!");
                return true;
            }
        }
        Counter.resetStats(p);
        p.sendMessage("Your stats have been reset!");
        commandSender.sendMessage(String.format("%s's stats have been reset!", p.getName()));
        return true;
    }
    public String getName(){
        return cmdName;
    }
}
