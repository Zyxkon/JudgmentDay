//package com.zyxkon.judgmentday.commands.cmds.stats;
//
//import com.zyxkon.judgmentday.Main;
//import com.zyxkon.judgmentday.commands.CommandGroup;
//import com.zyxkon.judgmentday.commands.SubCommand;
//import com.zyxkon.judgmentday.commands.cmds.MaybeSubcommand;
//import org.bukkit.command.Command;
//import org.bukkit.command.CommandExecutor;
//import org.bukkit.command.CommandSender;
//
//import java.util.Arrays;
//import java.util.HashMap;
//
//public class MainStatCmd extends CommandGroup {
//    static Main plugin;
//    private final HashMap<String, SubCommand> sCmds = new HashMap<>();
//    public MainStatCmd(Main plugin){
//        MainStatCmd.plugin = plugin;
//        this.addCommand(new MaybeSubcommand(plugin));
//        plugin.getCommand("prob").setExecutor(this);
//
//    }
//    @Override
//    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
//        String firstArg = strings[0];
//        if (sCmds.containsKey(firstArg)){
//            return sCmds.get(firstArg).onCommand(
//                    commandSender, command, s, Arrays.copyOfRange(strings, 1, strings.length)
//            );
//        }
//        return true;
//    }
//    public void addCommand(SubCommand command){
//        sCmds.put(command.getName(), command);
//    }
//}
