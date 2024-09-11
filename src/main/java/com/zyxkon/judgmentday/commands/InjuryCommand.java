package com.zyxkon.judgmentday.commands;

import com.zyxkon.judgmentday.CommandSystem;
import com.zyxkon.judgmentday.JDCommand;
import com.zyxkon.judgmentday.Main;
import com.zyxkon.judgmentday.Utils;
import com.zyxkon.judgmentday.injuries.Injury;
import com.zyxkon.judgmentday.injuries.InjuryManager;
import com.zyxkon.judgmentday.injuries.bloodloss.BloodLossManager;
import com.zyxkon.judgmentday.injuries.impairment.ImpairmentManager;
import com.zyxkon.judgmentday.injuries.infection.InfectionManager;
import com.zyxkon.judgmentday.injuries.poisoning.PoisoningManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class InjuryCommand extends JDCommand {
    BloodLossManager bloodMan = Main.bloodLossManager;
    ImpairmentManager impaMan = Main.impairmentManager;
    InfectionManager infecMan = Main.infectionManager;
    PoisoningManager poisoMan = Main.poisoningManager;
    public InjuryCommand(CommandSender sender) {
        super(sender);
        this.setName("injury");
    }

    @Override
    public CommandSystem.Commands getCmdType() {
        return CommandSystem.Commands.INJURY;
    }

    public String getUsage(){
        return CommandSystem.Commands.INJURY.getUsage();
    }

    public boolean check(){
        if (!isPlayer){
            sender.sendMessage("You cannot check your own health.");
            return false;
        }
        return check(pSender);
    }
    public boolean check(Player p){
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
    public boolean heal(){
        if (!isPlayer){
            sender.sendMessage("You are not a player. You cannot heal.");
            return false;
        }
        return heal(pSender);
    }
    public boolean heal(Player player){
        for (InjuryManager<?> man : Main.injuryManagers){
            if (man.isInjured(player)){
                man.healPlayer(player);
            }
        }
        return true;
    }
    public Injury.INJURIES getInjury(String s) throws IllegalArgumentException{
        for (Injury.INJURIES inj : Injury.INJURIES.values()){
            if (Utils.equatesTo(s.toUpperCase(), inj.name())){
                return inj;
            }
        }
        return Injury.INJURIES.ALL;
    }
    public boolean inflict(Injury.INJURIES injury){
        if (!isPlayer) {
            sender.sendMessage("You are not a player. You cannot suffer from injuries.");
            return false;
        }
        return inflict(injury, pSender);
    }
    public boolean inflict(Injury.INJURIES injury, Player player){
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
