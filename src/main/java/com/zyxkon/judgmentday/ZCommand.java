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

import java.util.Arrays;
import java.util.UUID;

public class ZCommand implements CommandExecutor {
    Main plugin;
    public ZCommand(Main plugin){
        this.plugin = plugin;
        plugin.getCommand("zyxkon").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)){
            commandSender.sendMessage(Utils.translate("&cOnly &nplayers&c can use this command!"));
            return true;
        }
        if (strings.length == 0){
            commandSender.sendMessage("Plugin được phát triển bởi Zyxkon");
            return true;
        }
        /*
        /zyxkon effect <amplifier> <duration> <effect>
        /zyxkon enchant <enchantment> <level>
        /zyxkon rename <name>
        /zyxkon unbreak
        /zyxkon thirst <player> <amount>
        /zyxkon injure <player> [bloodloss|impairment|infection]
        */
        Player player = (Player) commandSender;
        switch (strings[0].toLowerCase()) {
            case "effect": {
                ItemStack item = player.getInventory().getItemInMainHand();
                PotionMeta meta = (PotionMeta) item.getItemMeta();
                int amplifier = Integer.parseInt(strings[1]);
                int duration = Integer.parseInt(strings[2]);
                PotionEffect effect = PotionEffectType.getByName(strings[3]).createEffect(duration, amplifier);
                meta.addCustomEffect(effect, true);
                item.setItemMeta(meta);
                player.sendMessage(String.format("%s %s %s has been applied to %s", effect.getType(), amplifier, duration, meta.getDisplayName()));
                break;
            }
            case "enchant": {
                ItemStack item = player.getInventory().getItemInMainHand();
                ItemMeta meta = item.getItemMeta();
                Enchantment ench = Enchantment.getByName((strings[1]).toUpperCase());
                int lvl = Integer.parseInt(strings[2]);
                meta.addEnchant(ench, lvl, true);
                item.setItemMeta(meta);
                player.sendMessage(Utils.translate(String.format("&n%s &ahas been enchanted with &b%s&a of level &c%s", meta.getDisplayName(), ench.getName(), lvl)));
                break;
            }
            case "rename": {
                ItemStack item = player.getInventory().getItemInMainHand();
                ItemMeta meta = item.getItemMeta();
                String str = Utils.translate("&r" + String.join(" ", Arrays.copyOfRange(strings, 1, strings.length)));
                meta.setDisplayName(str);
                item.setItemMeta(meta);
                player.sendMessage(Utils.translate(String.format("&b&n%s &ahas been renamed to &a&n%s", item.getType(), str)));
                break;
            }
            case "unbreak": {
                ItemStack item = player.getInventory().getItemInMainHand();
                ItemMeta meta = item.getItemMeta();
                meta.setUnbreakable(true);
                item.setItemMeta(meta);
                player.sendMessage(Utils.translate(String.format("&a Your &n%s&a has been made unbreakable!", item.getType())));
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
                Player p = !strings[1].isEmpty() ? Bukkit.getPlayer(strings[1]) : player;
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
