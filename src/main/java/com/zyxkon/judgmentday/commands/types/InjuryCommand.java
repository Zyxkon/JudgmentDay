package com.zyxkon.judgmentday.commands.types;

import com.zyxkon.judgmentday.*;

import com.zyxkon.judgmentday.CommandType;
import com.zyxkon.judgmentday.commands.JDCommand;
import com.zyxkon.judgmentday.SubcommandType;
import com.zyxkon.judgmentday.injuries.Injury;
import com.zyxkon.judgmentday.injuries.bloodloss.BloodLossManager;
import com.zyxkon.judgmentday.injuries.impairment.ImpairmentManager;
import com.zyxkon.judgmentday.injuries.infection.InfectionManager;
import com.zyxkon.judgmentday.injuries.poisoning.PoisoningManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.stream.Collectors;


public class InjuryCommand extends JDCommand {
    public static final CommandType cmdType = CommandType.INJURY;
    public InjuryCommand(CommandSender sender) {
        super(sender);
        this.setName(cmdType.name());
    }

    @Override
    public CommandType getCommandType() {
        return cmdType;
    }

    @Override
    public boolean applySubcommand(String subcmd, String[] args){
        int n = args.length;
        Player subject = n != 0 ? Bukkit.getPlayer(args[n - 1]) : null;
        for (SubcommandType s : cmdType.getSubcommands()){
            if (Utils.equatesTo(subcmd, s.getSimpleName())) {
                switch (s){
                    case INJURY_HEAL: {
                        return this.heal(subject);
                    }
                    case INJURY_INFLICT: {
                        return this.inflict(Injury.getValue(args[0]), subject);
                    }
                    case INJURY_CHECK: {
                        return this.check(subject);
                    }
                }
            }
        }
        return false;
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
        Arrays.stream(Main.injuryManagers).forEach(m -> {
                    if (m.isInjured(player)) m.healPlayer(player);
        });
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
