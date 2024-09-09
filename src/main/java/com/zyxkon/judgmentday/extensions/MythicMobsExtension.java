package com.zyxkon.judgmentday.extensions;
import com.zyxkon.judgmentday.Main;
import com.zyxkon.judgmentday.Utils;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MobManager;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import io.lumine.xikage.mythicmobs.util.MythicUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntitySpawnEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MythicMobsExtension {
    Main plugin;

    static MythicMobs mythicPlugin;
    static MobManager mobManager;
    static List<String> mobsName;

    public MythicMobsExtension() {
        this.plugin = Main.getInstance();
        if (Main.hasPlugin(Extension.MYTHICMOBS)){
            mythicPlugin = MythicMobs.inst();
            Main.consoleSend(ChatColor.YELLOW+ "MythicMobs.inst() is null: %b", mythicPlugin == null);
            Bukkit.getPluginManager().registerEvents(new MythicMobsListener(), Main.getInstance());
            try {
                mobManager = mythicPlugin.getMobManager();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            mobsName = new ArrayList<>(mobManager.getMobNames());
        }
    }
    public static void spawnMob(String name, Location location){
//        MythicMob mMob = mobManager.getMythicMob(name);
//        assert mMob != null;
        ActiveMob aMob = mobManager.spawnMob(name, location);
        Main.testBroadcast("Spawned mythic mob %s at (%d, %d, %d)", name,
                (int) location.getX(), (int) location.getY(), (int) location.getZ());
    }
}
class MythicMobsListener implements Listener {

    @EventHandler
    public void onZombieSpawn(CreatureSpawnEvent event){
        Entity e = event.getEntity();
        CreatureSpawnEvent.SpawnReason r = event.getSpawnReason();
        if (!(e instanceof Zombie) || !(r == CreatureSpawnEvent.SpawnReason.CUSTOM)){
            return;
        }
        Location l = event.getLocation();

        MythicMobsExtension.spawnMob(
                MythicMobsExtension.mobsName.get((int) Utils.randRange(
                        MythicMobsExtension.mobsName.size()
                )),l
        );
    }
}
