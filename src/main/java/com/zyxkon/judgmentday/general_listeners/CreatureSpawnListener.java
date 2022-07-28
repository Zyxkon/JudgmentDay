package com.zyxkon.judgmentday.general_listeners;

import com.zyxkon.judgmentday.Main;
import com.zyxkon.judgmentday.Utils;

import com.zyxkon.judgmentday.extensions.WorldGuardExtension;
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

import java.util.ArrayList;

public class CreatureSpawnListener implements Listener {
    Main plugin;
    public CreatureSpawnListener(Main plugin){
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        Location loc = event.getEntity().getLocation();
        if (!loc.getBlock().isEmpty() || !loc.getBlock().getRelative(BlockFace.UP).isEmpty() || loc.getBlock().getRelative(BlockFace.DOWN).isEmpty()) {
            event.setCancelled(true);
            return;
        }
        if (!(event.getEntity() instanceof Zombie)){
            if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL || event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.CUSTOM){
                event.setCancelled(true);
            }
        }
        float lChance = 1/6f*100;
        float gChance = 1/12f*100;
        float cChance = 1/18f*100;
        float iChance = 1/10f;
        if (Bukkit.getPluginManager().getPlugin("WorldGuard") != null){
            ArrayList<String> regions = WorldGuardExtension.getRegion(loc);
            for (String r : regions){
                if (WorldGuardExtension.isSafezone(r)) return;
            }
            for (String r : regions){
                if (WorldGuardExtension.isBarrack(r)) {
                    iChance *= 300;
                    cChance *= 150;
                    lChance /= 10;
                    cChance /= 10;
                    break;
                }
            }
        }
        if (event.getEntity() instanceof Zombie){
            try {
                Ageable a = (Ageable) event.getEntity();
                if (!a.isAdult()) a.setAdult();
            } catch (ClassCastException ignored){}
            Zombie z = (Zombie) event.getEntity();
            z.setBaby(false);
            z.setHealth(10d);
            z.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(100d);
            z.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.3d);
            z.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(7d);
            z.getAttribute(Attribute.ZOMBIE_SPAWN_REINFORCEMENTS).setBaseValue(100d);
            ItemStack helmet, chestplate, leggings, boots;
            helmet = chestplate = leggings = boots = null;
            if (Utils.randBool()) {
                if (Utils.chance(iChance)) helmet = new ItemStack(Material.IRON_HELMET);
                else if (Utils.chance(cChance)) helmet = new ItemStack(Material.CHAINMAIL_HELMET);
                else if (Utils.chance(gChance)) helmet = new ItemStack(Material.GOLD_HELMET);
                else if (Utils.chance(lChance)) helmet = new ItemStack(Material.LEATHER_HELMET);
            }
            if (Utils.randBool()) {
                if (Utils.chance(iChance)) chestplate = new ItemStack(Material.IRON_CHESTPLATE);
                else if (Utils.chance(cChance)) chestplate = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
                else if (Utils.chance(gChance)) chestplate = new ItemStack(Material.GOLD_CHESTPLATE);
                else if (Utils.chance(lChance)) chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
            }
            if (Utils.randBool()) {
                if (Utils.chance(iChance)) leggings = new ItemStack(Material.IRON_LEGGINGS);
                else if (Utils.chance(cChance)) leggings = new ItemStack(Material.CHAINMAIL_LEGGINGS);
                else if (Utils.chance(gChance)) leggings = new ItemStack(Material.GOLD_LEGGINGS);
                else if (Utils.chance(lChance)) leggings = new ItemStack(Material.LEATHER_LEGGINGS);
            }
            if (Utils.randBool()) {
                if (Utils.chance(iChance)) boots = new ItemStack(Material.IRON_BOOTS);
                else if (Utils.chance(cChance)) boots = new ItemStack(Material.CHAINMAIL_BOOTS);
                else if (Utils.chance(gChance)) boots = new ItemStack(Material.GOLD_BOOTS);
                else if (Utils.chance(lChance)) boots = new ItemStack(Material.LEATHER_BOOTS);
            }
            z.getEquipment().setArmorContents(new ItemStack[]{boots, leggings, chestplate, helmet});
        }
    }
}
