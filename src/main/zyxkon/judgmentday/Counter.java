package main.zyxkon.judgmentday;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@SuppressWarnings("unused")
public class Counter implements Listener {
    public static HashMap<UUID, Integer> deaths = new HashMap<>();
    public static HashMap<UUID, Integer> mobKills = new HashMap<>();
    public static HashMap<UUID, Integer> playerKills = new HashMap<>();

    Main plugin;
    public Counter(Main plugin){
        this.plugin = plugin;
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
        initializeData();
        loadData();
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        deaths.putIfAbsent(uuid, 0);
        mobKills.putIfAbsent(uuid, 0);
        playerKills.putIfAbsent(uuid, 0);
    }
    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        Player player = event.getEntity();
        UUID uuid = player.getUniqueId();
        deaths.merge(uuid, 1, Integer::sum);
        Player killer = event.getEntity().getKiller();
        if (killer != null){
            uuid = killer.getUniqueId();
            playerKills.merge(uuid, 1, Integer::sum);
        }
    }
    @EventHandler
    public void onKill(EntityDeathEvent event){
        Entity entity = event.getEntity();
        if (entity instanceof Zombie){
            Player player = ((Zombie) entity).getKiller();
            if (player != null){
                UUID uuid = player.getUniqueId();
                mobKills.merge(uuid, 1, Integer::sum);
            }
        }
    }
    @SuppressWarnings("unchecked")
    public static void loadData() {
        String[] files = {"DeathCount.dat", "MobKillCount.dat", "PlayerKillCount.dat"};
        for (String file : files){
            String filePath = String.format("plugins%sJudgmentDay%<s".concat(file), File.separator);
            try {
                BukkitObjectInputStream input = new BukkitObjectInputStream(new GZIPInputStream(new FileInputStream(filePath)));
                Object readObj = input.readObject();
                input.close();
                if (!(readObj instanceof HashMap)) {
                    throw new IOException(file+" does not contain HashMap!");
                }
                switch (file){
                    case "DeathCount.dat":
                        deaths = (HashMap<UUID, Integer>) readObj;
                        break;
                    case "MobKillCount.dat":
                        mobKills = (HashMap<UUID, Integer>) readObj;
                        break;
                    case "PlayerKillCount.dat":
                        playerKills = (HashMap<UUID, Integer>) readObj;
                        break;
                }
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void initializeData() {
        String[] files = {"DeathCount.dat", "MobKillCount.dat", "PlayerKillCount.dat"};
        for (String file : files) {
            String filePath = String.format("plugins%sJudgmentDay%<s".concat(file), File.separator);
            File f = new File(filePath);
            if (!f.exists()){
                try {
                    BukkitObjectOutputStream output = new BukkitObjectOutputStream(new GZIPOutputStream(new FileOutputStream(filePath)));
                    switch (file) {
                        case "DeathCount.dat":
                            output.writeObject(deaths);
                            break;
                        case "MobKillCount.dat":
                            output.writeObject(mobKills);
                            break;
                        case "PlayerKillCount.dat":
                            output.writeObject(playerKills);
                            break;
                    }
                    output.flush();
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static void saveData() {
        String[] files = {"DeathCount.dat", "MobKillCount.dat", "PlayerKillCount.dat"};
        for (String file : files) {
            String filePath = String.format("plugins%sJudgmentDay%<s".concat(file), File.separator);
            try {
                BukkitObjectOutputStream output = new BukkitObjectOutputStream(new GZIPOutputStream(new FileOutputStream(filePath)));
                switch (file) {
                    case "DeathCount.dat":
                        output.writeObject(deaths);
                        break;
                    case "MobKillCount.dat":
                        output.writeObject(mobKills);
                        break;
                    case "PlayerKillCount.dat":
                        output.writeObject(playerKills);
                        break;
                }
                output.flush();
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
