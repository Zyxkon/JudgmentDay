package com.zyxkon.judgmentday.extensions;

import com.shampaggon.crackshot.events.WeaponDamageEntityEvent;
import com.zyxkon.judgmentday.Main;
import com.zyxkon.judgmentday.Utils;
import com.zyxkon.judgmentday.injuries.bloodloss.BloodLossManager;
import com.zyxkon.judgmentday.injuries.infection.InfectionManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class CrackShotExtension implements Listener {
    static Main plugin;
    private static BloodLossManager bloodLossManager;
    private static InfectionManager infectionManager;

    public CrackShotExtension(Main plugin){
        CrackShotExtension.plugin = plugin;
        bloodLossManager = BloodLossManager.getInstance();
        infectionManager = InfectionManager.getInstance();
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
    @EventHandler
    public void ricochet(ProjectileHitEvent event){
        EntityType[] ents = {EntityType.EGG, EntityType.ENDER_PEARL, EntityType.LINGERING_POTION, EntityType.SPLASH_POTION};
        Projectile entity = event.getEntity();
        Location hitLoc = entity.getLocation();
        EntityType type = entity.getType();
        if (Arrays.asList(ents).contains(type)) return;
        Vector velocity = entity.getVelocity();
        ArrayList<Double> points = new ArrayList<>(Arrays.asList(velocity.getX(), velocity.getY(), velocity.getZ()));
        double vecX = velocity.getX();
        double vecY = velocity.getY();
        double vecZ = velocity.getZ();
        ArrayList<Double> posPoints = new ArrayList<>();
        points.forEach(d -> posPoints.add(Math.abs(d)));
        double maxVec = points.get(posPoints.indexOf(Collections.max(posPoints)));
        if (Math.abs(maxVec) > 5) return;
        if (Math.abs(maxVec) < 1.5) return;
        double randX = Utils.randRange(Math.abs(vecX));
        double randY = Utils.randRange(Math.abs(vecY));
        double randZ = Utils.randRange(Math.abs(vecZ));
        vecX = randX;
        vecZ = randZ;
        vecY = (vecY < 0) ? randY : -randY;
        if (Utils.randBool()) vecX *= -1;
        if (Utils.randBool()) vecZ *= -1;
        vecX *= 0.65;
        vecY *= 0.65;
        vecZ *= 0.65;
        entity.remove();
        Entity newEnt = hitLoc.getWorld().spawnEntity(hitLoc, type);
        if (newEnt instanceof Arrow){
            ((Arrow) newEnt).setBounce(true);
            ((Arrow) newEnt).setPickupStatus(Arrow.PickupStatus.ALLOWED);
        }
        newEnt.setVelocity(new Vector(vecX, vecY, vecZ));
    }
}
