package com.zyxkon.judgmentday.general_listeners;

import com.zyxkon.judgmentday.Main;
import com.zyxkon.judgmentday.Utils;

import com.zyxkon.judgmentday.extensions.WorldGuardExtension;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.logging.Level;

public class CreatureSpawnListener implements Listener {
    Main plugin;
    public CreatureSpawnListener(Main plugin){
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        Location loc = event.getEntity().getLocation();
        if (Utils.isSolid(loc)) {
            event.setCancelled(true);
            return;
        }
        if (Utils.isSolid(loc.getBlock().getRelative(BlockFace.UP))) {
            event.setCancelled(true);
            return;
        }
        if (!Utils.isSolid(loc.getBlock().getRelative(BlockFace.DOWN))){
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
            ArrayList<String> regions = WorldGuardExtension.getRegions(loc);
            for (String r : regions){
                if (WorldGuardExtension.isSafezone(r) && event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.CUSTOM) {
                    event.setCancelled(true);
                    return;
                }
                else if (WorldGuardExtension.isBarracks(r)) {
                    iChance *= 750;
                    cChance *= 5;
                    lChance /= 100;
                    gChance /= 200;
                    break;
                }
            }
        }
        if (event.getEntity() instanceof Zombie){
//            try {
//                Ageable a = (Ageable) event.getEntity();
//                if (!a.isAdult()) a.setAdult();
//            } catch (ClassCastException ignored){}
            Zombie z = (Zombie) event.getEntity();
            z.setBaby(false);
            z.setHealth(15d);
            z.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(100d);
            z.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.3d);
            z.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(4d);
            ItemStack helmet, chestplate, leggings, boots;
//            helmet = chestplate = leggings = boots = null;
            ArrayList<ItemStack> helmetList = new ArrayList<>();
            ArrayList<ItemStack> chestplateList = new ArrayList<>();
            ArrayList<ItemStack> leggingsList = new ArrayList<>();
            ArrayList<ItemStack> bootsList = new ArrayList<>();
            if (Utils.randBool()) {
                if (Utils.chance(iChance)) helmetList.add(new ItemStack(Material.IRON_HELMET));
                if (Utils.chance(cChance)) helmetList.add(new ItemStack(Material.CHAINMAIL_HELMET));
                if (Utils.chance(gChance)) helmetList.add(new ItemStack(Material.GOLD_HELMET));
                if (Utils.chance(lChance)) helmetList.add(new ItemStack(Material.LEATHER_HELMET));
            }
            if (Utils.randBool()) {
                if (Utils.chance(iChance)) chestplateList.add(new ItemStack(Material.IRON_CHESTPLATE));
                if (Utils.chance(cChance)) chestplateList.add(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
                if (Utils.chance(gChance)) chestplateList.add(new ItemStack(Material.GOLD_CHESTPLATE));
                if (Utils.chance(lChance)) chestplateList.add(new ItemStack(Material.LEATHER_CHESTPLATE));
            }
            if (Utils.randBool()) {
                if (Utils.chance(iChance)) leggingsList.add(new ItemStack(Material.IRON_LEGGINGS));
                if (Utils.chance(cChance)) leggingsList.add(new ItemStack(Material.CHAINMAIL_LEGGINGS));
                if (Utils.chance(gChance)) leggingsList.add(new ItemStack(Material.GOLD_LEGGINGS));
                if (Utils.chance(lChance)) leggingsList.add(new ItemStack(Material.LEATHER_LEGGINGS));
            }
            if (Utils.randBool()) {
                if (Utils.chance(iChance)) bootsList.add(new ItemStack(Material.IRON_BOOTS));
                if (Utils.chance(cChance)) bootsList.add(new ItemStack(Material.CHAINMAIL_BOOTS));
                if (Utils.chance(gChance)) bootsList.add(new ItemStack(Material.GOLD_BOOTS));
                if (Utils.chance(lChance)) bootsList.add(new ItemStack(Material.LEATHER_BOOTS));
            }
            helmet = Utils.randElement(helmetList);
            chestplate = Utils.randElement(chestplateList);
            leggings = Utils.randElement(leggingsList);
            boots = Utils.randElement(bootsList);
            ItemStack[] armor = new ItemStack[]{boots, leggings, chestplate, helmet};
            for (ItemStack item : armor){
                if (Utils.randBool() && item != null){
                    ItemMeta meta = item.getItemMeta();
                    meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 5, true);
                    item.setItemMeta(meta);
                }
            }
            z.getEquipment().setArmorContents(armor);
        }
    }
}
