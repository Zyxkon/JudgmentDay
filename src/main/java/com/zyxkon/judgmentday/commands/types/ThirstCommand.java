package com.zyxkon.judgmentday.commands.types;


import com.zyxkon.judgmentday.CommandType;

import com.zyxkon.judgmentday.Utils;
import com.zyxkon.judgmentday.commands.JDCommand;
import com.zyxkon.judgmentday.SubcommandType;
import com.zyxkon.judgmentday.thirst.ThirstManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class ThirstCommand extends JDCommand {
    public static final CommandType cmdType = CommandType.THIRST;
    public ThirstCommand(CommandSender sender) {
        super(sender);
        this.setName(cmdType.name());
    }

    @Override
    public CommandType getCommandType() {
        return cmdType;
    }
    @Override
    public boolean applySubcommand(String subcmd, String[] args){
        int n = args.length;
        Player subject = n != 0 ? Bukkit.getPlayer(args[n - 1]) : null;
        for (SubcommandType s : cmdType.getSubcommands()){
            if (Utils.equatesTo(subcmd, s.getSimpleName())) {
                switch (s){
                    case THIRST_GET: {
                        return this.get(subject);
                    }
                    case THIRST_LIST: {
                        return this.sendThirstList();
                    }
                    case THIRST_RESET: {
                        return this.reset(subject);
                    }
                    case THIRST_SET: {
                        try {
                            int t = Integer.parseInt(args[0]);
                            return this.set(subject, t);
                        } catch (NumberFormatException exception){
                            sender.sendMessage("Please input an integer!");
                            return false;
                        }
                    }
                }
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
