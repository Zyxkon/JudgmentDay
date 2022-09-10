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
    public void onWeaponHitBlock(ProjectileHitEvent event){
//        Entity entity = event.getEntity();
//        if (!(entity instanceof Player)) return;
//        Location hitLoc = event.getHitBlock().getLocation();
//        Projectile projectile = event.getEntity();
//        EntityType type = projectile.getType();
//        Entity newProj = hitLoc.getWorld().spawnEntity(hitLoc, type);
//        player.sendMessage(projectile + " has hit!");
//        newProj.setVelocity(new Vector(Math.random()*2-1, 0.3, Math.random()*2-1))
        Location hitLoc = event.getEntity().getLocation();
        if (!(event.getEntity() instanceof Arrow)) return;
        Arrow arr = (Arrow) event.getEntity();
        ProjectileSource projSrc = arr.getShooter();
        Player player;
        double velY = arr.getVelocity().getY();
        if (projSrc instanceof Player) {
            player = (Player) projSrc;
            player.sendMessage("Y: "+ velY);
        }
        if (velY >= -0.4) return;
        arr.remove();
        Arrow arrow = (Arrow) hitLoc.getWorld().spawnEntity(hitLoc, EntityType.ARROW);
        int vecX = Utils.randRange(3, 6);
        int vecZ = Utils.randRange(3, 6);
        if (Utils.randBool()) vecX *= -1;
        if (Utils.randBool()) vecZ *= -1;
        arrow.setVelocity(new Vector(vecX, velY+0.2, vecZ));
    }
}
