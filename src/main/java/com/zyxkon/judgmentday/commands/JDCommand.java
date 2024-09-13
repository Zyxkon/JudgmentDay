package com.zyxkon.judgmentday.commands;

import com.zyxkon.judgmentday.CommandType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public abstract class JDCommand {
    public String name;
    public CommandSender sender;
    public Player pSender;
    public boolean isPlayer;
    public JDCommand(final CommandSender sender){
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
    public abstract CommandType getCommandType();
    public abstract boolean applySubcommand(String subcmd, String... args);
}
