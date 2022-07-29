package com.zyxkon.judgmentday.extensions;
import com.zyxkon.judgmentday.Main;
import com.zyxkon.judgmentday.Utils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.text.DecimalFormat;

public class VaultExtension implements Listener {
    static Main plugin;
    private static Economy eco = null;
    public VaultExtension(Main plugin){
        VaultExtension.plugin = plugin;
        if (plugin.hasPlugin("Vault")){
            RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
            VaultExtension.eco = rsp.getProvider();
            Bukkit.getPluginManager().registerEvents(this, plugin);
        }
    }
    @EventHandler
    public void onKill(EntityDeathEvent event){
        event.getDrops().clear();
        event.setDroppedExp(0);
        Entity entity = event.getEntity();
        Player player = event.getEntity().getKiller();
        if (!(entity instanceof Zombie)) return;
        if (player == null) return;
        if (eco == null) return;
        if (!eco.isEnabled()) return;
        if (!eco.hasAccount(player)) return;
        double money = Utils.randRange(0, 300)/100.;
        DecimalFormat df = new DecimalFormat("#.##");
        Utils.sendActionBarMessage(player, Utils.translate(String.format("&a&l+&a%s&2$",df.format(money))));
        eco.depositPlayer(player, money);
    }
    public static double getMoney(Player player){
        return (eco != null ? eco.getBalance(player) : 0);
    }
}
