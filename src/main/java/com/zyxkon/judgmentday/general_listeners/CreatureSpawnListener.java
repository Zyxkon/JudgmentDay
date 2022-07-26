package com.zyxkon.judgmentday.general_listeners;

import com.zyxkon.judgmentday.Main;
import com.zyxkon.judgmentday.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class CreatureSpawnListener implements Listener {
    Main plugin;
    public CreatureSpawnListener(Main plugin){
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler
    public static void onCreatureSpawn(CreatureSpawnEvent event) {
        Location loc = event.getEntity().getLocation();
        if (!loc.getBlock().isEmpty() || !loc.getBlock().getRelative(BlockFace.UP).isEmpty() || loc.getBlock().getRelative(BlockFace.DOWN).isEmpty()) {
            event.setCancelled(true);
            return;
        }
        if (!(event.getEntity() instanceof Zombie)){
            if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL
                    || event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.CUSTOM){
                event.setCancelled(true);
                EntityType ent = EntityType.ZOMBIE;
                event.getLocation().getWorld().spawnEntity(loc, ent);
            }
        }
        if (event.getEntity() instanceof Zombie){
            try {
                Ageable a = (Ageable) event.getEntity();
                if (!a.isAdult()) a.setAdult();
            } catch (ClassCastException ignored){}
            Random r = new Random();
            Zombie z = (Zombie) event.getEntity();
            z.setBaby(false);
            z.setHealth(10d);
            z.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(100d);
            z.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.3d);
            z.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(7d);
            z.getAttribute(Attribute.ZOMBIE_SPAWN_REINFORCEMENTS).setBaseValue(100d);
            ItemStack helmet = null;
            ItemStack chestplate = null;
            ItemStack leggings = null;
            ItemStack boots = null;
            if (r.nextBoolean()) {
                if (Utils.chance(1/6.)) helmet = new ItemStack(Material.LEATHER_HELMET);
                else if (Utils.chance(1/7.)) helmet = new ItemStack(Material.GOLD_HELMET);
                else if (Utils.chance(1/8.)) helmet = new ItemStack(Material.CHAINMAIL_HELMET);
                else if (Utils.chance(5)) helmet = new ItemStack(Material.IRON_HELMET);
//                else if (Utils.chance(30)) helmet = new ItemStack(Material.DIAMOND_HELMET);
            }
            if (r.nextBoolean()) {
                if (Utils.chance(1/6.)) chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
                else if (Utils.chance(1/7.)) chestplate = new ItemStack(Material.GOLD_CHESTPLATE);
                else if (Utils.chance(1/8.)) chestplate = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
                else if (Utils.chance(5)) chestplate = new ItemStack(Material.IRON_CHESTPLATE);
//                else if (Utils.chance(30)) chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
            }
            if (r.nextBoolean()) {
                if (Utils.chance(1./6.)) leggings = new ItemStack(Material.LEATHER_LEGGINGS);
                else if (Utils.chance(1/7.)) leggings = new ItemStack(Material.GOLD_LEGGINGS);
                else if (Utils.chance(1/8.)) leggings = new ItemStack(Material.CHAINMAIL_LEGGINGS);
                else if (Utils.chance(5)) leggings = new ItemStack(Material.IRON_LEGGINGS);
//                else if (Utils.chance(30)) leggings = new ItemStack(Material.DIAMOND_LEGGINGS);
            }
            if (r.nextBoolean()) {
                if (Utils.chance(1./6.)) boots = new ItemStack(Material.LEATHER_BOOTS);
                else if (Utils.chance(1/7.)) boots = new ItemStack(Material.GOLD_BOOTS);
                else if (Utils.chance(1/8.)) boots = new ItemStack(Material.CHAINMAIL_BOOTS);
                else if (Utils.chance(5)) boots = new ItemStack(Material.IRON_BOOTS);
//                else if (Utils.chance(30)) boots = new ItemStack(Material.DIAMOND_BOOTS);
            }
            z.getEquipment().setHelmet(helmet);
            z.getEquipment().setChestplate(chestplate);
            z.getEquipment().setLeggings(leggings);
            z.getEquipment().setBoots(boots);
        }
    }
}
