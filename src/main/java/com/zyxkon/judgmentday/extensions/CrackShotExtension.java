package com.zyxkon.judgmentday.extensions;

import com.shampaggon.crackshot.CSUtility;
import com.shampaggon.crackshot.events.WeaponDamageEntityEvent;
import com.shampaggon.crackshot.events.WeaponHitBlockEvent;
import com.sk89q.worldedit.blocks.ItemType;
import com.zyxkon.judgmentday.Main;
import com.zyxkon.judgmentday.Utils;
import com.zyxkon.judgmentday.injuries.bloodloss.BloodLossManager;
import com.zyxkon.judgmentday.injuries.infection.InfectionManager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

public class CrackShotExtension implements Listener {
    static Main plugin;
    private static BloodLossManager bloodLossManager;
    private static InfectionManager infectionManager;
    private static CSUtility utility;
    public CrackShotExtension(Main plugin){
        CrackShotExtension.plugin = plugin;
        bloodLossManager = BloodLossManager.getInstance();
        infectionManager = InfectionManager.getInstance();
        utility = new CSUtility();
        if (plugin.hasPlugin("CrackShot")){
            Bukkit.getPluginManager().registerEvents(this, plugin);
        }
    }
    @EventHandler
    public void onInfection(WeaponDamageEntityEvent event){
        Entity damager = event.getDamager();
        Entity victim = event.getVictim();
        if (victim instanceof Player){
            float percentChance;
            Player player = (Player) victim;
            ItemStack armor;
            if (damager instanceof Projectile){
                percentChance = 5;
                armor = player.getEquipment().getChestplate();
                if (armor != null) percentChance = Utils.chanceOfArmor(percentChance, armor.getType());
                if (Utils.chance(percentChance) && !infectionManager.isInjured(player))
                    infectionManager.affectPlayer(player);
            }
            else if (damager == null){
                percentChance = 10;
                armor = player.getEquipment().getChestplate();
                if (armor != null) percentChance = Utils.chanceOfArmor(percentChance, armor.getType());
                if (Utils.chance(percentChance) && !infectionManager.isInjured(player))
                    infectionManager.affectPlayer(player);
            }
        }
    }
    @EventHandler
    public void onBloodLoss(WeaponDamageEntityEvent event){
        Entity damager = event.getDamager();
        Entity victim = event.getVictim();
        if (victim instanceof Player){
            float percentChance;
            Player player = (Player) victim;
            ItemStack armor;
            if (damager instanceof Projectile){
                percentChance = 10;
                armor = player.getEquipment().getChestplate();
                if (armor != null) percentChance = Utils.chanceOfArmor(percentChance, armor.getType());
                if (Utils.chance(percentChance) && !bloodLossManager.isInjured(player))
                    bloodLossManager.affectPlayer(player);
            }
            else if (damager == null){
                percentChance = 10;
                armor = player.getEquipment().getChestplate();
                if (armor != null) percentChance = Utils.chanceOfArmor(percentChance, armor.getType());
                if (Utils.chance(percentChance) && !bloodLossManager.isInjured(player))
                    bloodLossManager.affectPlayer(player);
            }
        }
    }
//    public double randomV(){
//        return Math.random() * 2 - 1;
//    }
    @EventHandler
    public void ricochet(ProjectileHitEvent event){
        Location hitLoc = event.getHitBlock().getLocation();
        EntityType type = event.getEntityType();
        Projectile entity = event.getEntity();
        ProjectileSource projSrc = entity.getShooter();
        Player player;
        Vector vector = entity.getVelocity();
        double vecY = vector.getY();
        if (projSrc instanceof Player) {
            player = (Player) projSrc;
            player.sendMessage("Vel: " + entity.getVelocity());
        }
        if (Math.abs(vecY) < 0.2) return;
        entity.remove();
        int vecX = Utils.randRange(2, 3);
        int vecZ = Utils.randRange(2, 3);
        if (Utils.randBool()) vecX *= -1;
        if (Utils.randBool()) vecZ *= -1;
//        vecX -= vecX*(35/100.);
//        vecZ -= vecZ*(35/100.);
        vecY *= -65./100.;
        Entity newEnt = hitLoc.getWorld().spawnEntity(hitLoc, type);
        newEnt.setVelocity(new Vector(vecX, vecY, vecZ));
    }
}
