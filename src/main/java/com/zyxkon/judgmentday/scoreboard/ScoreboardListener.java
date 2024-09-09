package com.zyxkon.judgmentday.scoreboard;

import com.zyxkon.judgmentday.Main;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;

class ScoreboardListener implements Listener {
    Main plugin;
    public ScoreboardListener(Main plugin){
        this.plugin = plugin;
    }
    @EventHandler
    public void onEntityKillByPlayer(EntityDeathEvent event){
        Player killer = event.getEntity().getKiller();
        if (killer == null) return;
        ScoreboardLoader.loadStats(killer);
    }
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        Player killer = event.getEntity().getKiller();
        if (!(killer == null)) {
            ScoreboardLoader.loadStats(killer);
        }
        ScoreboardLoader.loadStats(event.getEntity());
    }
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event){
        ScoreboardLoader.loadStats(event.getPlayer());
    }
    @EventHandler
    public void onPlayerHurt(EntityDamageEvent event){
        Entity e = event.getEntity();
        if (!(e instanceof Player)) return;
        ScoreboardLoader.loadStats( (Player) e );
    }
}