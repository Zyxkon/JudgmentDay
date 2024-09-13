package com.zyxkon.judgmentday.commands;

import com.zyxkon.judgmentday.CommandType;
import com.zyxkon.judgmentday.Main;
import com.zyxkon.judgmentday.SubcommandType;
import com.zyxkon.judgmentday.Utils;
import com.zyxkon.judgmentday.commands.types.InjuryCommand;
import com.zyxkon.judgmentday.commands.types.StatsCommand;
import com.zyxkon.judgmentday.commands.types.ThirstCommand;
import com.zyxkon.judgmentday.extensions.WorldGuardExtension;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.logging.Level;


public class Commands implements CommandExecutor {
    static Main plugin;
    static String cmdName;
    public Commands(Main plugin){
        Commands.plugin = plugin;
        Commands.cmdName = plugin.getName();
        plugin.getCommand(cmdName).setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        for (int i = 0;i < strings.length; i++){
            Main.log(Level.INFO, String.format("%d %s", i, strings[i]));
        }
        if (strings.length == 0){
            String space = new String(new char[6]).replace("\0", "-");
            String msg = Utils.group("\n",
                    space+plugin.getName()+space,
                    String.format("Author: %s", String.join(", ",plugin.getDescription().getAuthors())),
                    String.format("Version: %s", plugin.getDescription().getVersion()),
                    String.format("Aliases: %s", String.join(", ", command.getAliases()))
            );
            commandSender.sendMessage(Utils.translate(msg));
            return true;
        }
        // Arrays.copyOfRange doesn't copy the element at the ending index, strings.length lies outside the boundary
        // of the `strings` array
        String[] args = Arrays.copyOfRange(strings, 1, strings.length);
//        commandSender.sendMessage("Args:\n");
//        Arrays.asList(args).forEach(commandSender::sendMessage);
        HashMap<String, Callable<Boolean>> commands = new HashMap<>();
        commands.put(CommandType.STATS.name(),
                () -> stats(commandSender, args)
        );
        commands.put(CommandType.THIRST.name(),
                () -> thirst(commandSender, args)
        );
        commands.put(CommandType.INJURY.name(),
                () -> injury(commandSender, args)
        );
        for (String str : commands.keySet()){
            if (Utils.equatesTo(strings[0].toUpperCase(), str)){
                try {
                    return commands.get(str).call();
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
        switch (strings[0].toLowerCase()) {
            // having variable i obviates the need to remember what arg you are on: i+1 becomes the next arg instead
            // in the above statement, i is still = 0, i++ technically increases i by one the next time its used, but
            // not in the first time
            case "r": case "regions":{
                if (Bukkit.getPluginManager().getPlugin("WorldGuard") == null){
                    commandSender.sendMessage("WorldGuard isn't installed, you can't use this subcommand!");
                    return true;
                }
                switch (strings[1].toLowerCase()){
                    case "a": case "add": {
                        switch (strings[2].toLowerCase()){
                            case "sz": case "safezone":{
                                String region = strings[3];
                                if (!WorldGuardExtension.regionExists(region)){
                                    commandSender.sendMessage(String.format("Region '%s' does not exist!", region));
                                    return true;
                                }
                                if (WorldGuardExtension.isSafezone(region)) {
                                    commandSender.sendMessage(String.format("Region '%s' is already a safezone!", region));
                                    return true;
                                }
                                ArrayList<String> safezones = new ArrayList<>(WorldGuardExtension.getRegionsConfig().getStringList("safezones"));
                                safezones.add(region);
                                WorldGuardExtension.getRegionsConfig().set("safezones", safezones);
                                try{
                                    WorldGuardExtension.getRegionsConfig().save(WorldGuardExtension.getRegionsFile());
                                    commandSender.sendMessage(Utils.translate(String.format("Region '%s' has &asuccessfully&f been saved as a safezone!", region)));
                                } catch (IOException exception) {
                                    exception.printStackTrace();
                                    commandSender.sendMessage(Utils.translate("An &cerror&f has occurred!"));
                                }
                                return true;
                            }
                            case "br": case "barracks": {
                                String region = strings[3];
                                if (!WorldGuardExtension.regionExists(region)){
                                    commandSender.sendMessage(String.format("Region '%s' does not exist!", region));
                                    return true;
                                }
                                if (WorldGuardExtension.isBarracks(region)) {
                                    commandSender.sendMessage(String.format("Region '%s' is already a barracks!", region));
                                    return true;
                                }
                                ArrayList<String> barracks = new ArrayList<>(WorldGuardExtension.getRegionsConfig().getStringList("barracks"));
                                barracks.add(region);
                                WorldGuardExtension.getRegionsConfig().set("barracks", barracks);
                                try{
                                    WorldGuardExtension.getRegionsConfig().save(WorldGuardExtension.getRegionsFile());
                                    commandSender.sendMessage(Utils.translate(String.format(
                                            "Region '%s' has &asuccessfully&f been saved as a barracks!", region)));
                                } catch (IOException exception) {
                                    exception.printStackTrace();
                                    commandSender.sendMessage(Utils.translate("An &cerror&f has occurred!"));
                                }
                                return true;
                            }
                        }
                        break;
                    }
                    case "r": case "remove": {
                        switch (strings[2].toLowerCase()){
                            case "sz": case "safezone":{
                                String region = strings[3];
                                ArrayList<String> safezones = new ArrayList<>(WorldGuardExtension.getRegionsConfig().getStringList("safezones"));
                                if (!WorldGuardExtension.isSafezone(region)){
                                    commandSender.sendMessage(String.format("Region '%s' is not a safezone!", region));
                                    return true;
                                }
                                safezones.remove(region);
                                WorldGuardExtension.getRegionsConfig().set("safezones", safezones);
                                try{
                                    WorldGuardExtension.getRegionsConfig().save(WorldGuardExtension.getRegionsFile());
                                    commandSender.sendMessage(Utils.translate(String.format("Safezone '%s' has been removed!", region)));
                                } catch (IOException exception) {
                                    exception.printStackTrace();
                                    commandSender.sendMessage(Utils.translate("An &cerror&f has occurred!"));
                                }
                                return true;
                            }
                            case "br": case "barracks":{
                                String region = strings[3];
                                ArrayList<String> barracks = new ArrayList<>(WorldGuardExtension.getRegionsConfig().getStringList("barracks"));
                                if (!WorldGuardExtension.isBarracks(region)){
                                    commandSender.sendMessage(String.format("Region '%s' is not a barracks!", region));
                                    return true;
                                }
                                barracks.remove(region);
                                WorldGuardExtension.getRegionsConfig().set("barracks", barracks);
                                try{
                                    WorldGuardExtension.getRegionsConfig().save(WorldGuardExtension.getRegionsFile());
                                    commandSender.sendMessage(Utils.translate(String.format("Barracks '%s' has been removed!", region)));
                                } catch (IOException exception) {
                                    exception.printStackTrace();
                                    commandSender.sendMessage(Utils.translate("An &cerror&f has occurred!"));
                                }
                                return true;
                            }
                        }
                        break;
                    }
                    case "g": case "get":{
                        switch (strings[2].toLowerCase()){
                            case "sz":case "safezone": {
                                ArrayList<String> safezones = WorldGuardExtension.getSafezones();
                                commandSender.sendMessage(String.join("\n", safezones));
                                return true;
                            }
                            case "br" :case "barracks":{
                                ArrayList<String> barracks = WorldGuardExtension.getBarracks();
                                commandSender.sendMessage(String.join("\n", barracks));
                                return true;
                            }
                        }
                    }
                }
                break;
            }
            case "c": case "cfg": case "config":{
                switch (strings[1].toLowerCase()){
                    case "rl": case "reload": {
                        WorldGuardExtension.reload();
                        commandSender.sendMessage(Utils.translate(String.format(
                                "The configuration of plugin %s has been reloaded!", plugin.getName())));
                        return true;
                    }
                }
                break;
            }
        }
        Main.log(Level.INFO, String.format("couldn't find any case for strings[0] which is %s, returning false", strings[0]));
        return false;
    }
    public static boolean stats(CommandSender sender, String[] args) {
        StatsCommand jdCmd = new StatsCommand(sender);
        return process(jdCmd, args);
    }
    public static boolean thirst(CommandSender sender, String[] args){
        ThirstCommand jdCmd = new ThirstCommand(sender);
        return process(jdCmd, args);
    }
    public static boolean injury(CommandSender sender, String[] args){
        InjuryCommand jdCmd = new InjuryCommand(sender);
        return process(jdCmd, args);
    }


    public static boolean process(JDCommand jdCmd, String[] args){
        CommandSender sender = jdCmd.sender;
        String u = jdCmd.getCommandType().getUsage();
        Main.log(Level.INFO, "COMMAND: "+jdCmd.getCommandType().name());
        if (args.length == 0){
            sender.sendMessage("WRONG ARGUMENTS! NO ARGUMENTS!");
            Utils.sendMultilineMessage(sender, u);
            return false;
        }
        int i = 0;
        for (SubcommandType sub : jdCmd.getCommandType().getSubcommands()) {
            String arg = args[i].toUpperCase();
            if (Utils.equatesTo(arg, sub.getSimpleName())){
                try {
                    return jdCmd.applySubcommand(arg, Arrays.copyOfRange(args, i + 1, args.length));
                } catch (IndexOutOfBoundsException e){
                    sender.sendMessage("INDEXOUTOFBOUNDS!");
                    Utils.sendMultilineMessage(sender, u);
                    return false;
                }
            }
        }
        sender.sendMessage("WHAT DID YOU JUST WRITE!?");
        Utils.sendMultilineMessage(sender, u);
        return false;
    }
}


