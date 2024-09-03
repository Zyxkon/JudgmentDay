package com.zyxkon.judgmentday.injuries.impairment;

import com.zyxkon.judgmentday.Main;
import com.zyxkon.judgmentday.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.UUID;


public class ImpairmentListener implements Listener {
    private static ImpairmentManager manager;
    public ImpairmentListener(Main plugin){
        manager = ImpairmentManager.getInstance();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler
    public void onDamage(EntityDamageEvent event){
        Entity entity = event.getEntity();
        if (!(entity instanceof Player)) return;
        Player player = (Player) entity;
        if (player.isDead()) return;
        float percentChance;
        if (player.getLocation().getBlock().getType() == Material.STATIONARY_WATER) {
            percentChance = 5;
            if (Utils.chance(percentChance) && !manager.isInjured(player)) manager.affectPlayer(player);
        }
        ItemStack armor;
        switch (event.getCause()){
            case FALL:
                percentChance = 15;
                armor = player.getEquipment().getLeggings();
                if (armor != null) percentChance = Utils.chanceOfArmor(percentChance, armor.getType());
                if (Utils.chance(percentChance) && !manager.isInjured(player)) manager.affectPlayer(player);
                break;
            case BLOCK_EXPLOSION:
            case ENTITY_EXPLOSION:
                percentChance = 5;
                armor = player.getEquipment().getLeggings();
                if (armor != null) percentChance = Utils.chanceOfArmor(percentChance, armor.getType());
                if (Utils.chance(percentChance) && !manager.isInjured(player)) manager.affectPlayer(player);
                break;
            case SUFFOCATION: {
                percentChance = 5;
                if (Utils.chance(percentChance) && !manager.isInjured(player)) manager.affectPlayer(player);
                break;
            }
        }
    }
    @EventHandler(priority = EventPriority.LOWEST)
    public void onHealing(PlayerInteractEvent event){
        Player player = event.getPlayer();
        ArrayList<Material> remedies = new ArrayList<Material>(){
            {
                add(Material.NETHER_STALK);
                add(Material.MUSHROOM_SOUP);
            }
        };
        if (!remedies.contains(event.getMaterial())) return;
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR)){
            if (manager.healPlayer(player)) Utils.sendActionBarMessage(player,
                    "You bandage your broken legs but it still takes you a while to walk normally again.");
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
        if (!manager.isInjured(uuid)) return;
        manager.getInjury(uuid).cancel();
        manager.put(uuid, null);
    }
    @EventHandler
    public void onJump(PlayerMoveEvent event){
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        Location to = event.getTo();
        Location from = event.getFrom();
        if (!manager.isInjured(uuid)) return;
        if (manager.getInjury(uuid).canJump) return;
        if (to.getY() - from.getY() == 0.41999998688697815) event.setCancelled(true);
    }
    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        Player player = event.getEntity();
        if (manager.isInjured(player) && manager.healPlayer(player)) event.setDeathMessage(String.format("%s walked with a broken bone for too long.", player.getName()));
    }
}
