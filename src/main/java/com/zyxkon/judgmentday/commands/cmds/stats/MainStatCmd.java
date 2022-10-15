package com.zyxkon.judgmentday.commands.cmds.stats;

import com.zyxkon.judgmentday.Main;
import com.zyxkon.judgmentday.commands.CommandGroup;
import com.zyxkon.judgmentday.commands.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

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
        commandSender.sendMessage("yea sure");
        return true;
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!check(strings)) return true;
        commandSender.sendMessage("why?");
        return super.onCommand(sCmds, commandSender, command, s, Arrays.copyOfRange(strings, 1, strings.length));
    }
    public void addCommand(SubCommand command){
        sCmds.put(command.getName(), command);
    }
    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean check(String[] strings) {
        return strings[0].equals(name);
    }
}
