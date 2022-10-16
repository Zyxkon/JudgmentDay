package com.zyxkon.judgmentday.commands.cmds.stats;

import com.zyxkon.judgmentday.Counter;
import com.zyxkon.judgmentday.Main;
import com.zyxkon.judgmentday.Utils;
import com.zyxkon.judgmentday.commands.CommandGroup;
import com.zyxkon.judgmentday.commands.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;

public class MainStatCmd extends CommandGroup {
    static Main plugin;
    private final HashMap<String, SubCommand> sCmds = new HashMap<>();
    private final static String name = "stats";
    public MainStatCmd(Main plugin){
        MainStatCmd.plugin = plugin;
        this.addCommand(new StatResetCmd(plugin));
        this.addCommand(new StatGetCmd(plugin));
        plugin.getCommand(plugin.getCommandName()).setExecutor(this);
    }
    @Override
    public boolean execute(CommandSender commandSender, Command command, String s, String[] strings){
        if (!(commandSender instanceof Player)){
            commandSender.sendMessage("You are not a player so you don't have any stats!");
            return true;
        }
        Player p = (Player) commandSender;
        commandSender.sendMessage(Utils.group("\n",
                String.format("%s has:", p.getName()),
                String.format("   Killed: %d walkers", Counter.getWalkerKills(p)),
                String.format("   Murdered: %d players", Counter.getPlayerKills(p)),
                String.format("   Died: %d times", Counter.getDeaths(p))
        ));
        return true;
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!check(strings)) return true;
        strings = Arrays.copyOfRange(strings, 1, strings.length);
        if (strings.length == 0) return execute(commandSender, command, s, strings);
        return super.onCommand(sCmds, commandSender, command, s, strings);
    }
    @Override public void addCommand(SubCommand command){
        sCmds.put(command.getName(), command);
    }
    @Override public String getName() {
        return name;
    }
    @Override public boolean check(String[] strings){
        return strings[0].equals(name);
    }
}
