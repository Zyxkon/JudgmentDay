package com.zyxkon.judgmentday.commands;

import com.zyxkon.judgmentday.CommandSystem;
import com.zyxkon.judgmentday.Counter;
import com.zyxkon.judgmentday.JDCommand;
import com.zyxkon.judgmentday.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StatsCommand extends JDCommand {
    private final static CommandSystem.Commands cmdType = CommandSystem.Commands.STATS;

    public CommandSystem.Commands getCmdType() {
        return cmdType;
    }
    public String getUsage(){
        return CommandSystem.Commands.STATS.getUsage();
    }
    public StatsCommand(CommandSender sender) {
        super(sender);
        this.setName("stats");
    }
    public boolean reset(Player player){
        player.sendMessage("You may be a player, but your stats should not be reset.");
        sender.sendMessage(String.format("You tried to reset %s's stats.", player.getName()));
        return true;
    }
    public boolean reset(){
        if (isPlayer) {
            return this.reset(this.pSender);
        }
        sender.sendMessage("You are not a player and your stats cannot be reset.");
        return false;
    }
    public boolean get(Player player){
        if (!Utils.isValidPlayer(player)){
            sender.sendMessage("Invalid player. No stats found.");
            return false;
        }
        sender.sendMessage("Stats of "+player.getName()+":\n"+statsList(player));
        return true;
    }
    public boolean get(){
        if (isPlayer){
            return this.get(this.pSender);
        }
        sender.sendMessage("You are not a player and your stats cannot be extracted nor retrieved.");
        return false;
    }
    public String statsList(Player p){
        return String.format(
                "Deaths: %d\n"+
                        "Players killed: %d\n"+
                        "Walkers killed: %d\n",
                Counter.getDeaths(p), Counter.getPlayerKills(p), Counter.getWalkerKills(p)
        );
    }
}

