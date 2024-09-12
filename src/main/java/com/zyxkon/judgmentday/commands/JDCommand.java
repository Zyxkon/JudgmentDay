package com.zyxkon.judgmentday.commands;

import com.zyxkon.judgmentday.commands.types.InjuryCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class JDCommand {
    public String name;
    public CommandSender sender;
    public Player pSender;
    public boolean isPlayer;
    public JDCommand(CommandSender sender){
        this.sender = sender;
        try {
            this.pSender = (Player) sender;
            this.isPlayer = true;
        } catch (ClassCastException exc){
            this.isPlayer = false;
        }
    }
    public String getName(){ return name;}
    public void setName(String n){ name = n;}

    public String getUsage() {
        return null;
    }

    public static CommandType getCmdType() {
        return null;
    }

    public List<String> getSubcommandNames() {
        return null;
    }

    public boolean applySubcommand(String subcmd, String... args) {
        return false;
    }
    public String getUsagesAsInstance(){
        return getUsages();
    }

    public static String getUsages() {
        return null;
    }

    public abstract String usages();
}
