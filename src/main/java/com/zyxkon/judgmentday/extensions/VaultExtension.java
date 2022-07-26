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

public class VaultExtension implements Listener {
    static Main plugin;
    private static Economy eco = null;
    public VaultExtension(Main plugin){
        VaultExtension.plugin = plugin;
        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        VaultExtension.eco = rsp.getProvider();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler
    public void onKill(EntityDeathEvent event){
        Entity entity = event.getEntity();
        Player player = event.getEntity().getKiller();
        if (!(entity instanceof Zombie)) return;
        if (player == null) return;
        if (eco == null) return;
        if (!eco.isEnabled()) return;
        if (!eco.hasAccount(player)) return;
        eco.depositPlayer(player, Utils.randRange(0, 300)/100.);
    }
    public static double getMoney(Player player){
        return (eco != null ? eco.getBalance(player) : 0);
    }
}
