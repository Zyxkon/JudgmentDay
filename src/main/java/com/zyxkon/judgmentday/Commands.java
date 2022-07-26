package com.zyxkon.judgmentday;

import com.zyxkon.judgmentday.injuries.bloodloss.BloodLossManager;
import com.zyxkon.judgmentday.thirst.ThirstManager;
import com.zyxkon.judgmentday.injuries.impairment.ImpairmentManager;
import com.zyxkon.judgmentday.injuries.infection.InfectionManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.UUID;

public class Commands implements CommandExecutor {
    static Main plugin;
    public Commands(Main plugin){
        Commands.plugin = plugin;
        plugin.getCommand("judgmentday").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
//        if (strings.length == 0){
//            ArrayList<String> messages = new ArrayList<>();
//            messages.add("");
//            commandSender.sendMessage(messages.toArray(new String[]{}));
//        }
        /*
        /jd effect <amplifier> <duration> <effect>
        /jd enchant <enchantment> <level>
        /jd rename <name>
        /jd unbreak
        /jd thirst <player> <amount>
        /jd injure <player> [bloodloss|impairment|infection]
        */
        Player player = (Player) commandSender;
        switch (strings[0].toLowerCase()) {
            case "stats":{
                switch (strings[1].toLowerCase()){
                    case "reset":
                        Player p;
                        try{
                            p = Bukkit.getPlayer(strings[2]);
                        } catch (IndexOutOfBoundsException exception){
                            p = player;
                        }
                        Counter.resetStats(p);
                        p.sendMessage("Your stats have been resetted!");
                        break;
                }
                break;
            }
            case "thirst": {
                Player p = Bukkit.getPlayer(strings[1]);
                int thirst = Integer.parseInt(strings[2]);
                ThirstManager.setThirst(p, thirst);
                break;
            }
            case "injure": {
                Player p = Bukkit.getPlayer(strings[1]);
                UUID uuid = p.getUniqueId();
                switch (strings[2].toLowerCase()){
                    case "bloodloss": {
                        if (BloodLossManager.isInjured(uuid)) return true;
                        BloodLossManager.affectPlayer(p);
                        p.sendMessage("You have been inflicted with blood loss!");
                        break;
                    }
                    case "impairment": {
                        if (ImpairmentManager.isInjured(uuid)) return true;
                        ImpairmentManager.affectPlayer(p);
                        p.sendMessage("You have been inflicted with impairment!");
                        break;
                    }
                    case "infection": {
                        if (InfectionManager.isInjured(uuid)) return true;
                        InfectionManager.affectPlayer(p);
                        p.sendMessage("You have been inflicted with an infection!");
                        break;
                    }
                    case "all": {
                        if (!BloodLossManager.isInjured(uuid)) BloodLossManager.affectPlayer(p);
                        if (!ImpairmentManager.isInjured(uuid)) ImpairmentManager.affectPlayer(p);
                        if (!InfectionManager.isInjured(uuid)) InfectionManager.affectPlayer(p);
                        p.sendMessage("You have been inflicted with all sorts of injuries possible!");
                        break;
                    }
                }
                return true;
            }
            case "heal":{
                Player p;
                try {
                    p = Bukkit.getPlayer(strings[1]);
                } catch (IndexOutOfBoundsException exception){
                    p = player;
                }
                if (BloodLossManager.isInjured(p)) BloodLossManager.healPlayer(p);
                if (ImpairmentManager.isInjured(p)) ImpairmentManager.healPlayer(p);
                if (InfectionManager.isInjured(p)) InfectionManager.healPlayer(p);
                p.sendMessage("You have been healed!");
                return true;
            }
        }
        return false;
    }
}
