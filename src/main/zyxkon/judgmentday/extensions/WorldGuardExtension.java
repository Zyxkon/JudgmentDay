package main.zyxkon.judgmentday.extensions;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;

public class WorldGuardExtension {
    private static WorldGuardPlugin getWorldGuard(){
        Plugin pl = Bukkit.getPluginManager().getPlugin("WorldGuard");
        if (!(pl instanceof WorldGuardPlugin)) return null;
        return (WorldGuardPlugin) pl;
    }

    public static ArrayList<String> getRegion(Player player){
        ArrayList<String> array = new ArrayList<>();
        WorldGuardPlugin worldGuard = getWorldGuard();
        if (worldGuard == null) return array;
        RegionManager manager = worldGuard.getRegionManager(player.getWorld());
        ApplicableRegionSet set = manager.getApplicableRegions(player.getLocation());
        for (ProtectedRegion region : set){
            String name = region.getId();
            name = name.substring(0, 1).toUpperCase() + name.substring(1);
            array.add(name);
        }
        return array;
    }
}
