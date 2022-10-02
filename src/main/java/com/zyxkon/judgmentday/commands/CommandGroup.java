package com.zyxkon.judgmentday.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.HashMap;

public abstract class CommandGroup implements CommandExecutor {
    private final HashMap<String, SubCommand> sCmds = new HashMap<>();
    public boolean execute(CommandSender commandSender, Command command, String s, String[] strings){
        return true;
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 0) return execute(commandSender, command, s, strings);
        String firstArg = strings[0];
        if (sCmds.containsKey(firstArg)){
            return sCmds.get(firstArg).onCommand(
                    commandSender, command, s, Arrays.copyOfRange(strings, 1, strings.length)
            );
        }
        return true;
    }
    public void addCommand(SubCommand command){
        sCmds.put(command.getName(), command);
    }
}
