package main.zyxkon.judgmentday.injuries.infection;

import main.zyxkon.judgmentday.Utils;
import main.zyxkon.judgmentday.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.*;
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
import org.bukkit.potion.PotionEffect;

import java.util.UUID;
import java.util.HashMap;
import java.util.ArrayList;

public class InfectionManager implements Listener {
    public static HashMap<UUID, Infection> affectedPlayers = new HashMap<>();
    static Main plugin;
    public InfectionManager(Main plugin){
        InfectionManager.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
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
                if (Utils.chance(percentChance) && !affectedPlayers.containsKey(player.getUniqueId())) affectPlayer(player);
            }
            else if (player.getLocation().getBlock().getType() == Material.WEB){
                percentChance = 10;
                armor = player.getEquipment().getLeggings();
                if (armor != null) percentChance = Utils.chanceOfArmor(percentChance, armor.getType());
                if (Utils.chance(percentChance) && !affectedPlayers.containsKey(player.getUniqueId())) affectPlayer(player);
            }
            else if (player.getLocation().add(0, 1, 0).getBlock().getType() == Material.WEB){
                percentChance = 10;
                armor = player.getEquipment().getHelmet();
                if (armor != null) percentChance = Utils.chanceOfArmor(percentChance, armor.getType());
                if (Utils.chance(percentChance) && !affectedPlayers.containsKey(player.getUniqueId())) affectPlayer(player);
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
                if (Utils.chance(percentChance) && !affectedPlayers.containsKey(player.getUniqueId())) affectPlayer(player);
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
        if (remedies.contains(event.getMaterial())){
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR){
                if (healPlayer(player)) Utils.sendActionBarMessage(player, "You take an antibiotic. Your infection goes away.");
            }
        }
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        UUID uuid = event.getPlayer().getUniqueId();
        if (affectedPlayers.containsKey(uuid)) affectPlayer(event.getPlayer());
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        UUID uuid = event.getPlayer().getUniqueId();
        if (affectedPlayers.containsKey(uuid)) {
            affectedPlayers.get(uuid).cancel();
            affectedPlayers.put(uuid, null);
        }
    }
    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        Player player = event.getEntity();
        if (healPlayer(player)) event.setDeathMessage(String.format("%s succumbed to the infection.", player.getName()));
    }
    public static void affectPlayer(Player player){
        UUID uuid = player.getUniqueId();
        affectedPlayers.put(uuid, new Infection(plugin, player));
    }
    public static boolean healPlayer(Player player){
        UUID uuid = player.getUniqueId();
        if (affectedPlayers.containsKey(uuid)) {
            affectedPlayers.get(uuid).cancel();
            affectedPlayers.remove(uuid);
            return true;
        }
        return false;
    }
    public static void shutDown(){
        for (UUID uuid : affectedPlayers.keySet()){
            Player player = Bukkit.getPlayer(uuid);
            for(PotionEffect potion : player.getActivePotionEffects()) player.removePotionEffect(potion.getType());
            player.setWalkSpeed(affectedPlayers.get(uuid).normalSpeed);
            affectedPlayers.get(uuid).cancel();
        }
    }
}

