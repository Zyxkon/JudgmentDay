package com.zyxkon.judgmentday.commands;

import com.zyxkon.judgmentday.JDCommand;
import com.zyxkon.judgmentday.thirst.ThirstManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class ThirstCommand extends JDCommand {

    public ThirstCommand(CommandSender sender) {
        super(sender);
    }
    public enum SUBCOMMAND
    {
        RESET, GET, SET, LIST
    }
    public boolean get(Player p){
        int t = ThirstManager.getThirst(p);
        sender.sendMessage(String.format("%s's thirst is %d", p.getName(), t));
        return true;
    }
    public boolean get(){
        if (this.isPlayer) {
            return get(pSender);
        }
        sender.sendMessage("You are not a player; and therefore, you have no thirst to check.");
        return false;
    }
    public boolean set(Player p, int t){
        ThirstManager.setThirst(p, t);
        sender.sendMessage(String.format("%s's thirst is now %d", p.getName(), t));
        return true;
    }
    public boolean set(int t){
        if (this.isPlayer) {
            return set(pSender, t);
        }
        sender.sendMessage("You are not a player; and therefore, you have no thirst to set.");
        return false;
    }
    public boolean sendThirstList(){
        sender.sendMessage(
                "Thirst list:\n"
        );
        int i = 0;
        HashMap<UUID, Integer> hash = ThirstManager.getHash();
        for (UUID key : hash.keySet()){
            sender.sendMessage(String.format("%d.%s:%d%%", i, Bukkit.getPlayer(key).getName(), hash.get(key)));
            i++;
        }
        return true;
    }
}