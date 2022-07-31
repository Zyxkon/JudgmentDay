package com.zyxkon.judgmentday.injuries;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public abstract class InjuryManager<I> {
    public abstract HashMap<UUID, I> getHashmap();
    public abstract void put(UUID uuid, I process);
    public abstract I getInjury(UUID uuid);
    public boolean isInjured(Player player){
        return isInjured(player.getUniqueId());
    }
    public abstract boolean isInjured(UUID uuid);
    public abstract void affectPlayer(Player player);
    public abstract boolean healPlayer(Player player);
    public abstract void shutDown();
}
