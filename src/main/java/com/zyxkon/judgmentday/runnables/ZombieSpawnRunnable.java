package com.zyxkon.judgmentday.runnables;

import java.util.stream.Collectors;

import com.zyxkon.judgmentday.Main;
import com.zyxkon.judgmentday.Utils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Random;

public class ZombieSpawnRunnable extends BukkitRunnable {

    final Main plugin;
    public ZombieSpawnRunnable(Main plugin){
        this.plugin = plugin;
        this.runTaskTimer(plugin, 0L, 20L);
    }
    @Override
    public void run(){
        for (Player p: plugin.getServer().getOnlinePlayers()) {
//            List<Entity> ents = p.getNearbyEntities(30, 10, 30);
//            int limit = 40;
//            if (ents.stream().anyMatch(e -> e instanceof Player)) limit *=
//                    (ents.stream().filter(e -> e instanceof Player)).toArray().length;
//            ents = ents.stream().filter((e) -> e.getType() == EntityType.ZOMBIE).collect(Collectors.toList());
//            if (ents.size() > limit) return;
            World w = p.getWorld();
            Random random = new Random();
            for (int i = 0; i<Utils.randRange(0, 10); i++){
                int x = Utils.randRange(15, 40);
                int y = Utils.randRange(-3, 3);
                int z = Utils.randRange(15, 40);
                if (random.nextBoolean()) x = -x;
                if (random.nextBoolean()) z = -z;
                Location loc = p.getLocation().add(x, y, z);
                int x_offset = Utils.randRange(-4, 4);
                int z_offset = Utils.randRange(-4, 4);
                loc.add(x_offset, 0, z_offset);
                EntityType ent = EntityType.ZOMBIE;
                if (Utils.chance(1)) ent = EntityType.HUSK;
                else if (Utils.chance(5)) ent = EntityType.ZOMBIE_VILLAGER;
                w.spawnEntity(loc, ent);
            }
        }
    }
}
