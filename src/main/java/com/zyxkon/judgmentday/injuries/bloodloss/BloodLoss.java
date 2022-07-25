package com.zyxkon.judgmentday.injuries.bloodloss;

import com.zyxkon.judgmentday.Main;
import com.zyxkon.judgmentday.Utils;
import org.bukkit.entity.Player;

public class BloodLoss {
    static Main plugin;
    Player player;
    int process;
    public BloodLoss(Main plugin, Player player){
        BloodLoss.plugin = plugin;
        this.player = player;
        process = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            int timer = 0;
            @Override
            public void run() {
                if (!player.isDead()){
                    if (timer <= 60) {
                        if (Utils.isInRange(timer, 0, 5)) Utils.sendActionBarMessage(player,
                                "You are bleeding! Find a bandage and close the wound.");
                        if (timer % 10 == 0 && player.getHealth() > 3d) player.damage(3d);
                    } else if (Utils.isInRange(timer, 60, 60 + 100)) {
                        if (Utils.isInRange(timer, 60, 65)) Utils.sendActionBarMessage(player,
                                "The bleeding got worse and you are now bleeding profusely.");
                        if (timer % 20 == 0 && player.getHealth() > 4d) player.damage(4d);
                    } else if (timer >= 160) {
                        if (Utils.isInRange(timer, 160, 165)) Utils.sendActionBarMessage(player,
                                "You are losing a very large amount of blood. Heal yourself now.");
                        if (timer % 20 == 0) {
                            if (player.getHealth() > 8d) player.damage(8d);
                            else if (player.getHealth() <= 8d) player.damage(player.getHealth()-1);
                        }
                    }
                    timer++;
                }
            }
        }, 0L, 20L);
    }
    public void cancel(){
        plugin.getServer().getScheduler().cancelTask(process);
    }
}
