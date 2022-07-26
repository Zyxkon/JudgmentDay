package com.zyxkon.judgmentday.injuries.impairment;

import com.zyxkon.judgmentday.Main;
import com.zyxkon.judgmentday.Utils;
import com.zyxkon.judgmentday.injuries.infection.InfectionManager;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Impairment {
    static Main plugin;
    Player player;
    int process;
    final float normalSpeed;
    boolean canJump;
    public Impairment(Main plugin, Player player){
        this.canJump = true;
        Impairment.plugin = plugin;
        this.player = player;
        this.normalSpeed = (!InfectionManager.isInjured(player) ? player.getWalkSpeed() : 1);
        process = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            int timer = 0;
            final int stage_1 = 60*2;
            final int stage_2 = 60*2;
            final int sum = stage_1+stage_2;
            final int stage_3 = 60*3;
            @Override
            public void run() {
                if (!player.isDead()){
                    if (Utils.isInRange(timer, 0, 5)) Utils.sendActionBarMessage(player,
                            "Your legs are injured and you are finding it harder to walk by the second. Find a first aid kit.");
                    if (Utils.isInRange(timer, 0, 100))
                        player.setWalkSpeed((float) (normalSpeed * ((70. - timer / 2) / 100.)));
                    if (Utils.isInRange(timer, stage_1, sum)) {
                        if (Utils.isInRange(timer, stage_1, stage_1 + 6)) Utils.sendActionBarMessage(player,
                                "The pain in your legs is making you feel dizzy and you feel like fainting. You find jumping a lot more difficult.");
                        canJump = false;
                        if (timer % 20 == 0) player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 15 * 20, 0));
                        if (timer % 30 == 0) player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 20, 0));
                    }
                    if (timer >= sum) {
                        if (Utils.isInRange(timer, sum, sum + 5)) Utils.sendActionBarMessage(player,
                                "You feel very unwell and your vision starts to become blurry. Your legs feel a lot worse.");
                        player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 10 * 20, 0));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 20, 0));
                        if (Utils.isInRange(timer, sum, sum + 60)) player.setWalkSpeed((float) (normalSpeed * ((20. - (timer - sum) / 3) / 100.)));
                        if (timer == sum + 60) Utils.sendActionBarMessage(player,
                                "The pain is too great for you to walk and you are now paralyzed.");
                    }
                    if (timer >= sum + stage_3) {
                        if (Utils.isInRange(timer, sum + stage_3, sum + stage_3 + 5)) Utils.sendActionBarMessage(player,
                                "The pain becomes too much for your body to handle and you are slowly dying. HEAL YOURSELF NOW!");
                        player.damage(4d);
                    }
                    timer++;
                }
            }
        }, 0L, 20L);
    }
    public void cancel(){
        plugin.getServer().getScheduler().cancelTask(process);
        player.setWalkSpeed(normalSpeed);
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 25*20, 0));
    }
}
