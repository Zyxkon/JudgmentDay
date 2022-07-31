package com.zyxkon.judgmentday.injuries.infection;

import com.zyxkon.judgmentday.Main;
import com.zyxkon.judgmentday.injuries.InjuryManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffect;

import java.util.UUID;
import java.util.HashMap;

public class InfectionManager extends InjuryManager<Infection> {
    private static final HashMap<UUID, Infection> affectedPlayers = new HashMap<>();
    static Main plugin;
    public static InfectionManager instance;
    public InfectionManager(Main plugin){
        InfectionManager.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(new InfectionListener(), plugin);
        instance = this;
    }
    public static InfectionManager getInstance(){
        return instance;
    }

    @Override
    public HashMap<UUID, Infection> getHashmap() {
        return affectedPlayers;
    }
    @Override
    public void put(UUID uuid, Infection process) {
        affectedPlayers.put(uuid, process);
    }
    @Override
    public Infection getInjury(UUID uuid){
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
        affectedPlayers.put(uuid, new Infection(plugin, player));
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
            for(PotionEffect potion : player.getActivePotionEffects()) player.removePotionEffect(potion.getType());
            player.setWalkSpeed(getInjury(uuid).normalSpeed);
            getInjury(uuid).cancel();
        }
    }

}

