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
import java.util.concurrent.Callable;
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
        commandSender.sendMessage("Args:\n");
        Arrays.asList(args).forEach(commandSender::sendMessage);
        HashMap<String, Callable<Boolean>> commands = new HashMap<>();
        commands.put("stats",
                () -> stats(commandSender, args)
        );
        commands.put("thirst",
                () -> thirst(commandSender, args)
        );
        commands.put("injury",
                () -> injury(commandSender, args)
        );
        for (String str : commands.keySet()){
            if (Utils.equatesTo(strings[0].toLowerCase(), str)){
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
        CommandSystem.Commands c = jdCmd.getCmdType();
        if (args.length == 0){
            sender.sendMessage(jdCmd.getUsage());
            return false;
        }
        for (CommandSystem.Subcommands s : CommandSystem.Subcommands.getSubcommands(c)) {
            if (Utils.equatesTo(args[0].toUpperCase(), s.name())){
                if (s == CommandSystem.Subcommands.GET) {
                    try {
                        return jdCmd.get(Bukkit.getPlayer(args[1]));
                    } catch (IndexOutOfBoundsException e) {
                        return jdCmd.get();
                    }
                }
            }
        }
        sender.sendMessage(jdCmd.getUsage());
        return false;
    }
    public static boolean thirst(CommandSender sender, String[] args){
        ThirstCommand jdCmd = new ThirstCommand(sender);
        if (args.length == 0){
            sender.sendMessage(jdCmd.getUsage());
            return false;
        }
        for (CommandSystem.Subcommands s : CommandSystem.Subcommands.getSubcommands(jdCmd.getCmdType())) {
            if (Utils.equatesTo(args[0].toUpperCase(), s.name())){
                switch (s){
                    case GET:
                    {
                        try {
                            return jdCmd.get(Bukkit.getPlayer(args[1]));
                        } catch (IndexOutOfBoundsException e){
                            return jdCmd.get();
                        }
                    }
                    case SET:
                    {
                        try {
                            return jdCmd.set(Bukkit.getPlayer(args[2]), Integer.parseInt(args[1]));
                        } catch (IndexOutOfBoundsException e){
                            return jdCmd.set(Integer.parseInt(args[1]));
                        }
                    }
                    case RESET: {
                        try {
                            return jdCmd.reset(Bukkit.getPlayer(args[1]));
                        } catch (IndexOutOfBoundsException e) {
                            return jdCmd.reset();
                        }
                    }
                    case LIST:
                    {
                        return jdCmd.sendThirstList();
                    }
                }
            }
        }
        sender.sendMessage(jdCmd.getUsage());
        return false;
    }
    public static boolean injury(CommandSender sender, String[] args){
        InjuryCommand jdCmd = new InjuryCommand(sender);
        if (args.length == 0){
            sender.sendMessage(jdCmd.getUsage());
            return false;
        }
        for (CommandSystem.Subcommands s : CommandSystem.Subcommands.getSubcommands(jdCmd.getCmdType())) {
            if (Utils.equatesTo(args[0].toUpperCase(), s.name())){
                switch (s){
                    case CHECK:
                    {
                        try { return jdCmd.check(Bukkit.getPlayer(args[1])); }
                        catch (IndexOutOfBoundsException e){ return jdCmd.check(); }

                    }
                    case INFLICT:
                    {
                        try {
                            Injury.INJURIES inj = jdCmd.getInjury(args[1].toUpperCase());
                            System.out.println(args[1].toUpperCase());
                            try {
                                return jdCmd.inflict(inj, Bukkit.getPlayer(args[2]));
                            } catch (IndexOutOfBoundsException exc) {
                                return jdCmd.inflict(inj);
                            }
                        }
                        catch (IndexOutOfBoundsException | IllegalArgumentException ignored){
                            sender.sendMessage("Unknown injury.");
                            return false;
                        }
                    }
                    case HEAL:
                    {
                        try {
                            return jdCmd.heal(Bukkit.getPlayer(args[2]));
                        } catch (IndexOutOfBoundsException exc) {
                            return jdCmd.heal();
                        }
                    }
                }
            }
        }
        sender.sendMessage(jdCmd.getUsage());
        return false;
    }
}

