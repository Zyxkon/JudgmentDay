package main.zyxkon.judgmentday.extensions;

import com.shampaggon.crackshot.events.WeaponDamageEntityEvent;
import main.zyxkon.judgmentday.Main;
import main.zyxkon.judgmentday.Utils;
import main.zyxkon.judgmentday.injuries.bloodloss.BloodLossManager;

import main.zyxkon.judgmentday.injuries.infection.InfectionManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("unused")
public class CrackShotExtension implements Listener {
    Main plugin;
    public CrackShotExtension(Main plugin){
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
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
                if (Utils.chance(percentChance) && !InfectionManager.affectedPlayers.containsKey(player.getUniqueId()))
                    InfectionManager.affectPlayer(player);
            }
            else if (damager == null){
                percentChance = 10;
                armor = player.getEquipment().getChestplate();
                if (armor != null) percentChance = Utils.chanceOfArmor(percentChance, armor.getType());
                if (Utils.chance(percentChance) && !InfectionManager.affectedPlayers.containsKey(player.getUniqueId()))
                    InfectionManager.affectPlayer(player);
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
                if (Utils.chance(percentChance) && !BloodLossManager.affectedPlayers.containsKey(player.getUniqueId()))
                    BloodLossManager.affectPlayer(player);
            }
            else if (damager == null){
                percentChance = 10;
                armor = player.getEquipment().getChestplate();
                if (armor != null) percentChance = Utils.chanceOfArmor(percentChance, armor.getType());
                if (Utils.chance(percentChance) && !BloodLossManager.affectedPlayers.containsKey(player.getUniqueId()))
                    BloodLossManager.affectPlayer(player);
            }
        }
    }
}
