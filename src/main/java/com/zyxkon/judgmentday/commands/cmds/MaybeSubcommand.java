package com.zyxkon.judgmentday.commands.cmds;

import com.zyxkon.judgmentday.Main;
import com.zyxkon.judgmentday.commands.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class MaybeSubcommand extends SubCommand {
    static Main plugin;
    private final String cmdName = "kingbreaker";
    public MaybeSubcommand(Main plugin){
        MaybeSubcommand.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        commandSender.sendMessage("hello! this is ur friendly neighborhood maybe subcmd, the command name is "+cmdName+" btw");
        return true;
    }
    public String getName(){
        return cmdName;
    }
}
