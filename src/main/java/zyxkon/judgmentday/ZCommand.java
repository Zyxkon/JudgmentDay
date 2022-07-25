package zyxkon.judgmentday;

import zyxkon.judgmentday.injuries.bloodloss.BloodLossManager;
import zyxkon.judgmentday.injuries.impairment.ImpairmentManager;
import zyxkon.judgmentday.injuries.infection.InfectionManager;
import zyxkon.judgmentday.thirst.ThirstManager;
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

import static zyxkon.judgmentday.Utils.translate;

public class ZCommand implements CommandExecutor {
    Main plugin;
    public ZCommand(Main plugin){
        this.plugin = plugin;
        plugin.getCommand("zyxkon").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)){
            commandSender.sendMessage(translate("&cOnly &nplayers&c can use this command!"));
            return true;
        }
        if (strings.length == 0){
            commandSender.sendMessage("Plugin được phát triển bởi Zyxkon");
        }
        /*
        /zyxkon effect <amplifier> <duration> <effect>
        /zyxkon enchant <enchantment> <level>
        /zyxkon rename <name>
        /zyxkon unbreak
        /zyxkon thirst <player> <amount>
        /zyxkon injure <player> [bloodloss|impairment|infection]
        */
        else {
            Player player = (Player) commandSender;
            if (strings[0].equalsIgnoreCase("effect")){
                ItemStack item = player.getInventory().getItemInMainHand();
                PotionMeta meta = (PotionMeta) item.getItemMeta();
                int amplifier = Integer.parseInt(strings[1]);
                int duration = Integer.parseInt(strings[2]);
                PotionEffect effect = PotionEffectType.getByName(strings[3]).createEffect(duration, amplifier);
                meta.addCustomEffect(effect, true);
                item.setItemMeta(meta);
                player.sendMessage(String.format("%s %s %s has been applied to %s", effect.getType(), amplifier, duration,
                        meta.getDisplayName()));
                return true;
            }
            else if (strings[0].equalsIgnoreCase("enchant")){
                ItemStack item = player.getInventory().getItemInMainHand();
                ItemMeta meta = item.getItemMeta();
                Enchantment ench = Enchantment.getByName((strings[1]).toUpperCase());
                int lvl = Integer.parseInt(strings[2]);
                meta.addEnchant(ench, lvl, true);
                item.setItemMeta(meta);
                player.sendMessage(translate(String.format("&n%s &ahas been enchanted with &b%s&a of level &c%s", meta.getDisplayName(), ench.getName(), lvl)));
            }
            else if (strings[0].equalsIgnoreCase("rename")){
                ItemStack item = player.getInventory().getItemInMainHand();
                ItemMeta meta = item.getItemMeta();
                String str = Utils.translate("&r" + String.join(" ", Arrays.copyOfRange(strings, 1, strings.length)));
                meta.setDisplayName(str);
                item.setItemMeta(meta);
                player.sendMessage(Utils.translate(String.format("&b&n%s &ahas been renamed to &a&n%s", item.getType(), str)));
                return true;
            }
            else if (strings[0].equalsIgnoreCase("unbreak")){
                ItemStack item = player.getInventory().getItemInMainHand();
                ItemMeta meta = item.getItemMeta();
                meta.setUnbreakable(true);
                item.setItemMeta(meta);
                player.sendMessage(translate(String.format("&a Your &n%s&a has been made unbreakable!", item.getType())));
                return true;
            }
            else if (strings[0].equalsIgnoreCase("thirst")){
                Player p = Bukkit.getPlayer(strings[1]);
                int thirst = Integer.parseInt(strings[2]);
                ThirstManager.setThirst(p, thirst);
                return true;
            }
            else if (strings[0].equalsIgnoreCase("injure")){
                Player p = Bukkit.getPlayer(strings[1]);
                UUID uuid = p.getUniqueId();
                if (strings[2].equalsIgnoreCase("bloodloss") && !BloodLossManager.affectedPlayers.containsKey(uuid)) {
                    BloodLossManager.affectPlayer(p);
                    p.sendMessage("You have been inflicted with blood loss!");
                }
                else if (strings[2].equalsIgnoreCase("impairment") && !ImpairmentManager.affectedPlayers.containsKey(uuid)) {
                    ImpairmentManager.affectPlayer(p);
                    p.sendMessage("You have been inflicted with impairment!");
                }
                else if (strings[2].equalsIgnoreCase("infection") && !InfectionManager.affectedPlayers.containsKey(uuid)) {
                    InfectionManager.affectPlayer(p);
                    p.sendMessage("You have been inflicted with an infection!");
                }
                else player.sendMessage("Unknown injury!");
            }
        }
        return true;
    }
}
