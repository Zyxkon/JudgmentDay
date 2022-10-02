package com.zyxkon.judgmentday.commands.cmds;

import com.zyxkon.judgmentday.Main;
import com.zyxkon.judgmentday.commands.CommandGroup;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class testActualCommand extends CommandGroup {
    static Main plugin;
    public testActualCommand(Main plugin){
        testActualCommand.plugin = plugin;
        this.addCommand(new MaybeSubcommand(plugin));
        this.addCommand(new anotherSubCmd(plugin));
        plugin.getCommand("prob").setExecutor(this);

    }
    @Override
    public boolean execute(CommandSender commandSender, Command command, String s, String[] strings){
        commandSender.sendMessage("no bithces? i mean no args?");
        return true;
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        commandSender.sendMessage("seems like you've just executed us! yea sure, idc if u have args or not");
        return super.onCommand(commandSender, command, s, strings);
    }
}
