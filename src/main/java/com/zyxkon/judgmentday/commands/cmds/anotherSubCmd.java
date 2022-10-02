package com.zyxkon.judgmentday.commands.cmds;

import com.zyxkon.judgmentday.Main;
import com.zyxkon.judgmentday.commands.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class anotherSubCmd extends SubCommand {
    static Main plugin;
    private final String cmdName = "ballsbreaker";
    public anotherSubCmd(Main plugin){
        MaybeSubcommand.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        commandSender.sendMessage("hello! this is uhmm yet another subcommand, except this time its called "+cmdName+" lol");
        return true;
    }
    public String getName(){
        return cmdName;
    }
}
