package com.zyxkon.judgmentday.thirst;

import com.zyxkon.judgmentday.Utils;
import com.zyxkon.judgmentday.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@SuppressWarnings("unused")
public class ThirstManager extends BukkitRunnable implements Listener {
    static Main plugin;
    private static HashMap<UUID, Integer> thirstPlayers = new HashMap<>();
    private static final HashMap<UUID, Dehydration> affectedPlayers = new HashMap<>();
    private static final HashMap<UUID, Integer> thirstTimer = new HashMap<>();
    private static String pluginName;
    public ThirstManager(Main plugin){
        ThirstManager.plugin = plugin;
        pluginName = plugin.getName();
        this.runTaskTimer(plugin, 0L, 20L);
        Bukkit.getPluginManager().registerEvents(this, plugin);
        initializeData();
        loadData();
    }
    public static int getThirst(Player player){
        return getThirst(player.getUniqueId());
    }
    public static int getThirst(UUID uuid){
        return thirstPlayers.get(uuid);
    }
    public static void affectPlayer(Player player) {
        UUID uuid = player.getUniqueId();
        affectedPlayers.put(uuid, new Dehydration(plugin, player));
    }
    public static boolean healPlayer(Player player){
        UUID uuid = player.getUniqueId();
        if (affectedPlayers.containsKey(uuid)){
            affectedPlayers.get(uuid).cancel();
            affectedPlayers.remove(uuid);
            return true;
        }
        return false;
    }
    public static void addThirst(Player player, int percent){
        UUID uuid = player.getUniqueId();
        if (thirstPlayers.get(uuid)+percent >= 100){
            thirstPlayers.put(uuid, 100);
            return;
        }
        thirstPlayers.put(uuid, thirstPlayers.get(uuid)+percent);
        int thirst = thirstPlayers.get(uuid);
        if (thirst <= 20){
            if (!affectedPlayers.containsKey(uuid)) affectPlayer(player);
        }
    }
    public static void setThirst(Player player, int percent){
        UUID uuid = player.getUniqueId();
        if (thirstPlayers.containsKey(uuid)) {
            addThirst(player, -thirstPlayers.get(uuid) + percent);
            return;
        }
        thirstPlayers.put(player.getUniqueId(), percent);
    }
    public static ChatColor formatThirst(int thirst){
        int max_thirst = 100;
        ChatColor color = ChatColor.WHITE;
        if (Utils.isInRange(thirst, (int) (max_thirst/6.*5), max_thirst)) color = ChatColor.AQUA;
        else if (Utils.isInRange(thirst, (int) (max_thirst/6.*4), (int) (max_thirst/6.*5))) color = ChatColor.GREEN;
        else if (Utils.isInRange(thirst, (int) (max_thirst/6.*3), (int) (max_thirst/6.*4))) color = ChatColor.YELLOW;
        else if (Utils.isInRange(thirst, (int) (max_thirst/6.*2), (int) (max_thirst/6.*3))) color = ChatColor.GOLD;
        else if (Utils.isInRange(thirst, max_thirst/6, (int) (max_thirst/6.*2))) color = ChatColor.RED;
        else if (Utils.isInRange(thirst, 0, max_thirst/6)) color = ChatColor.DARK_RED;
        return color;
    }
    public static void initializeData() {
        File file = new File(MessageFormat.format("plugins{0}{1}{0}ThirstData.dat", File.separator, pluginName));
        if (!file.exists()){
            try {
                BukkitObjectOutputStream output = new BukkitObjectOutputStream(new GZIPOutputStream(new FileOutputStream(file)));
                output.writeObject(thirstPlayers);
                output.flush();
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @SuppressWarnings("unchecked")
    public static void loadData() {
        File file = new File(MessageFormat.format("plugins{0}{1}{0}ThirstData.dat", File.separator, pluginName));
        try {
            BukkitObjectInputStream input = new BukkitObjectInputStream(new GZIPInputStream(new FileInputStream(file)));
            Object readObj = input.readObject();
            input.close();
            if (!(readObj instanceof HashMap)) {
                throw new IOException("ThirstData.dat does not contain HashMap!");
            }
            thirstPlayers = (HashMap<UUID, Integer>) readObj;

        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }
    public static void saveData() {
        File file = new File(MessageFormat.format("plugins{0}{1}{0}ThirstData.dat", File.separator, pluginName));
        try {
            BukkitObjectOutputStream output = new BukkitObjectOutputStream(new GZIPOutputStream(new FileOutputStream(file)));
            output.writeObject(thirstPlayers);
            output.flush();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run(){
        for (Player player: plugin.getServer().getOnlinePlayers()){
            UUID uuid = player.getUniqueId();
            if (player.getGameMode().equals(GameMode.SURVIVAL)){
                if (thirstPlayers.get(player.getUniqueId()) <= 0) return;
                if (!thirstTimer.containsKey(uuid)) thirstTimer.put(uuid, 0);
                int timer = thirstTimer.get(uuid);
                if (!player.isDead()) {
                    if (thirstTimer.containsKey(uuid)) thirstTimer.put(uuid, thirstTimer.get(uuid)+1);
                    int thirst = thirstPlayers.get(uuid);
                    if (Utils.isInRange(thirst, 90, 100)) {
                        if (timer % 15 == 0) addThirst(player, -1);
                        return;
                    }
                    else if (Utils.isInRange(thirst, 30, 90)){
                        if (timer % 20 == 0) addThirst(player, -1);
                        return;
                    }
                    else if (Utils.isInRange(thirst, 0, 30)) {
                        if (timer % 30 == 0) addThirst(player, -1);
                        return;
                    }
                }
            }
        }
    }
    @EventHandler
    public void onDrink(PlayerItemConsumeEvent event){
        Player player = event.getPlayer();
        if (event.getItem().getType() == Material.POTION){
            PotionType potionType = ((PotionMeta) event.getItem().getItemMeta()).getBasePotionData().getType();
            if (potionType == PotionType.WATER) {
                addThirst(player, 30);
                healPlayer(player);
            }
        }
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (!thirstPlayers.containsKey(uuid)) setThirst(player, 100);
        if (affectedPlayers.containsKey(uuid)) affectPlayer(event.getPlayer());
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        UUID uuid = event.getPlayer().getUniqueId();
        if (affectedPlayers.containsKey(uuid)) {
            affectedPlayers.get(uuid).cancel();
            affectedPlayers.put(uuid, null);
        }
    }
    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        setThirst(event.getEntity(), 100);
        if (healPlayer(event.getEntity())) event.setDeathMessage(String.format("%s died of thirst.", event.getEntity().getName()));
    }
}
