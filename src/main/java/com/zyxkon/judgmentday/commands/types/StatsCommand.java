package com.zyxkon.judgmentday.commands.types;

import com.zyxkon.judgmentday.commands.CommandType;
import com.zyxkon.judgmentday.commands.Commands;
import com.zyxkon.judgmentday.Counter;
import com.zyxkon.judgmentday.Main;
import com.zyxkon.judgmentday.Utils;
import com.zyxkon.judgmentday.commands.JDCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class StatsCommand extends JDCommand {
    public static CommandType getCmdType() {
        return CommandType.STATS;
    }
    public String usages(){
        return getCmdType().getUsage();
    }
    static List<Subcommands> subcommands = Arrays.asList(Subcommands.values());
    static String usage;
    static {
        List<String> params = new ArrayList<>(Collections.singletonList("/" + Main.commandName));
        for (Subcommands s : subcommands){
            params.add(s.usage);
        }
        usage = String.join(" ", params);
    }
    public enum Subcommands {
        GET(Player.class);
        String usage;

        Subcommands(Class<?>... args) {
            usage = Utils.returnUsage(args);
        }
    }

    public StatsCommand(CommandSender sender) {
        super(sender);
        this.setName("stats");
    }

    public boolean applySubcommand(String subcmd, String... args){
        int n = args.length;
        Player subject = n != 0 ? Bukkit.getPlayer(args[n - 1]) : null;
        switch(Subcommands.valueOf(subcmd)){
            case GET: return this.get(subject);
        }
        return false;
    }

    public List<String> getSubcommandNames(){
        return Arrays.stream(Subcommands.values()).map(Enum::name).collect(Collectors.toList());
    }

    public static String getUsages(){
        List<String> uses = new ArrayList<>();
        for (StatsCommand.Subcommands s : StatsCommand.Subcommands.values()){
            List<String> strs = new ArrayList<>();
            strs.add("/"+ Main.commandName);
            strs.add(getCmdType().name());
            strs.add(s.name());
            strs.add(s.usage);
            uses.add(String.join(" ", strs));
        }
        return String.join("\n", uses);
    }
    public String getUsagesAsInstance(){
        return getUsages();
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

