package main.zyxkon.judgmentday.thirst;

import main.zyxkon.judgmentday.Main;
import main.zyxkon.judgmentday.Utils;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Dehydration {
    Main plugin;
    Player player;
    int process;
    public Dehydration(Main plugin, Player player){
        this.plugin = plugin;
        this.player = player;
        process = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            boolean bool = true;
            boolean bool1 = true;
            int i = 0;
            @Override
            public void run() {
                if (bool){
                    Utils.sendActionBarMessage(player, "The lack of water makes you feel tired. Drink water immediately.");
                    bool = false;
                }
                int thirst = ThirstManager.thirstPlayers.get(player.getUniqueId());
                if (thirst <= 10){
                    if (thirst == 10) Utils.sendActionBarMessage(player, "Your vision becomes blurry and everything around you seems distorted. FIND WATER NOW.");
                    player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 8*20, 0));
                }
                if (thirst == 0){
                    if (bool1){
                        Utils.sendActionBarMessage(player, "Your body cannot handle the dehydration and you are dying slowly.");
                        bool1 = false;
                    }
                    if (i % 2 == 0) player.damage(4d);
                }
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 5*20, 0));
                i++;
            }
        }, 0L, 20L);
    }
    public void cancel(){
        plugin.getServer().getScheduler().cancelTask(process);
    }
}
