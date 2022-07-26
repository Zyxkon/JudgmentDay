package com.zyxkon.judgmentday.runnables;
import com.zyxkon.judgmentday.Counter;
import com.zyxkon.judgmentday.Main;
import com.zyxkon.judgmentday.Utils;
import com.zyxkon.judgmentday.injuries.bloodloss.BloodLossManager;
import com.zyxkon.judgmentday.thirst.ThirstManager;
import com.zyxkon.judgmentday.extensions.WorldGuardExtension;
import com.zyxkon.judgmentday.injuries.impairment.ImpairmentManager;
import com.zyxkon.judgmentday.injuries.infection.InfectionManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

public class ScoreboardLoaderRunnable extends BukkitRunnable {
    private final Main plugin;
    ScoreboardManager manager = Bukkit.getScoreboardManager();
    public ScoreboardLoaderRunnable(Main plugin){
        this.plugin = plugin;
        this.runTaskTimer(plugin, 0L, 1L);
    }
    @Override
    public void run(){
        for (Player player: plugin.getServer().getOnlinePlayers()){
            loadStats(player);
        }
    }
    public void loadStats(Player player){
        UUID uuid = player.getUniqueId();
        Scoreboard board = manager.getNewScoreboard();
        Objective objective = board.registerNewObjective("Stats", "dummy");
        objective.setDisplayName(Utils.translate("&c&n"+player.getName()));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        int thirst = ThirstManager.getThirst(player);
        boolean bleeding = BloodLossManager.isInjured(player);
        boolean impaired = ImpairmentManager.isInjured(player);
        boolean infected = InfectionManager.isInjured(player);
        ArrayList<String> scores = new ArrayList<>();
        scores.add("&6&l»&e❤ Status: &r" + ((!bleeding && !impaired && !infected) ? "&aNo" : "&4&n&lYES"));
        if (bleeding) scores.add("&c  •&nBLEEDING");
        if (impaired) scores.add("&6  •&nIMPAIRED");
        if (infected) scores.add("&2  •&nINFECTED");
        scores.add("&8&l»&7⚔ Mobs killed: " + Counter.getMobKills(uuid));
        scores.add("&2&l»&a⨁ Players killed: &c&l" + Counter.getPlayerKills(uuid));
        scores.add("&4&l»&c☠ Deaths: " + Counter.getDeaths(uuid));
        scores.add("&3&l»&b&nHydration&r&b: " + ThirstManager.formatThirst(thirst) + ChatColor.BOLD + thirst + "%");
        ArrayList<String> regions = WorldGuardExtension.getRegion(player);
        scores.add("&1&l»&l&9۩ Location: &r" + (regions.isEmpty() ? "Unknown" : String.join("-", regions)));
        scores = (ArrayList<String>) scores.stream().map(Utils::translate);
        Utils.addScore(objective, scores);
        player.setScoreboard(board);
    }
}
