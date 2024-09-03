package com.zyxkon.judgmentday.injuries.bloodloss;

import com.zyxkon.judgmentday.Main;
import com.zyxkon.judgmentday.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.UUID;

public class BloodLossListener implements Listener {
    private static BloodLossManager manager;
    public BloodLossListener(Main plugin){
        manager = BloodLossManager.getInstance();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler
    public void onDamage(EntityDamageEvent event){
        Entity entity = event.getEntity();
        if (!(entity instanceof Player)) return;
        Player player = (Player) entity;
        if (player.isDead()) return;
        float percentChance;
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL){
            percentChance = 5;
            if (player.getEquipment().getBoots() != null) percentChance = Utils.chanceOfArmor(percentChance, player.getEquipment().getBoots().getType());
            if (Utils.chance(percentChance) && !manager.isInjured(player)) manager.affectPlayer(player);
        }
        else if (player.getLocation().getBlock().getType() == Material.WEB){
            percentChance = 5;
            ItemStack armor = player.getEquipment().getLeggings();
            if (armor != null) percentChance = Utils.chanceOfArmor(percentChance, armor.getType());
            if (Utils.chance(percentChance) && !manager.isInjured(player)) manager.affectPlayer(player);
        }
        else if (player.getLocation().add(0, 1, 0).getBlock().getType() == Material.WEB){
            percentChance = 5;
            ItemStack armor = player.getEquipment().getHelmet();
            if (armor != null) percentChance = Utils.chanceOfArmor(percentChance, player.getEquipment().getHelmet().getType());
            if (Utils.chance(percentChance) && !manager.isInjured(player)) manager.affectPlayer(player);
        }
        else if (event.getCause() == EntityDamageEvent.DamageCause.SUFFOCATION){
            percentChance = 5;
            if (Utils.chance(percentChance) && !manager.isInjured(player)) manager.affectPlayer(player);
        }
    }
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event){
        Entity victim = event.getEntity();
        Entity damager = event.getDamager();
        if (!(victim instanceof Player)) return;
        Player player = (Player) victim;
        if (!(damager instanceof Zombie)) return;
        if (victim.isDead()) return;
        float percentChance;
        percentChance = 20;
        if (player.getEquipment().getChestplate() != null) percentChance = Utils.chanceOfArmor(percentChance, player.getEquipment().getChestplate().getType());
        if (Utils.chance(percentChance) && !manager.isInjured(player)) manager.affectPlayer(player);
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
            if (manager.healPlayer(player)) Utils.sendActionBarMessage(player, "You closed your open wound, the bleeding stops.");
        }
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        if (manager.isInjured(player)) manager.affectPlayer(player);
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        UUID uuid = event.getPlayer().getUniqueId();
        if (!manager.isInjured(uuid)) return;
        manager.getInjury(uuid).cancel();
        manager.put(uuid, null);
    }
    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        Player player = event.getEntity();
        if (manager.isInjured(player) && manager.healPlayer(player)) event.setDeathMessage(String.format("%s lost too much blood.", player.getName()));
    }
}
