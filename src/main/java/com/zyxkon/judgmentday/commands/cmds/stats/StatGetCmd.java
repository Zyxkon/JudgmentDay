package com.zyxkon.judgmentday.commands.cmds.stats;

import com.zyxkon.judgmentday.Counter;
import com.zyxkon.judgmentday.Main;
import com.zyxkon.judgmentday.Utils;
import com.zyxkon.judgmentday.commands.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StatGetCmd extends SubCommand {
    static Main plugin;
    private final static String cmdName = "get";
    public StatGetCmd(Main plugin){
        StatGetCmd.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player p = Bukkit.getPlayer(strings[0]);
        if (p == null) {
            commandSender.sendMessage(String.format("Player '%s' not found!", strings[0]));
            return true;
        }
        commandSender.sendMessage(Utils.group("\n",
                String.format("%s has:", p.getName()),
                String.format("   Killed: %d walkers", Counter.getWalkerKills(p)),
                String.format("   Murdered: %d players", Counter.getPlayerKills(p)),
                String.format("   Died: %d times", Counter.getDeaths(p))
        ));
        return true;
    }
    public String getName(){
        return cmdName;
    }
}