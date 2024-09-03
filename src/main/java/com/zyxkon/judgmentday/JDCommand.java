package com.zyxkon.judgmentday;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class JDCommand {
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
}
