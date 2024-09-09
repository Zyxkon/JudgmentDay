package com.zyxkon.judgmentday.general_listeners;

import com.zyxkon.judgmentday.Main;
import com.zyxkon.judgmentday.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerDeathListener implements Listener {
    Main plugin;
    public PlayerDeathListener(Main plugin){
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        Player player = event.getEntity();
        Location loc = player.getLocation();
        World w = event.getEntity().getWorld();
        Zombie z = w.spawn(loc, Zombie.class);
        ItemStack[] armorContents = player.getEquipment().getArmorContents();
        if (armorContents != null) z.getEquipment().setArmorContents(armorContents);
        z.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(150d);
        z.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.34d);
        z.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(5d);
        z.getAttribute(Attribute.ZOMBIE_SPAWN_REINFORCEMENTS).setBaseValue(100d);
        z.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(1000d);
        z.setHealth(player.getMaxHealth()*5);
        z.setCustomName(Utils.translate(String.format("&4&l☣&c&n%s&4&l☣", player.getName())));
        z.setCustomNameVisible(true);
    }
}
