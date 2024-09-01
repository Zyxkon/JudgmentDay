package com.zyxkon.judgmentday.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

abstract class JDCommand {
    CommandSender sender;
    Player pSender;
    boolean isPlayer;
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
