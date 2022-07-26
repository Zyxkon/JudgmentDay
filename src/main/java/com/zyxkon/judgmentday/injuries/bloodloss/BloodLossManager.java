package com.zyxkon.judgmentday.injuries.bloodloss;

import com.zyxkon.judgmentday.Utils;
import com.zyxkon.judgmentday.Main;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;


public class BloodLossManager implements Listener {
    private static final Map<UUID, BloodLoss> affectedPlayers = new HashMap<>();
    static Main plugin;
    public BloodLossManager(Main plugin){
        BloodLossManager.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    public static BloodLoss getInjury(UUID uuid){
        return affectedPlayers.get(uuid);
    }
    public static boolean isInjured(Player player){
        return isInjured(player.getUniqueId());
    }
    public static boolean isInjured(UUID uuid){
        return affectedPlayers.containsKey(uuid);
    }
    public static void affectPlayer(Player player){
        UUID uuid = player.getUniqueId();
        affectedPlayers.put(uuid, new BloodLoss(plugin, player));
    }
    public static boolean healPlayer(Player player){
        UUID uuid = player.getUniqueId();
        if (isInjured(uuid)) {
            getInjury(uuid).cancel();
            affectedPlayers.remove(uuid);
            return true;
        }
        return false;
    }
    public static void shutDown(){
        for (UUID uuid : affectedPlayers.keySet()){
            if (getInjury(uuid) != null) getInjury(uuid).cancel();
        }
    }
    @EventHandler
    public void onDamage(EntityDamageEvent event){
        Entity entity = event.getEntity();
        if (entity instanceof Player){
            Player player = (Player) entity;
            float percentChance;
            if (event.getCause() == EntityDamageEvent.DamageCause.FALL){
                percentChance = 5;
                if (player.getEquipment().getBoots() != null) percentChance = Utils.chanceOfArmor(percentChance, player.getEquipment().getBoots().getType());
                if (Utils.chance(percentChance) && !isInjured(player)) affectPlayer(player);
            }
            else if (player.getLocation().getBlock().getType() == Material.WEB){
                percentChance = 5;
                ItemStack armor = player.getEquipment().getLeggings();
                if (armor != null) percentChance = Utils.chanceOfArmor(percentChance, armor.getType());
                if (Utils.chance(percentChance) && !isInjured(player)) affectPlayer(player);
            }
            else if (player.getLocation().add(0, 1, 0).getBlock().getType() == Material.WEB){
                percentChance = 5;
                ItemStack armor = player.getEquipment().getHelmet();
                if (armor != null) percentChance = Utils.chanceOfArmor(percentChance, player.getEquipment().getHelmet().getType());
                if (Utils.chance(percentChance) && !isInjured(player)) affectPlayer(player);
            }
            else if (event.getCause() == EntityDamageEvent.DamageCause.SUFFOCATION){
                percentChance = 5;
                if (Utils.chance(percentChance) && !isInjured(player)) affectPlayer(player);
            }
        }
    }
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event){
        Entity victim = event.getEntity();
        Entity damager = event.getDamager();
        if (victim instanceof Player){
            float percentChance;
            Player player = (Player) victim;
            if (damager instanceof Zombie){
                percentChance = 20;
                if (player.getEquipment().getChestplate() != null) percentChance = Utils.chanceOfArmor(percentChance, player.getEquipment().getChestplate().getType());
                if (Utils.chance(percentChance) && !isInjured(player)) affectPlayer(player);
            }
        }
    }
    @EventHandler
    public void onHealing(PlayerInteractEvent event){
        Player player = event.getPlayer();
        ArrayList<Material> remedies = new ArrayList<Material>(){
            {
                add(Material.NETHER_STALK);
                add(Material.MUSHROOM_SOUP);
            }
        };
        if (!remedies.contains(event.getMaterial())) return;
        Action action = event.getAction();
        if (action.equals(Action.RIGHT_CLICK_BLOCK) || action.equals(Action.RIGHT_CLICK_AIR)) {
            Utils.healPlayer(player, 8d);
            if (healPlayer(player)) Utils.sendActionBarMessage(player, "You closed your open wound, the bleeding stops.");
        }
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        if (isInjured(player)) affectPlayer(player);
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        UUID uuid = event.getPlayer().getUniqueId();
        if (isInjured(uuid)) {
            getInjury(uuid).cancel();
            affectedPlayers.put(uuid, null);
        }
    }
    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        Player player = event.getEntity();
        if (healPlayer(player)) event.setDeathMessage(String.format("%s lost too much blood.", player.getName()));
    }
}

