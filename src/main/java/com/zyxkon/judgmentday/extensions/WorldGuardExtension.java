package com.zyxkon.judgmentday.extensions;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.zyxkon.judgmentday.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class WorldGuardExtension {
    private static File regionsFile;
    static Main plugin;
    private static FileConfiguration regionsConfig;
    private static final WorldGuardPlugin worldGuard;
    static WorldGuard wgInstance;
    static RegionContainer container;
    public WorldGuardExtension(){
        plugin = Main.getInstance();
    }
    static {
        wgInstance = WorldGuard.getInstance();
        container = wgInstance.getPlatform().getRegionContainer();
        Plugin pl = Bukkit.getPluginManager().getPlugin(Extension.WORLDGUARD.pluginName);
        if (pl instanceof WorldGuardPlugin) {
            worldGuard = (WorldGuardPlugin) pl;
            regionsFile = new File(plugin.getDataFolder(), "regions.yml");
            if (!regionsFile.exists()) {
                plugin.saveResource("regions.yml", false);
            }
            regionsConfig = YamlConfiguration.loadConfiguration(regionsFile);
        }
        else {
            worldGuard = null;
        }
    }
    public static boolean regionExists(String regionId){
        if (worldGuard == null) return false;
        Set<String> regions = new HashSet<>();
        for (World world : plugin.getServer().getWorlds()){
            RegionManager manager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(
                    (com.sk89q.worldedit.world.World) world
            );
            if (manager == null) {
                return false;
            }
            regions.addAll(manager.getRegions().keySet());
        }
        return regions.contains(regionId);
    }
    public static ArrayList<String> getRegions(Player player){
        return getRegions(player.getLocation());
    }
    public static ArrayList<String> getRegions(Location location){
        com.sk89q.worldedit.world.World world = BukkitAdapter.adapt(location.getWorld());
        ArrayList<String> array = new ArrayList<>();
        if (worldGuard == null) return array;

        RegionContainer container = wgInstance.getPlatform().getRegionContainer();
        RegionManager regions = container.get(world);
        assert regions != null;
        ApplicableRegionSet set = regions.getApplicableRegions(BukkitAdapter.adapt(location).toVector().toBlockPoint());
        set.forEach(r -> array.add(r.getId()));
        return array;
    }
    public static ArrayList<String> getBarracks(){
        return new ArrayList<>(getRegionsConfig().getStringList("barracks"));
    }
    public static ArrayList<String> getSafezones(){
        return new ArrayList<>(getRegionsConfig().getStringList("safezones"));
    }
    public static boolean isBarracks(String regionId){
        return (regionExists(regionId) && getBarracks().contains(regionId));
    }
    public static boolean isSafezone(String regionId){
        return (regionExists(regionId) && getSafezones().contains(regionId));
    }
    public static void reload(){
        regionsFile = new File(plugin.getDataFolder(), "regions.yml");
        regionsConfig = YamlConfiguration.loadConfiguration(regionsFile);
    }
    public static File getRegionsFile(){
        return regionsFile;
    }
    public static FileConfiguration getRegionsConfig(){
        return regionsConfig;
    }
}
