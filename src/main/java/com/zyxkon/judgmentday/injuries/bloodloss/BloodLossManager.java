package com.zyxkon.judgmentday.injuries.bloodloss;

import com.zyxkon.judgmentday.Main;
import com.zyxkon.judgmentday.injuries.InjuryManager;
import org.bukkit.*;
import org.bukkit.entity.*;

import java.util.*;


public class BloodLossManager extends InjuryManager<BloodLoss> {
    private static final HashMap<UUID, BloodLoss> affectedPlayers = new HashMap<>();
    private static Main plugin;
    public static BloodLossManager instance;
    public BloodLossManager(final Main plugin){
        instance = this;
        BloodLossManager.plugin = plugin;
        new BloodLossListener(plugin);
    }
    @Override
    public HashMap<UUID, BloodLoss> getHashmap(){
        return affectedPlayers;
    }
    public static BloodLossManager getInstance(){
        return instance;
    }
    @Override
    public void put(UUID uuid, BloodLoss process){
        affectedPlayers.put(uuid, process);
    }
    @Override
    public BloodLoss getInjury(UUID uuid){
        return getHashmap().get(uuid);
    }
    @Override
    public boolean isInjured(Player player){
        return isInjured(player.getUniqueId());
    }
    @Override
    public boolean isInjured(UUID uuid){
        return getHashmap().containsKey(uuid);
    }
    @Override
    public void affectPlayer(Player player){
        UUID uuid = player.getUniqueId();
        put(uuid, new BloodLoss(plugin, player));
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
            if (getInjury(uuid) != null) getInjury(uuid).cancel();
        }
    }

}

