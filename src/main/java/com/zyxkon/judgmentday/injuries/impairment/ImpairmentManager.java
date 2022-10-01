package com.zyxkon.judgmentday.injuries.impairment;

import com.zyxkon.judgmentday.Main;
import com.zyxkon.judgmentday.injuries.InjuryManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.UUID;


public class ImpairmentManager extends InjuryManager<Impairment> {
    private static final HashMap<UUID, Impairment> affectedPlayers = new HashMap<>();
    static Main plugin;
    public static ImpairmentManager instance;
    public ImpairmentManager(final Main plugin){
        instance = this;
        ImpairmentManager.plugin = plugin;
        new ImpairmentListener(plugin);
    }
    public static ImpairmentManager getInstance(){
        return instance;
    }
    @Override
    public HashMap<UUID, Impairment> getHashmap() {
        return affectedPlayers;
    }
    @Override
    public void put(UUID uuid, Impairment process){
        affectedPlayers.put(uuid, process);
    }
    @Override
    public Impairment getInjury(UUID uuid){
        return getHashmap().get(uuid);
    }
    @Override
    public boolean isInjured(UUID uuid){
        return getHashmap().containsKey(uuid);
    }
    @Override
    public void affectPlayer(Player player){
        UUID uuid = player.getUniqueId();
        put(uuid, new Impairment(plugin, player));
    }
    @Override
    public boolean healPlayer(Player player){
        UUID uuid = player.getUniqueId();
        if (isInjured(uuid)) {
            getInjury(uuid).cancel();
            affectedPlayers.remove(uuid);
            return true;
        }
        return false;
    }
    @Override
    public void shutDown(){
        for (UUID uuid : getHashmap().keySet()){
            Player player = Bukkit.getPlayer(uuid);
            player.setWalkSpeed(getInjury(uuid).normalSpeed);
            getInjury(uuid).cancel();
        }
    }
}

