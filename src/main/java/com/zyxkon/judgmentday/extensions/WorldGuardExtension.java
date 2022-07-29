package com.zyxkon.judgmentday.extensions;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.zyxkon.judgmentday.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public final class WorldGuardExtension {
    private static final Main plugin = Main.getInstance();
    private static WorldGuardPlugin getWorldGuard(){
        Plugin pl = Bukkit.getPluginManager().getPlugin("WorldGuard");
        if (!(pl instanceof WorldGuardPlugin)) return null;
        return (WorldGuardPlugin) pl;
    }
    public static boolean regionExists(String regionId){
        WorldGuardPlugin worldGuard = getWorldGuard();
        if (worldGuard == null) return false;
        Set<String> regions = new HashSet<>();
        for (World world : Main.getInstance().getServer().getWorlds()){
            RegionManager manager = worldGuard.getRegionManager(world);
            regions.addAll(manager.getRegions().keySet());
        }
        return regions.contains(regionId);
    }
    public static ArrayList<String> getRegions(Player player){
        return getRegions(player.getLocation());
    }
    public static ArrayList<String> getRegions(Location location){
        WorldGuardPlugin worldGuard = getWorldGuard();
        ArrayList<String> array = new ArrayList<>();
        if (worldGuard == null) return array;
        RegionManager manager = worldGuard.getRegionManager(location.getWorld());
        ApplicableRegionSet set = manager.getApplicableRegions(location);
        set.forEach(r -> array.add(r.getId()));
        return array;
    }
    public static ArrayList<String> getBarracks(){
        return new ArrayList<>(plugin.getRegionsConfig().getStringList("barracks"));
    }
    public static ArrayList<String> getSafezones(){
        return new ArrayList<>(plugin.getRegionsConfig().getStringList("safezones"));
    }
    public static boolean isBarracks(String regionId){
        return (regionExists(regionId) && getBarracks().contains(regionId));
    }
    public static boolean isSafezone(String regionId){
        return (regionExists(regionId) && getSafezones().contains(regionId));
    }
}
