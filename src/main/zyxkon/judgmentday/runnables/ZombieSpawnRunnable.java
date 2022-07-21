package main.zyxkon.judgmentday.runnables;

import java.util.stream.Collectors;

import main.zyxkon.judgmentday.Main;
import static main.zyxkon.judgmentday.Utils.randRange;
import static main.zyxkon.judgmentday.Utils.chance;
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
        this.runTaskTimer(plugin, 0L, 10L);
    }
    @Override
    public void run(){
        for (Player p: plugin.getServer().getOnlinePlayers()) {
            List<Entity> ents = p.getNearbyEntities(40, 5, 40);
            int limit = 5;
            if (ents.stream().anyMatch(e -> e instanceof Player)) limit *=
                    (ents.stream().filter(e -> e instanceof Player)).toArray().length;
            ents = ents.stream().filter((e) -> e.getType() == EntityType.ZOMBIE).collect(Collectors.toList());
            Random random = new Random();
            if (ents.size() <= limit){
                World w = p.getWorld();
                int x = randRange(15, 50);
                int y = randRange(-3, 3);
                int z = randRange(15, 50);
                if (random.nextBoolean()) x = -x;
                if (random.nextBoolean()) z = -z;
                Location loc = p.getLocation().add(x, y, z);
                int x_offset = randRange(-4, 4);
                int z_offset = randRange(-4, 4);
                loc.add(x_offset, 0, z_offset);
                EntityType ent = EntityType.ZOMBIE;
                if (chance(10)) ent = EntityType.HUSK;
                else if (chance(35)) ent = EntityType.ZOMBIE_VILLAGER;
                w.spawnEntity(loc, ent);
                loc.add(-x_offset, 0, -z_offset);
            }
        }
    }
}
