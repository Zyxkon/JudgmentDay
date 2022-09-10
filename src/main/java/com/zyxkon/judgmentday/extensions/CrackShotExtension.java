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
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
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
    @EventHandler
    public void onWeaponHitBlock(WeaponHitBlockEvent event){
        Player player = event.getPlayer();
//        Location hitLoc = event.getBlock().getLocation();
        Entity projectile = event.getProjectile();
//        EntityType type = projectile.getType();
        projectile.setVelocity(player.getLocation().toVector());
    }
}
