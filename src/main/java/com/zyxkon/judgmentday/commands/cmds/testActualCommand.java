package com.zyxkon.judgmentday.commands.cmds;

import com.zyxkon.judgmentday.Main;
import com.zyxkon.judgmentday.commands.CommandGroup;
import com.zyxkon.judgmentday.commands.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.HashMap;

public class testActualCommand extends CommandGroup {
    static Main plugin;
    private final static String name = "test";
    private final HashMap<String, SubCommand> sCmds = new HashMap<>();
    public testActualCommand(Main plugin){
        testActualCommand.plugin = plugin;
        this.addCommand(new MaybeSubcommand(plugin));
        this.addCommand(new anotherSubCmd(plugin));
        plugin.getCommand(plugin.getCommandName()).setExecutor(this);
    }
    @Override
    public boolean execute(CommandSender commandSender, Command command, String s, String[] strings){
        commandSender.sendMessage("no bithces? i mean no args?");
        return true;
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!check(strings)) return true;
        commandSender.sendMessage("seems like you've just executed us! yea sure, idc if u have args or not");
        return super.onCommand(sCmds, commandSender, command, s, Arrays.copyOfRange(strings, 1, strings.length));
    }
    public void addCommand(SubCommand command){
        sCmds.put(command.getName(), command);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean check(String[] strings) {
        return strings[0].equals(name);
    }
}
