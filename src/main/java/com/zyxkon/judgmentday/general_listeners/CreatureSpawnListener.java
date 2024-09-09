package com.zyxkon.judgmentday.general_listeners;

import com.zyxkon.judgmentday.Main;
import com.zyxkon.judgmentday.Utils;

import com.zyxkon.judgmentday.extensions.Extension;
import com.zyxkon.judgmentday.extensions.WorldGuardExtension;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Banner;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftZombie;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.List;

public class CreatureSpawnListener implements Listener {
    Main plugin;
    public CreatureSpawnListener(Main plugin){
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
//        plugin.log(Level.WARNING, String.format("A %s just spawned because of %s", event.getEntity().getName(), event.getSpawnReason().toString()));
        Entity ent = event.getEntity();
        World w = ent.getWorld();
        Location loc = ent.getLocation();
        CreatureSpawnEvent.SpawnReason reason = event.getSpawnReason();
        if (reason == CreatureSpawnEvent.SpawnReason.SPAWNER_EGG) return;
//        if (reason == CreatureSpawnEvent.SpawnReason.SPAWNER_EGG){
//            WorldServer nmsWorld = ((CraftWorld) w).getHandle();
//            if (ent instanceof Skeleton && !(ent instanceof Stray)){
//                event.setCancelled(true);
//                CustomEntitySkeleton r = new CustomEntitySkeleton(w);
//                r.setPosition(loc.getX(), loc.getY(), loc.getZ());
//                nmsWorld.addEntity(r, CreatureSpawnEvent.SpawnReason.CUSTOM);
//                r.setInvisible(true);
//                w.spawnParticle(Particle.BARRIER, loc, 100);
//                Main.testBroadcast("new %s at (%d, %d, %d)", r.getBukkitEntity().getName(),(int) loc.getX(), (int) loc.getY(), (int) loc.getZ());
//            }
//            if (ent instanceof Stray){
//                event.setCancelled(true);
//                Runner r = new Runner(w);
//                r.setPosition(loc.getX(), loc.getY(), loc.getZ());
//                nmsWorld.addEntity(r, CreatureSpawnEvent.SpawnReason.CUSTOM);
//                w.spawnParticle(Particle.BARRIER, loc, 100);
//                Main.testBroadcast("new %s at (%d, %d, %d)", r.getBukkitEntity().getName(),(int) loc.getX(), (int) loc.getY(), (int) loc.getZ());
//            }
//            if (ent instanceof Cow){
//                event.setCancelled(true);
//                MadCow c = new MadCow(w);
//                c.setPosition(loc.getX(), loc.getY(), loc.getZ());
//                nmsWorld.addEntity(c, CreatureSpawnEvent.SpawnReason.CUSTOM);
//                w.spawnParticle(Particle.CRIT_MAGIC, loc, 100);
//                Main.testBroadcast("new mad ass cow %s at (%d, %d, %d)", c.getBukkitEntity().getName(),(int) loc.getX(), (int) loc.getY(), (int) loc.getZ());
//            }
//        }
        if (reason == CreatureSpawnEvent.SpawnReason.NATURAL){
            if (ent instanceof Monster || ent instanceof Animals){
                event.setCancelled(true);
                w.spawnEntity(loc, EntityType.ZOMBIE);
                return;
            }
        }
        float lChance = 1/2f*100;
        float cChance = 1/3f*100;
        float gChance = 1/4f*100;
        float iChance = 1/5f*100;
        boolean isBarracks = false;
        if (Extension.WORLDGUARD.isLoaded()){
            ArrayList<String> regions = WorldGuardExtension.getRegions(loc);
            for (String r : regions){
                if (WorldGuardExtension.isSafezone(r) && event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.CUSTOM) {
                    event.setCancelled(true);
                    return;
                }
                else if (WorldGuardExtension.isBarracks(r)) {
                    isBarracks = true;
                    iChance *= 750;
                    cChance *= 5;
                    gChance /= 100;
                    break;
                }
            }
        }
        Zombie z;
        try {
            z = (Zombie) event.getEntity();
        } catch (ClassCastException ignored) {
            return;
        }
        z.setHealth(15d);
        z.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(100d);
        if (!z.isBaby()) {
                z.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(
                    z.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getValue()*1.5
            );
        }
        z.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(
                z.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getValue()*1.5
        );
        ArrayList<ItemStack> helmetList = new ArrayList<>();
        ArrayList<ItemStack> chestplateList = new ArrayList<>();
        ArrayList<ItemStack> leggingsList = new ArrayList<>();
        ArrayList<ItemStack> bootsList = new ArrayList<>();
        ItemStack lHelmet = new ItemStack(Material.LEATHER_HELMET);
        ItemStack lChestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        ItemStack lLeggings = new ItemStack(Material.LEATHER_LEGGINGS);
        ItemStack lBoots = new ItemStack(Material.LEATHER_BOOTS);
        ItemStack[] lArmor = {lBoots, lLeggings, lChestplate, lHelmet};
        for (ItemStack a : lArmor){
            LeatherArmorMeta l = (LeatherArmorMeta)(a.getItemMeta());
            l.setColor(Utils.randColor(2));
            a.setItemMeta(l);
        }
        if (Utils.randBool()) {
            if (Utils.chance(iChance)) helmetList.add(new ItemStack(Material.IRON_HELMET));
            if (Utils.chance(cChance)) helmetList.add(new ItemStack(Material.CHAINMAIL_HELMET));
            if (Utils.chance(gChance)) helmetList.add(new ItemStack(Material.GOLD_HELMET));
            if (Utils.chance(lChance)) helmetList.add(lHelmet);
        }
        if (Utils.randBool()) {
            if (Utils.chance(iChance)) chestplateList.add(new ItemStack(Material.IRON_CHESTPLATE));
            if (Utils.chance(cChance)) chestplateList.add(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
            if (Utils.chance(gChance)) chestplateList.add(new ItemStack(Material.GOLD_CHESTPLATE));
            if (Utils.chance(lChance)) chestplateList.add(lChestplate);
        }
        if (Utils.randBool()) {
            if (Utils.chance(iChance)) leggingsList.add(new ItemStack(Material.IRON_LEGGINGS));
            if (Utils.chance(cChance)) leggingsList.add(new ItemStack(Material.CHAINMAIL_LEGGINGS));
            if (Utils.chance(gChance)) leggingsList.add(new ItemStack(Material.GOLD_LEGGINGS));
            if (Utils.chance(lChance)) leggingsList.add(lLeggings);
        }
        if (Utils.randBool()) {
            if (Utils.chance(iChance)) bootsList.add(new ItemStack(Material.IRON_BOOTS));
            if (Utils.chance(cChance)) bootsList.add(new ItemStack(Material.CHAINMAIL_BOOTS));
            if (Utils.chance(gChance)) bootsList.add(new ItemStack(Material.GOLD_BOOTS));
            if (Utils.chance(lChance)) bootsList.add(lBoots);
        }
        ItemStack[] armor = new ItemStack[]{
                Utils.randElement(bootsList), Utils.randElement(leggingsList),
                Utils.randElement(chestplateList), Utils.randElement(helmetList)
        };
        // armor order is ALWAYS boots, leggings, chestplate, helmet
        if (isBarracks){
            if (Utils.chance(10)) {
                armor = new ItemStack[]{
                        new ItemStack(Material.IRON_BOOTS), new ItemStack(Material.IRON_LEGGINGS),
                        new ItemStack(Material.IRON_CHESTPLATE), new ItemStack(Material.IRON_HELMET)
                };
            }
            if (Utils.chance(25)) {
                ItemStack shield = new ItemStack(Material.SHIELD);
                BlockStateMeta shieldBsm = (BlockStateMeta) shield.getItemMeta();
                Banner banner = (Banner) shieldBsm.getBlockState();
                List<Pattern> patterns = new ArrayList<Pattern>(){
                    {
                        add(new Pattern(DyeColor.WHITE,PatternType.STRIPE_TOP));
                        add(new Pattern(DyeColor.GRAY,PatternType.STRIPE_MIDDLE));
                        add(new Pattern(DyeColor.BLACK,PatternType.BORDER));
                    }
                };
                banner.setPatterns(patterns);
                shieldBsm.setBlockState(banner);
                shield.setItemMeta(shieldBsm);
                if (Utils.randBool()) z.getEquipment().setItemInMainHand(shield);
                else z.getEquipment().setItemInOffHand(shield);
            }
        }
        for (ItemStack item : armor){
            if (item != null){
                item.setDurability((short) 3);
                if (Utils.randBool()) {
                    ItemMeta meta = item.getItemMeta();
                    meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 5, true);
                    item.setItemMeta(meta);
                }
            }
        }
        z.getEquipment().setArmorContents(armor);

    }
}
