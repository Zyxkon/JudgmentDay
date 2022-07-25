package com.zyxkon.judgmentday.injuries.impairment;

import com.zyxkon.judgmentday.Main;
import com.zyxkon.judgmentday.Utils;
import com.zyxkon.judgmentday.injuries.bloodloss.BloodLoss;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class ImpairmentManager implements Listener {
    private static final Map<UUID, Impairment> affectedPlayers = new HashMap<>();
    static Main plugin;
    public ImpairmentManager(final Main plugin){
        ImpairmentManager.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    public static Impairment getInjury(UUID uuid){
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
        affectedPlayers.put(uuid, new Impairment(plugin, player));
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
            player.setWalkSpeed(getInjury(uuid).normalSpeed);
            getInjury(uuid).cancel();
        }
    }
    @EventHandler
    public void onHurt(EntityDamageEvent event){
        Entity entity = event.getEntity();
        if (entity instanceof Player){
            Player player = (Player) entity;
            float percentChance;
            if (player.getLocation().getBlock().getType() == Material.STATIONARY_WATER) {
                percentChance = 5;
                if (Utils.chance(percentChance) && !isInjured(player)) affectPlayer(player);
            }
            else if (event.getCause() == EntityDamageEvent.DamageCause.FALL){
                percentChance = 15;
                ItemStack armor = player.getEquipment().getLeggings();
                if (armor != null) percentChance = Utils.chanceOfArmor(percentChance, armor.getType());
                if (Utils.chance(percentChance) && !isInjured(player)) affectPlayer(player);
            }
            else if (event.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION || event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION){
                percentChance = 5;
                ItemStack armor = player.getEquipment().getLeggings();
                if (armor != null) percentChance = Utils.chanceOfArmor(percentChance, armor.getType());
                if (Utils.chance(percentChance) && !isInjured(player)) affectPlayer(player);
            }
            else if (event.getCause() == EntityDamageEvent.DamageCause.SUFFOCATION){
                percentChance = 5;
                if (Utils.chance(percentChance) && !isInjured(player)) affectPlayer(player);
            }
        }
    }
    @EventHandler
    public void onHealing(PlayerInteractEvent event){
        Player player = event.getPlayer();
        ArrayList<Material> remedies = new ArrayList<Material>(){
            {
                add(Material.NETHER_WARTS);
                add(Material.MUSHROOM_SOUP);
            }
        };
        if (remedies.contains(event.getMaterial())){
            if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR)){
                if (healPlayer(player)) Utils.sendActionBarMessage(player,
                        "You bandage your broken legs but it still takes you a while to walk normally again.");
            }
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
    public void onJump(PlayerMoveEvent event){
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        Location to = event.getTo();
        Location from = event.getFrom();
        if (isInjured(uuid)) {
            if (!getInjury(uuid).canJump) {
                if (to.getY() - from.getY() == 0.41999998688697815) event.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onRespawn(PlayerRespawnEvent event){
        Player player = event.getPlayer();
        player.setWalkSpeed(player.getWalkSpeed());
    }
    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        Player player = event.getEntity();
        if (healPlayer(player)) event.setDeathMessage(String.format("%s walked with a broken bone for too long.", player.getName()));
    }
}

