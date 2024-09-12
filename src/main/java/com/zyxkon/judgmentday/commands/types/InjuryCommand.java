package com.zyxkon.judgmentday.commands.types;

import com.zyxkon.judgmentday.*;

import com.zyxkon.judgmentday.commands.CommandType;
import com.zyxkon.judgmentday.commands.JDCommand;
import com.zyxkon.judgmentday.injuries.Injury;
import com.zyxkon.judgmentday.injuries.InjuryManager;
import com.zyxkon.judgmentday.injuries.bloodloss.BloodLossManager;
import com.zyxkon.judgmentday.injuries.impairment.ImpairmentManager;
import com.zyxkon.judgmentday.injuries.infection.InfectionManager;
import com.zyxkon.judgmentday.injuries.poisoning.PoisoningManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class InjuryCommand extends JDCommand {

    public static CommandType getCmdType() {
        return CommandType.INJURY;
    }
    public String usages(){
        return getCmdType().getUsage();
    }
    public enum Subcommands {
        CHECK(Player.class),
        INFLICT(Injury.INJURIES.class, Player.class),
        HEAL(Player.class);



        String usage;
        Subcommands(Class<?>... args) {
            usage = Utils.returnUsage(args);
        }
    }
    public static Subcommands[] getSubcommands(){
        return Subcommands.values();
    }
    public static String getUsages(){
        List<String> uses = new ArrayList<>();
        for (Subcommands s : getSubcommands()){
            List<String> strs = new ArrayList<>();
            strs.add("/"+ Main.commandName);
            strs.add(getCmdType().name());
            strs.add(s.name());
            strs.add(s.usage);
            uses.add(String.join(" ", strs));
        }
        return String.join("\n", uses);
    }
    @Override
    public String getUsagesAsInstance(){
        return getUsages();
    }

    public InjuryCommand(CommandSender sender) {
        super(sender);
        this.setName("injury");
    }
    public boolean applySubcommand(String subcmd, String[] args){
        int n = args.length;
        Player subject = n != 0 ? Bukkit.getPlayer(args[n - 1]) : null;
        switch(Subcommands.valueOf(subcmd)){
            case HEAL: {
                return this.heal(subject);
            }
            case INFLICT: {
                return this.inflict(Injury.getValue(args[0]), subject);
            }
            case CHECK: {
                return this.check(subject);
            }
        }
        return false;
    }

    public List<String> getSubcommandNames(){
        return Arrays.stream(Subcommands.values()).map(Enum::name).collect(Collectors.toList());
    }

    BloodLossManager bloodMan = Main.bloodLossManager;
    ImpairmentManager impaMan = Main.impairmentManager;
    InfectionManager infecMan = Main.infectionManager;
    PoisoningManager poisoMan = Main.poisoningManager;


    public boolean check(Player p){
        if (p == null){
            if (!isPlayer){
                sender.sendMessage("You cannot check your own health.");
                return false;
            }
            return check(pSender);
        }
        sender.sendMessage(String.format("%s's status:", p.getName()));
        if (Main.bloodLossManager.isInjured(p)) {
            sender.sendMessage("Bleeding");
        }
        if (Main.impairmentManager.isInjured(p)) {
            sender.sendMessage("Impaired");
        }
        if (Main.infectionManager.isInjured(p)) {
            sender.sendMessage("Infected");
        }
        if (Main.poisoningManager.isInjured(p)) {
            sender.sendMessage("Poisoned");
        }
        sender.sendMessage("\n");
        return true;
    }

    public boolean heal(Player player){
        if (player == null){
            if (!isPlayer){
                sender.sendMessage("You are not a player. You cannot heal.");
                return false;
            }
            return heal(pSender);
        }
        for (InjuryManager<?> man : Main.injuryManagers){
            if (man.isInjured(player)){
                man.healPlayer(player);
            }
        }
        return true;
    }

    public boolean inflict(Injury.INJURIES injury, Player player){
        if (injury == null){
            sender.sendMessage(
                    "Could not find the provided injury. Injury enums:"
                            + Arrays.stream(Injury.INJURIES.values()).map(Enum::name).collect(Collectors.joining(","))
            );
            return false;
        }
        if (player == null){
            if (!isPlayer) {
                sender.sendMessage("You are not a player. You cannot suffer from injuries.");
                return false;
            }
            return inflict(injury, pSender);
        }
        switch (injury){
            case BLOODLOSS:
                if (!Main.bloodLossManager.isInjured(player)) {
                    bloodMan.affectPlayer(player);
                    return true;
                }
                sender.sendMessage(String.format("Player %s is already bleeding.", player.getName()));
                return false;
            case IMPAIRMENT:
                if (!impaMan.isInjured(player)) {
                    impaMan.affectPlayer(player);
                    return true;
                }
                sender.sendMessage(String.format("Player %s is already physically impaired.", player.getName()));
                return false;
            case INFECTION:
                if (!infecMan.isInjured(player)) {
                    infecMan.affectPlayer(player);
                    return true;
                }
                sender.sendMessage(String.format("Player %s is already infected.", player.getName()));
                return false;
            case POISONING:
                if (!poisoMan.isInjured(player)) {
                    poisoMan.affectPlayer(player);
                    return true;
                }
                sender.sendMessage(String.format("Player %s is already poisoned.", player.getName()));
                return false;
            case ALL:
                Arrays.asList(Main.injuryManagers).forEach(inj -> {
                    if (!inj.isInjured(player)) {
                        inj.affectPlayer(player);
                    }
                });
        }
        return false;
    }
}
