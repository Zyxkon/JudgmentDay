package com.zyxkon.judgmentday.injuries.infection;

import com.zyxkon.judgmentday.Main;
import com.zyxkon.judgmentday.Utils;
import com.zyxkon.judgmentday.injuries.Injury;
import com.zyxkon.judgmentday.injuries.impairment.ImpairmentManager;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public class Infection extends Injury {
    Main plugin;
    Player player;
    int process;
    float normalSpeed;
    public Infection(Main plugin, Player player){
        this.plugin = plugin;
        this.player = player;
        this.normalSpeed = (!ImpairmentManager.getInstance().isInjured(player) ? player.getWalkSpeed() : 0.2f);
        process = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable(){
            int timer = 0;
            @Override
            public void run() {
                if (!player.isDead()){
                    if (Utils.isInRange(timer, 0, 5)) Utils.sendActionBarMessage(player, Utils.translate(
                            "You are infected, take an antibiotic or use a medkit immediately. Your infection is &nstage 1."));
                    if (timer < 170) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 5 * 20, 0));
                    }
                    if (player.getWalkSpeed() >= normalSpeed) player.setWalkSpeed((float) (normalSpeed * (95.0 / 100.0)));
                    if (timer >= 40) {
                        if (Utils.isInRange(timer, 40, 45)) Utils.sendActionBarMessage(player, Utils.translate(
                                "You feel exhausted. Your infection is &e&nstage 2."));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 5 * 20, 0));
                    }
                    if (timer >= 70) {
                        if (Utils.isInRange(timer, 70, 75)) Utils.sendActionBarMessage(player, Utils.translate(
                                "You start to feel dizzy. Your infection is &6&nstage 3."));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 5 * 20, 0));
                        if (timer % 3 == 0) player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 8 * 20, 0));
                        if (timer % 2 == 0) player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 4 * 20, 0));
                    }
                    if (timer >= 120) {
                        if (Utils.isInRange(timer, 120, 125)) Utils.sendActionBarMessage(player, Utils.translate(
                                "You are passing out. Your infection is &c&n&lstage 4."));
                        if (timer % 4 == 0) player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 3 * 20, 1));
                    }
                    if (timer > 170) {
                        if (Utils.isInRange(timer, 170, 175)) Utils.sendActionBarMessage(player, Utils.translate(
                                "The infection is destroying your body at a rapid pace. Your infection is &4&l&nSTAGE 5"));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 4 * 20, 1));
                    }
                    timer++;
                }
            }
        }, 0L, 20L);
    }
    @Override
    public void cancel(){
        plugin.getServer().getScheduler().cancelTask(process);
        player.setWalkSpeed(normalSpeed);
    }
}
