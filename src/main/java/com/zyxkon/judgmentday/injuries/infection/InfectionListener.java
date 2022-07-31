package com.zyxkon.judgmentday.injuries.infection;

import com.zyxkon.judgmentday.Utils;
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

public class InfectionListener implements Listener {
    private static InfectionManager manager;
    public InfectionListener(){
        manager = InfectionManager.getInstance();
    }
    @EventHandler
    public void onDamage(EntityDamageEvent event){
        if (!(event.getEntity() instanceof Player)) return;
        float percentChance;
        Player player = (Player) event.getEntity();
        if (player.isDead()) return;
        ItemStack armor;
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL){
            percentChance = 5;
            armor = player.getEquipment().getBoots();
            if (armor != null) percentChance = Utils.chanceOfArmor(percentChance, armor.getType());
            if (Utils.chance(percentChance) && !manager.isInjured(player)) manager.affectPlayer(player);
        }
        else if (player.getLocation().getBlock().getType() == Material.WEB){
            percentChance = 10;
            armor = player.getEquipment().getLeggings();
            if (armor != null) percentChance = Utils.chanceOfArmor(percentChance, armor.getType());
            if (Utils.chance(percentChance) && !manager.isInjured(player)) manager.affectPlayer(player);
        }
        else if (player.getLocation().add(0, 1, 0).getBlock().getType() == Material.WEB){
            percentChance = 10;
            armor = player.getEquipment().getHelmet();
            if (armor != null) percentChance = Utils.chanceOfArmor(percentChance, armor.getType());
            if (Utils.chance(percentChance) && !manager.isInjured(player)) manager.affectPlayer(player);
        }
    }
    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent event){
        Entity damager = event.getDamager();
        if (!(event.getEntity() instanceof Player)) return;
        float percentChance;
        Player player = (Player) event.getEntity();
        if (player.isDead()) return;
        ItemStack armor;
        if (damager instanceof Zombie) {
            percentChance = 20;
            armor = player.getEquipment().getChestplate();
            if (armor != null) percentChance = Utils.chanceOfArmor(percentChance, armor.getType());
            if (Utils.chance(percentChance) && !manager.isInjured(player)) manager.affectPlayer(player);
        }
    }
    @EventHandler
    public void onHealing(PlayerInteractEvent event){
        Player player = event.getPlayer();
        ArrayList<Material> remedies = new ArrayList<Material>(){
            {
                add(Material.GLOWSTONE_DUST);
                add(Material.MUSHROOM_SOUP);
            }
        };
        if (!remedies.contains(event.getMaterial())) return;
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR){
            if (manager.healPlayer(player)) Utils.sendActionBarMessage(player, "You take an antibiotic. Your infection goes away.");
        }
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        UUID uuid = event.getPlayer().getUniqueId();
        if (manager.isInjured(uuid)) manager.affectPlayer(event.getPlayer());
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        UUID uuid = event.getPlayer().getUniqueId();
        if (manager.isInjured(uuid)) {
            manager.getInjury(uuid).cancel();
            manager.put(uuid, null);
        }
    }
    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        Player player = event.getEntity();
        if (manager.healPlayer(player)) event.setDeathMessage(String.format("%s succumbed to the infection.", player.getName()));
    }
}
