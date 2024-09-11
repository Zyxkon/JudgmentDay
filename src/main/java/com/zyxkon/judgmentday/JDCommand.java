package com.zyxkon.judgmentday;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class JDCommand {
    static String name;
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

    public abstract CommandSystem.Commands getCmdType();
}
