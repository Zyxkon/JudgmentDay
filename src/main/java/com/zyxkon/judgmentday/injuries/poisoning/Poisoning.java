package com.zyxkon.judgmentday.injuries.poisoning;

import com.zyxkon.judgmentday.Main;
import com.zyxkon.judgmentday.Utils;
import com.zyxkon.judgmentday.injuries.Injury;
import com.zyxkon.judgmentday.injuries.impairment.ImpairmentManager;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Poisoning extends Injury {
    static Main plugin;
    Player player;
    int process;
    float normalSpeed;
    Runnable runnable;
    public Poisoning(Main plugin, Player player){
        Poisoning.plugin = plugin;
        this.player = player;
        runnable = getRunnable();
        normalSpeed = 0.2f;
        process = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 0L, 20L);
    }
    @Override
    public void cancel(){
        plugin.getServer().getScheduler().cancelTask(process);
        player.setWalkSpeed(normalSpeed);
    }

    @Override
    public Runnable getRunnable() {
        return new Runnable(){
            int timer = 0;
            @Override
            public void run() {
                if (!player.isDead()){
                    if (Utils.isInRange(timer, 0, 5)) Utils.sendActionBarMessage(player, Utils.translate(
                            "You are poisoned. Your poison progress is at stage 1."));
                    if (timer < 170) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 5 * 20, 0));
                    }
                    player.setWalkSpeed((float) (normalSpeed * (80.0 / 100.0)));
                    if (timer >= 40) {
                        if (Utils.isInRange(timer, 40, 45)) Utils.sendActionBarMessage(player, Utils.translate(
                                "You feel extremely uneasy. Your ailment has reached stage 2."));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 5 * 20, 0));
                    }
                    if (timer >= 70) {
                        if (Utils.isInRange(timer, 70, 75)) Utils.sendActionBarMessage(player, Utils.translate(
                                "You have a high fever. Your malaise has reached &6&nstage 3."));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 5 * 20, 0));
                        if (timer % 3 == 0) player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 8 * 20, 0));
                        if (timer % 2 == 0) player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 4 * 20, 0));
                    }
                    if (timer >= 120) {
                        player.setWalkSpeed((float) (normalSpeed * (125.0 / 100.0)));
                        if (Utils.isInRange(timer, 120, 125)) Utils.sendActionBarMessage(player, Utils.translate(
                                "You are nauseous and pukish. Your infection is at &c&n&lstage 4."));
                        if (timer % 4 == 0) player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 3 * 20, 1));
                    }
                    if (timer > 170) {
                        if (Utils.isInRange(timer, 170, 175)) Utils.sendActionBarMessage(player, Utils.translate(
                                "You feel like cutting your bowels out. Your poisoning is at &4&l&nSTAGE 5"));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 4 * 20, 1));
                    }
                    timer++;
                }
            }
        };
    }
}
