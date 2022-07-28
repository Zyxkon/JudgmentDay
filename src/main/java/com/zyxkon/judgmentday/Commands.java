package com.zyxkon.judgmentday;

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
//        if (strings.length == 0){
//            ArrayList<String> messages = new ArrayList<>();
//            messages.add("");
//            commandSender.sendMessage(messages.toArray(new String[]{}));
//        }
        /*

        /jd thirst <player> <amount>
        /jd injure <player> [bloodloss|impairment|infection]
        */
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
                break;
            }
            case "thirst": {
                switch (strings[2].toLowerCase()){
                    case "get":{
                        Player p;
                        try {
                            p = Bukkit.getPlayer(strings[3]);
                        } catch (IndexOutOfBoundsException exception){
                            p = player;
                        }
                        player.sendMessage(String.format("%s's hydration is %d", p.getName(), ThirstManager.getThirst(p))+"%");
                        break;
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
                    }
                }
//                Player p = Bukkit.getPlayer(strings[1]);
//                int thirst = Integer.parseInt(strings[2]);
//                ThirstManager.setThirst(p, thirst);
                break;
            }
            case "injure": {
                Player p = Bukkit.getPlayer(strings[1]);
                UUID uuid = p.getUniqueId();
                switch (strings[2].toLowerCase()){
                    case "bloodloss": {
                        if (BloodLossManager.isInjured(uuid)) return true;
                        BloodLossManager.affectPlayer(p);
                        p.sendMessage("You have been inflicted with blood loss!");
                        break;
                    }
                    case "impairment": {
                        if (ImpairmentManager.isInjured(uuid)) return true;
                        ImpairmentManager.affectPlayer(p);
                        p.sendMessage("You have been inflicted with impairment!");
                        break;
                    }
                    case "infection": {
                        if (InfectionManager.isInjured(uuid)) return true;
                        InfectionManager.affectPlayer(p);
                        p.sendMessage("You have been inflicted with an infection!");
                        break;
                    }
                    case "all": {
                        if (!BloodLossManager.isInjured(uuid)) BloodLossManager.affectPlayer(p);
                        if (!ImpairmentManager.isInjured(uuid)) ImpairmentManager.affectPlayer(p);
                        if (!InfectionManager.isInjured(uuid)) InfectionManager.affectPlayer(p);
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
                if (BloodLossManager.isInjured(p)) BloodLossManager.healPlayer(p);
                if (ImpairmentManager.isInjured(p)) ImpairmentManager.healPlayer(p);
                if (InfectionManager.isInjured(p)) InfectionManager.healPlayer(p);
                p.sendMessage("You have been healed!");
                return true;
            }
            case "r":
            case "regions":{
                if (Bukkit.getPluginManager().getPlugin("WorldGuard") == null){
                    player.sendMessage("WorldGuard isn't installed, you can't use this subcommand!");
                    return true;
                }
                switch (strings[1].toLowerCase()){
                    case "a": case "add": {
                        switch (strings[2].toLowerCase()){
                            case "safezones": case "safezone":{
                                String region = strings[3];
                                if (!WorldGuardExtension.regionExists(region)){
                                    commandSender.sendMessage(String.format("Region %s does not exist!", region));
                                    return true;
                                }
                                if (WorldGuardExtension.isSafezone(region)) {
                                    commandSender.sendMessage(String.format("%s is already a safezone!", region));
                                    return true;
                                }
                                ArrayList<String> safezones = new ArrayList<>(plugin.getRegionsConfig().getStringList("safezones"));
                                safezones.add(region);
                                plugin.getRegionsConfig().set("safezones", safezones);
                                try{
                                    plugin.getRegionsConfig().save(plugin.getRegionsFile());
                                    commandSender.sendMessage(Utils.translate("The region has &asuccessfully&f been saved as a safezone!"));
                                } catch (IOException exception) {
                                    exception.printStackTrace();
                                    commandSender.sendMessage(Utils.translate("An &cerror&f has occurred!"));
                                }
                                return true;
                            }
                            case "barracks": case "barrack": {
                                String region = strings[3];
                                if (!WorldGuardExtension.regionExists(region)){
                                    commandSender.sendMessage(String.format("Region %s does not exist!", region));
                                    return true;
                                }
                                if (WorldGuardExtension.isBarrack(region)) {
                                    commandSender.sendMessage(String.format("%s is already a barrack!", region));
                                    return true;
                                }
                                ArrayList<String> barracks = new ArrayList<>(plugin.getRegionsConfig().getStringList("barracks"));
                                barracks.add(region);
                                plugin.getRegionsConfig().set("barracks", barracks);
                                try{
                                    plugin.getRegionsConfig().save(plugin.getRegionsFile());
                                    commandSender.sendMessage(Utils.translate("The region has &asuccessfully&f been saved as a barrack!"));
                                } catch (IOException exception) {
                                    exception.printStackTrace();
                                    commandSender.sendMessage(Utils.translate("An &cerror&f has occurred!"));
                                }
                                return true;
                            }
                        }
                        break;
                    }
//                    case "r": case "remove": {
//                        switch (strings[2].toLowerCase()){
//                            case "safezones":
//                            case "safezone":{
//                                String region = strings[3];
//                                ArrayList<String> safezones = new ArrayList<>(plugin.getRegionsConfig().getStringList("safezones"));
//                                if (!safezones.contains(region)){
//                                    commandSender.sendMessage(String.format("%s is not a safezone!", region));
//                                    return true;
//                                }
//                                safezones.remove(region);
//                                plugin.getRegionsConfig().set("safezones", region);
//                                try{
//                                    plugin.getRegionsConfig().save(plugin.getRegionsFile());
//                                    commandSender.sendMessage(Utils.translate(String.format("Safezone %s has been removed!", region)));
//                                } catch (IOException exception) {
//                                    exception.printStackTrace();
//                                    commandSender.sendMessage(Utils.translate("An &cerror&f has occurred!"));
//                                }
//                                return true;
//                            }
//                            case "barracks":
//                            case "barrack":{
//                                String region = strings[3];
//                                ArrayList<String> barracks = new ArrayList<>(plugin.getRegionsConfig().getStringList("barracks"));
//                                if (!barracks.contains(region)){
//                                    commandSender.sendMessage(String.format("%s is not a barrack!", region));
//                                    return true;
//                                }
//                                barracks.remove(region);
//                                plugin.getRegionsConfig().set("barracks", region);
//                                try{
//                                    plugin.getRegionsConfig().save(plugin.getRegionsFile());
//                                    commandSender.sendMessage(Utils.translate(String.format("Barrack %s has been removed!", region)));
//                                } catch (IOException exception) {
//                                    exception.printStackTrace();
//                                    commandSender.sendMessage(Utils.translate("An &cerror&f has occurred!"));
//                                }
//                                return true;
//                            }
//                        }
//                        break;
//                    }
                }
                break;
            }
        }
        return false;
    }
}

