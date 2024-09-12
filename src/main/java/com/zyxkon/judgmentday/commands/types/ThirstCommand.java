package com.zyxkon.judgmentday.commands.types;


import com.zyxkon.judgmentday.Main;
import com.zyxkon.judgmentday.commands.CommandType;
import com.zyxkon.judgmentday.commands.Commands;

import com.zyxkon.judgmentday.Utils;
import com.zyxkon.judgmentday.commands.JDCommand;
import com.zyxkon.judgmentday.thirst.ThirstManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class ThirstCommand extends JDCommand {
    enum Subcommands {
        SET(int.class, Player.class),
        GET(Player.class),
        RESET(Player.class),
        LIST();



        String usage;
        Subcommands(Class<?>... args) {
            usage = Utils.returnUsage(args);
        }
    }
    public String usages(){
        return getCmdType().getUsage();
    }
    public ThirstCommand(CommandSender sender) {
        super(sender);
        this.setName("thirst");
    }

    public static CommandType getCmdType() {
        return CommandType.THIRST;
    }
    public ThirstCommand getStatic(){
        return null;
    }
    public static String getUsages(){
        List<String> uses = new ArrayList<>();
        for (Subcommands s : Subcommands.values()){
            List<String> strs = new ArrayList<>();
            strs.add("/"+ Main.commandName);
            strs.add(getCmdType().name());
            strs.add(s.name());
            strs.add(s.usage);
            uses.add(String.join(" ", strs));
        }
        return String.join("\n", uses);
    }

    public List<String> getSubcommandNames(){
        return Arrays.stream(Subcommands.values()).map(Enum::name).collect(Collectors.toList());
    }

    @Override
    public boolean applySubcommand(String subcmd, String... args) {
        int n = args.length;
        Player subject = n != 0 ? Bukkit.getPlayer(args[n - 1]) : null;
        switch(Subcommands.valueOf(subcmd)){
            case GET: {
                return this.get(subject);
            }
            case RESET: {
                return this.reset(subject);
            }
            case SET: {
                try {
                    int t = Integer.parseInt(args[0]);
                    return this.set(subject, t);
                } catch (NumberFormatException exception){
                    sender.sendMessage("Please input an integer!");
                    return false;
                }
            }
            case LIST: {
                return sendThirstList();
            }
        }
        return false;
    }

    public boolean reset(Player p){
        if (p == null){
            if (this.isPlayer) return reset(pSender);
            sender.sendMessage("You are not a player; and therefore, you have no thirst to reset.");
            return false;
        }
        return set(p, 100);
    }
    public boolean get(Player p){
        if (p == null){
            if (this.isPlayer) {
                return get(pSender);
            }
            sender.sendMessage("You are not a player; and therefore, you have no thirst to check.");
            return false;
        }
        int t = ThirstManager.getThirst(p);
        sender.sendMessage(String.format("%s's thirst is %d", p.getName(), t));
        return true;
    }
    public boolean set(Player p, int t){
        if (p == null){
            if (this.isPlayer) {
                return set(pSender, t);
            }
            sender.sendMessage("You are not a player; and therefore, you have no thirst to set.");
            return false;
        }
        ThirstManager.setThirst(p, t);
        sender.sendMessage(String.format("%s's thirst is now %d", p.getName(), t));
        return true;
    }
    public boolean sendThirstList(){
        sender.sendMessage(
                "Thirst list:\n"
        );
        int i = 0;
        HashMap<UUID, Integer> hash = ThirstManager.getHash();
        for (UUID key : hash.keySet()){
            sender.sendMessage(String.format("%d.%s:%d%%", i, Bukkit.getPlayer(key).getDisplayName(), hash.get(key)));
            i++;
        }
        return true;
    }
}
