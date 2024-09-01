package com.zyxkon.judgmentday.commands;

import com.zyxkon.judgmentday.injuries.Injury;
import com.zyxkon.judgmentday.injuries.InjuryManager;
import com.zyxkon.judgmentday.injuries.bloodloss.BloodLoss;
import com.zyxkon.judgmentday.injuries.bloodloss.BloodLossManager;
import com.zyxkon.judgmentday.injuries.impairment.Impairment;
import com.zyxkon.judgmentday.injuries.impairment.ImpairmentManager;
import com.zyxkon.judgmentday.injuries.infection.Infection;
import com.zyxkon.judgmentday.injuries.infection.InfectionManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class InjuryCommand extends JDCommand{
    public enum INJURIES {
        BLOODLOSS, IMPAIRMENT, INFECTION
    }
    public enum SUBCOMMAND
    {
        CHECK, INFLICT, HEAL
    }
    static InjuryManager<BloodLoss> instance1 = BloodLossManager.getInstance();
    static InjuryManager<Impairment> instance2 = ImpairmentManager.getInstance();
    static InjuryManager<Infection> instance3 = InfectionManager.getInstance();
    ArrayList<InjuryManager<? extends Injury>> managers = new ArrayList<>();
    public InjuryCommand(CommandSender sender) {
        super(sender);
        managers.add(instance1);
        managers.add(instance2);
        managers.add(instance3);
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
        if (instance1.isInjured(p)) {
            sender.sendMessage("Bleeding");
        }
        if (instance2.isInjured(p)) {
            sender.sendMessage("Impaired");
        }
        if (instance3.isInjured(p)) {
            sender.sendMessage("Infected");
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
        for (InjuryManager<? extends Injury> man : managers){
            if (man.isInjured(player)){
                man.healPlayer(player);
            }
        }
        return true;
    }
    public boolean inflict(INJURIES injury){
        if (!isPlayer) {
            sender.sendMessage("You are not a player. You cannot suffer from injuries.");
            return false;
        }
        return inflict(injury, pSender);
    }
    public boolean inflict(INJURIES injury, Player player){
        switch (injury){
            case BLOODLOSS:
                if (!instance1.isInjured(player)) {
                    instance1.affectPlayer(player);
                    return true;
                }
                sender.sendMessage(String.format("Player %s is already bleeding.", player.getName()));
                return false;
            case IMPAIRMENT:
                if (!instance2.isInjured(player)) {
                    instance2.affectPlayer(player);
                    return true;
                }
                sender.sendMessage(String.format("Player %s is already physically impaired.", player.getName()));
                return false;
            case INFECTION:
                if (!instance3.isInjured(player)) {
                    instance3.affectPlayer(player);
                    return true;
                }
                sender.sendMessage(String.format("Player %s is already infected.", player.getName()));
                return false;
            default:
                managers.forEach( inst -> {
                    if (!inst.isInjured(player)) {
                        inst.affectPlayer(player);
                    }
                });
                return false;
        }
    }
}
