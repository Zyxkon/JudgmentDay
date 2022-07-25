package com.zyxkon.judgmentday.runnables;

import com.zyxkon.judgmentday.Main;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class BarbedWireRunnable extends BukkitRunnable {
    Main plugin;
    public BarbedWireRunnable(Main plugin){
        this.plugin = plugin;
        this.runTaskTimer(plugin, 0, 15L);
    }
    @Override
    public void run() {
        for (Player player: plugin.getServer().getOnlinePlayers()){
            if (player.getLocation().getBlock().getType() == Material.WEB
                    || player.getLocation().getBlock().getRelative(BlockFace.UP).getType() == Material.WEB){
                player.damage(2d);
            }
        }
    }
}
