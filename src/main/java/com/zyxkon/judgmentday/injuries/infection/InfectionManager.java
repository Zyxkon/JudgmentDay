package com.zyxkon.judgmentday.injuries.infection;

import com.zyxkon.judgmentday.Main;
import com.zyxkon.judgmentday.Utils;
import com.zyxkon.judgmentday.injuries.impairment.Impairment;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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
import org.bukkit.potion.PotionEffect;

import java.util.UUID;
import java.util.HashMap;
import java.util.ArrayList;

public class InfectionManager implements Listener {
    private static final HashMap<UUID, Infection> affectedPlayers = new HashMap<>();
    static Main plugin;
    public InfectionManager(Main plugin){
        InfectionManager.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    public static Infection getInjury(UUID uuid){
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
        affectedPlayers.put(uuid, new Infection(plugin, player));
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
            Player player = Bukkit.getPlayer(uuid);
            for(PotionEffect potion : player.getActivePotionEffects()) player.removePotionEffect(potion.getType());
            player.setWalkSpeed(getInjury(uuid).normalSpeed);
            getInjury(uuid).cancel();
        }
    }
    @EventHandler
    public void onDamage(EntityDamageEvent event){
        if (event.getEntity() instanceof Player){
            float percentChance;
            Player player = (Player) event.getEntity();
            ItemStack armor;
            if (event.getCause() == EntityDamageEvent.DamageCause.FALL){
                percentChance = 5;
                armor = player.getEquipment().getBoots();
                if (armor != null) percentChance = Utils.chanceOfArmor(percentChance, armor.getType());
                if (Utils.chance(percentChance) && !isInjured(player)) affectPlayer(player);
            }
            else if (player.getLocation().getBlock().getType() == Material.WEB){
                percentChance = 10;
                armor = player.getEquipment().getLeggings();
                if (armor != null) percentChance = Utils.chanceOfArmor(percentChance, armor.getType());
                if (Utils.chance(percentChance) && !isInjured(player)) affectPlayer(player);
            }
            else if (player.getLocation().add(0, 1, 0).getBlock().getType() == Material.WEB){
                percentChance = 10;
                armor = player.getEquipment().getHelmet();
                if (armor != null) percentChance = Utils.chanceOfArmor(percentChance, armor.getType());
                if (Utils.chance(percentChance) && !isInjured(player)) affectPlayer(player);
            }
        }
    }
    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent event){
        Entity damager = event.getDamager();
        if (event.getEntity() instanceof Player){
            float percentChance;
            Player player = (Player) event.getEntity();
            ItemStack armor;
            if (damager instanceof Zombie) {
                percentChance = 20;
                armor = player.getEquipment().getChestplate();
                if (armor != null) percentChance = Utils.chanceOfArmor(percentChance, armor.getType());
                if (Utils.chance(percentChance) && !isInjured(player)) affectPlayer(player);
            }
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
            if (healPlayer(player)) Utils.sendActionBarMessage(player, "You take an antibiotic. Your infection goes away.");
        }
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        UUID uuid = event.getPlayer().getUniqueId();
        if (isInjured(uuid)) affectPlayer(event.getPlayer());
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
        if (healPlayer(player)) event.setDeathMessage(String.format("%s succumbed to the infection.", player.getName()));
    }
}

