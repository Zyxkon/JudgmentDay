package zyxkon.judgmentday.general_listeners;

import zyxkon.judgmentday.Main;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.stream.Collectors;

public class EntityDamageListener implements Listener {
    Main plugin;
    public EntityDamageListener(Main plugin){
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler
    public static void onDamageByEntity(EntityDamageByEntityEvent event){
        Entity victim = event.getEntity();
        Entity attacker = event.getDamager();
        if (victim instanceof Player && attacker instanceof Zombie){
            Player player = (Player) victim;
            PotionEffect slow = new PotionEffect(PotionEffectType.SLOW, 4*20, 0);
            PotionEffect nausea = new PotionEffect(PotionEffectType.CONFUSION, 6*20, 0);
            PotionEffect[] pot = {slow, nausea};
            player.addPotionEffects(Arrays.stream(pot).collect(Collectors.toList()));
        }
    }
    @EventHandler
    public void onDamage(EntityDamageEvent event){
        Entity victim = event.getEntity();
        if (victim instanceof Zombie && event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            event.setCancelled(true);
            ((Zombie) victim).damage(event.getDamage()/10);
        }
    }
}
