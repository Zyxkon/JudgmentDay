package com.zyxkon.judgmentday.commands.types;

import com.zyxkon.judgmentday.CommandType;
import com.zyxkon.judgmentday.Counter;
import com.zyxkon.judgmentday.Utils;
import com.zyxkon.judgmentday.commands.JDCommand;
import com.zyxkon.judgmentday.SubcommandType;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class StatsCommand extends JDCommand {
    public static final CommandType cmdType;
    static {
        cmdType = CommandType.STATS;
    }
    public StatsCommand(CommandSender sender) {
        super(sender);
        this.setName(cmdType.name());
    }
    @Override
    public CommandType getCommandType() {
        return cmdType;
    }
    @Override
    public boolean applySubcommand(String subcmd, String... args){
        int n = args.length;
        Player subject = n != 0 ? Bukkit.getPlayer(args[n - 1]) : null;
        for (SubcommandType s : cmdType.getSubcommands()){
            if (Utils.equatesTo(subcmd.toUpperCase(), s.getSimpleName())) {
                switch (s){
                    case STATS_GET: {
                        return this.get(subject);
                    }
                }
            }
        }
        return false;
    }

    public boolean get(Player player){
        if (player == null){
            if (isPlayer){
                return this.get(this.pSender);
            }
            sender.sendMessage("You are not a player and your stats cannot be extracted nor retrieved.");
            return false;
        }
        if (!Utils.isValidPlayer(player)){
            sender.sendMessage("Invalid player. No stats found.");
            return false;
        }
        sender.sendMessage("Stats of "+player.getName()+":\n"+statsList(player));
        return true;
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

