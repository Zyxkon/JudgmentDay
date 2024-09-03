package com.zyxkon.judgmentday;

import com.zyxkon.judgmentday.commands.InjuryCommand;
import com.zyxkon.judgmentday.commands.StatsCommand;
import com.zyxkon.judgmentday.commands.ThirstCommand;
import com.zyxkon.judgmentday.extensions.WorldGuardExtension;
import com.zyxkon.judgmentday.injuries.Injury;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

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
            plugin.log(Level.INFO, String.format("%d %s", i, strings[i]));
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
        commandSender.sendMessage("Args:\n");
        Arrays.asList(args).forEach(commandSender::sendMessage);
        if (Utils.equatesTo(strings[0].toLowerCase(), "stats")) {
            return stats(commandSender, args);
        }
        else if (Utils.equatesTo(strings[0].toLowerCase(), "thirst")) {
            return thirst(commandSender, args);
        }
        else if (Utils.equatesTo(strings[0].toLowerCase(), "injury")) {
            return injury(commandSender, args);
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
        plugin.log(Level.INFO, String.format("couldn't find any case for strings[0] which is %s, returning false", strings[0]));
        return false;
    }
    public static boolean stats(CommandSender sender, String[] args) {
        StatsCommand statsCmd = new StatsCommand(sender);
        ArrayList <String> subCmds = new ArrayList<>();
        for (StatsCommand.SUBCOMMAND sub : StatsCommand.SUBCOMMAND.values()) {
            subCmds.add(sub.name().toLowerCase());
        }
        String usage = String.format(
                "/%s %s (%s) [name]",
                plugin.getName(), "stats", String.join("/", subCmds)
        );
        if (args.length == 0){
            sender.sendMessage("No arguments provided. Usage:"+usage);
            return false;
        }
        if (Utils.equatesTo(args[0], StatsCommand.SUBCOMMAND.RESET.toString().toLowerCase())) {
            try {
                // if there is a subject to reset the stats of
                return statsCmd.reset(Bukkit.getPlayer(args[1]));
            } catch (IndexOutOfBoundsException exc) {
                // if there is no subject to reset the stats of
                return statsCmd.reset();
            }
        } else if (Utils.equatesTo(args[0], StatsCommand.SUBCOMMAND.GET.toString().toLowerCase())) {
            try {
                return statsCmd.get(Bukkit.getPlayer(args[1]));
            } catch (IndexOutOfBoundsException exc) {
                // no subject found
                return statsCmd.get();
            }
        }
        sender.sendMessage(String.format("Illegal arguments. Usage: /%s (get/reset) [name]", cmdName));
        return false;
    }
    public static boolean thirst(CommandSender sender, String[] args){
        String cmd = "Thirst";
        List<ThirstCommand.SUBCOMMAND> sCmds = Arrays.asList(ThirstCommand.SUBCOMMAND.values());
        String usage = String.format(
                // /jd thirst (set/get) (thirst) [name]
                "/%s %s (%s) (thirst) [name]",
                cmdName, cmd, sCmds.stream().map(s ->
                        s.toString().toLowerCase()).collect(Collectors.joining("/"))
        );
        ThirstCommand thirstCmd = new ThirstCommand(sender);
        if (args.length == 0){
            sender.sendMessage("Insufficient arguments. Usage: "+usage);
            return false;
        }
        if (Utils.equatesTo(args[0].toLowerCase(),
                ThirstCommand.SUBCOMMAND.GET.name().toLowerCase())) {
            try {
                // if there is a subject to reset the stats of
                return thirstCmd.get(Bukkit.getPlayer(args[1]));
            } catch (IndexOutOfBoundsException exc) {
                // if there is no subject to reset the stats of
                return thirstCmd.get();
            }
        } else if (Utils.equatesTo(args[0].toLowerCase(),
                ThirstCommand.SUBCOMMAND.SET.name().toLowerCase())) {
            try {
                return thirstCmd.set(Bukkit.getPlayer(args[2]), Integer.parseInt(args[1]));
            } catch (IndexOutOfBoundsException exc) {
                // no subject found
                return thirstCmd.set(Integer.parseInt(args[1]));
            }
        }
        else if  (Utils.equatesTo(args[0].toLowerCase(),
                ThirstCommand.SUBCOMMAND.LIST.name().toLowerCase())){
            return thirstCmd.sendThirstList();
        }
        sender.sendMessage("Illegal arguments. Usage:"+usage);
        return false;
    }
    public static boolean injury(CommandSender sender, String[] args){
        String cmd = "injury";
        List<InjuryCommand.SUBCOMMAND> sCmds = Arrays.asList(InjuryCommand.SUBCOMMAND.values());
        String usage = String.format(
                "/%s %s (%s) (%s) [name]",
                cmdName, cmd, sCmds.stream().map(s ->
                s.toString().toLowerCase()).collect(Collectors.joining("/")),
                Arrays.stream(Injury.INJURIES.values()).map(s ->
                        s.toString().toLowerCase()).collect(Collectors.joining("/"))
        );
        InjuryCommand injuryCmd = new InjuryCommand(sender);
        if (args.length == 0){
            sender.sendMessage(
                    "Not enough arguments. Usage:"+usage
            );
            return false;
        }
        if (Utils.equatesTo(args[0].toLowerCase(),
                InjuryCommand.SUBCOMMAND.CHECK.toString().toLowerCase())) {
            try {
                // if there is a subject
                return injuryCmd.check(Bukkit.getPlayer(args[1]));
            } catch (IndexOutOfBoundsException exc) {
                // if there is no subject
                return injuryCmd.check();
            }
        }
        else if (Utils.equatesTo(args[0].toLowerCase(),
                InjuryCommand.SUBCOMMAND.HEAL.toString().toLowerCase())){
            try {
                return injuryCmd.heal(Bukkit.getPlayer(args[2]));
            } catch (IndexOutOfBoundsException exc) {
                // no subject found
                return injuryCmd.heal();
            }
        }
        else if (Utils.equatesTo(args[0].toLowerCase(),
                InjuryCommand.SUBCOMMAND.INFLICT.toString().toLowerCase())) {
            try {
                Injury.INJURIES inj = injuryCmd.getInjury(args[1]);
                    try {
                        return injuryCmd.inflict(inj, Bukkit.getPlayer(args[2]));
                    } catch (IndexOutOfBoundsException exc) {
                        // no subject found
                        return injuryCmd.inflict(inj);
                    }
                }
            catch (IndexOutOfBoundsException | IllegalArgumentException ignored){
                sender.sendMessage("Unknown injury.");
                return false;
            }
        }
        sender.sendMessage("Illegal arguments. Usage:"+usage);
        return false;
    }
}

