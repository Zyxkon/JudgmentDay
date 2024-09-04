package com.zyxkon.judgmentday.runnables;

import java.util.stream.Collectors;

import com.zyxkon.judgmentday.Main;
import com.zyxkon.judgmentday.Utils;
import com.zyxkon.judgmentday.mobs.zombies.Runner;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Random;

public class ZombieSpawnRunnable extends BukkitRunnable {

    final Main plugin;
    public ZombieSpawnRunnable(Main plugin){
        this.plugin = plugin;
        this.runTaskTimer(plugin, 0L, 5L);
        // this is based on the minecraft time unit: tick, 20 tick equates to 1 second
    }
    @Override
    public void run(){
        for (Player p: plugin.getServer().getOnlinePlayers()) {
            int x_range = 40;
            int y_range = 15;
            int z_range = 40;
            List<Entity> ents = p.getNearbyEntities(x_range, y_range, z_range);
            int limit = 100;
//            if (ents.stream().anyMatch(e -> e instanceof Player)) {
//                limit *= (ents.stream().filter(e -> e instanceof Player)).toArray().length;
//            }
            // makes zombies spawn according to the number of players within each others radius
            ents = ents.stream().filter((e) -> e.getType() == EntityType.ZOMBIE).collect(Collectors.toList());
            if (ents.size() > limit) return;
            World w = p.getWorld();
            Random random = new Random();
            for (int i = 0; i<10; i++){
                int x = Utils.randRange(x_range /2,x_range*3/2);
                int y = Utils.randRange(-y_range/2, y_range/2);
                int z = Utils.randRange(z_range /2,z_range*3/2);
                if (random.nextBoolean()) x = -x;
                if (random.nextBoolean()) z = -z;
                Location loc = p.getLocation().add(x, y, z);
                int x_offset = Utils.randRange(-4, 4);
                int z_offset = Utils.randRange(-4, 4);
                loc.add(x_offset, 0, z_offset);
                EntityType ent = EntityType.ZOMBIE;
                if (Utils.chance(10)) ent = EntityType.HUSK;
                else if (Utils.chance(20)) ent = EntityType.ZOMBIE_VILLAGER;
                Block bl = loc.getBlock();
                if (Utils.isSolid(bl.getRelative(BlockFace.UP))
                        || Utils.isSolid(bl)
                        || !Utils.isSolid(bl.getRelative(BlockFace.DOWN))) {
                    continue;
                }
                w.spawnEntity(loc, ent);
                Runner r = new Runner(w);
                r.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
                ((CraftWorld) w).getHandle().addEntity(
                        r
                );
            }
        }
    }
}
