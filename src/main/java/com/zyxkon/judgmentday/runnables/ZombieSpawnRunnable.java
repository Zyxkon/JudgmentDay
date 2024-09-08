package com.zyxkon.judgmentday.runnables;

import java.util.stream.Collectors;

import com.zyxkon.judgmentday.Main;
import com.zyxkon.judgmentday.Utils;
import com.zyxkon.judgmentday.Runner;
import net.minecraft.server.v1_12_R1.WorldServer;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class ZombieSpawnRunnable extends BukkitRunnable {

    final Main plugin;
    public ZombieSpawnRunnable(Main plugin){
        this.plugin = plugin;
        this.runTaskTimer(plugin, 0L, 100L);
        // this is based on the minecraft time unit: tick, 20 tick equates to 1 second
    }
    @Override
    public void run(){
        for (Player p: plugin.getServer().getOnlinePlayers()) {
            int x_range = 40;
            int y_range = 15;
            int z_range = 40;
            List<Entity> ents = p.getNearbyEntities(x_range, y_range, z_range);
            int limit = 50;
            int horizontalDistance = 10;
            int verticalDistance = 0;
//            if (ents.stream().anyMatch(e -> e instanceof Player)) {
//                limit *= (ents.stream().filter(e -> e instanceof Player)).toArray().length;
//            }
            // makes zombies spawn according to the number of players within each others radius
            ents = ents.stream().filter((e) -> e.getType() == EntityType.ZOMBIE).collect(Collectors.toList());
            if (ents.size() > limit) return;
            World w = p.getWorld();
            for (int i = 0; i< 10; i++){
                Location loca = Utils.getRandomLocationWithinRange(
                        p.getLocation(), x_range, y_range, z_range,
                        horizontalDistance, verticalDistance);
                if (!Utils.isValidSpawnPosition(loca)) continue;
                EntityType ent = EntityType.ZOMBIE;
                if (Utils.chance(10)) ent = EntityType.HUSK;
                else if (Utils.chance(20)) ent = EntityType.ZOMBIE_VILLAGER;
                w.spawnEntity(loca, ent);
//                WorldServer nmsWorld = ((CraftWorld) w).getHandle();
//                Runner r = new Runner(w);
//                r.setPosition(loca.getX(), loca.getY(), loca.getZ());
//                nmsWorld.addEntity(r);
//                w.spawnParticle(Particle.BARRIER, loca, 100);
//                for (Player pl : w.getPlayers()) {
//                    pl.sendMessage(
//                            String.format("Spawned a runner at %s, %s, %s",
//                                    loc.getX(), loc.getY(), loc.getZ())
//                    );
//                }
            }
        }
    }
}
