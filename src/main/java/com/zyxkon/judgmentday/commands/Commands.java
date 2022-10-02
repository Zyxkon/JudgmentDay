package com.zyxkon.judgmentday.commands;

import com.zyxkon.judgmentday.Counter;
import com.zyxkon.judgmentday.Main;
import com.zyxkon.judgmentday.Utils;
import com.zyxkon.judgmentday.extensions.WorldGuardExtension;
import com.zyxkon.judgmentday.injuries.bloodloss.BloodLossManager;
import com.zyxkon.judgmentday.thirst.ThirstManager;
import com.zyxkon.judgmentday.injuries.impairment.ImpairmentManager;
import com.zyxkon.judgmentday.injuries.infection.InfectionManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class Commands implements CommandExecutor {
    static Main plugin;
    public Commands(Main plugin){
        Commands.plugin = plugin;
        plugin.getCommand("judgmentday").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
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
        Player player = (Player) commandSender;
        switch (strings[0].toLowerCase()) {
            case "stats":{
                switch (strings[1].toLowerCase()){
                    case "reset": {
                        Player p;
                        try {
                            p = Bukkit.getPlayer(strings[2]);
                        } catch (IndexOutOfBoundsException exception) {
                            p = player;
                        }
                        Counter.resetStats(p);
                        p.sendMessage("Your stats have been resetted!");
                        break;
                    }
                    case "get":{
                        int counts = 0;
                        String str = "count";
                        Player p;
                        try {
                            p = Bukkit.getPlayer(strings[3]);
                        }
                        catch (IndexOutOfBoundsException exception){
                            p = player;
                        }
                        switch (strings[2].toLowerCase()){
                            case "d":
                            case "deaths":{
                                str = "death";
                                counts = Counter.getDeaths(p);
                                break;
                            }
                            case "pk":
                            case "player_kills":{
                                str = "player kills";
                                counts = Counter.getPlayerKills(p);
                                break;
                            }
                            case "wk":
                            case "walker_kills":{
                                str = "walker kills";
                                counts = Counter.getWalkerKills(p);
                                break;
                            }
                        }
                        player.sendMessage(String.format("%s's %s is %s ", p.getName(), str, counts));
                        break;
                    }
                }
                return true;
            }
            case "thirst": {
                switch (strings[1].toLowerCase()){
                    case "get":{
                        Player p;
                        try {
                            p = Bukkit.getPlayer(strings[3]);
                        } catch (IndexOutOfBoundsException exception){
                            p = player;
                        }
                        player.sendMessage(String.format("%s's hydration is %d", p.getName(), ThirstManager.getThirst(p))+"%");
                        return true;
                    }
                    case "set":{
                        /*
                         * 2     3        4
                         * set <player> <thirst>
                         * set <thirst>
                         * set 223 // sets the player's thirst to 223
                         * set 223 223 // sets the thirst of the player called 223 to 223
                         *
                         *
                         */
                        Player p;
                        int thirst;
                        try {
                            thirst = Integer.parseInt(strings[3]);
                            p = player;
                        } catch (NumberFormatException exception){
                            p = Bukkit.getPlayer(strings[3]);
                            thirst = Integer.parseInt(strings[4]);
                        }
                        ThirstManager.setThirst(p, thirst);
                        p.sendMessage(String.format("Your hydration is now %s", thirst)+"%");
                        return true;
                    }
                }
//                Player p = Bukkit.getPlayer(strings[1]);
//                int thirst = Integer.parseInt(strings[2]);
//                ThirstManager.setThirst(p, thirst);
                return true;
            }
            case "injure": {
                Player p = Bukkit.getPlayer(strings[1]);
                UUID uuid = p.getUniqueId();
                switch (strings[2].toLowerCase()){
                    case "bloodloss": {
                        if (BloodLossManager.getInstance().isInjured(uuid)) return true;
                        BloodLossManager.getInstance().affectPlayer(p);
                        p.sendMessage("You have been inflicted with blood loss!");
                        break;
                    }
                    case "impairment": {
                        if (ImpairmentManager.getInstance().isInjured(uuid)) return true;
                        ImpairmentManager.getInstance().affectPlayer(p);
                        p.sendMessage("You have been inflicted with impairment!");
                        break;
                    }
                    case "infection": {
                        if (InfectionManager.getInstance().isInjured(uuid)) return true;
                        InfectionManager.getInstance().affectPlayer(p);
                        p.sendMessage("You have been inflicted with an infection!");
                        break;
                    }
                    case "all": {
                        if (!BloodLossManager.getInstance().isInjured(uuid)) BloodLossManager.getInstance().affectPlayer(p);
                        if (!ImpairmentManager.getInstance().isInjured(uuid)) ImpairmentManager.getInstance().affectPlayer(p);
                        if (!InfectionManager.getInstance().isInjured(uuid)) InfectionManager.getInstance().affectPlayer(p);
                        p.sendMessage("You have been inflicted with all sorts of injuries possible!");
                        break;
                    }
                }
                return true;
            }
            case "heal":{
                Player p;
                try {
                    p = Bukkit.getPlayer(strings[1]);
                } catch (IndexOutOfBoundsException exception){
                    p = player;
                }
                if (BloodLossManager.getInstance().isInjured(p)) BloodLossManager.getInstance().healPlayer(p);
                if (ImpairmentManager.getInstance().isInjured(p)) ImpairmentManager.getInstance().healPlayer(p);
                if (InfectionManager.getInstance().isInjured(p)) InfectionManager.getInstance().healPlayer(p);
                p.sendMessage("You have been healed!");
                return true;
            }
            case "r": case "regions":{
                if (Bukkit.getPluginManager().getPlugin("WorldGuard") == null){
                    player.sendMessage("WorldGuard isn't installed, you can't use this subcommand!");
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
        return false;
    }
}

