package com.zyxkon.judgmentday.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.HashMap;

public abstract class CommandGroup implements CommandExecutor {
    public abstract boolean execute(CommandSender commandSender, Command command, String s, String[] strings);

    //    @Override
    public boolean onCommand(HashMap<String, SubCommand> hash, CommandSender commandSender, Command command, String s, String[] strings) {
//        if (strings.length == 0) return execute(commandSender, command, s, strings);
        String firstArg = strings[0];
        if (hash.containsKey(firstArg)){
            return hash.get(firstArg).onCommand(
                    commandSender, command, s, Arrays.copyOfRange(strings, 1, strings.length)
            );
        }
        return true;
    }
    public abstract void addCommand(SubCommand command);
    public abstract String getName();
    public abstract boolean check(String[] strings);
}
