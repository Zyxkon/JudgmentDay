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
import java.util.UUID;

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
        ArrayList<String> strings = new ArrayList<>();
        String injured = Utils.translate("&l&6»&eInjured: &r");
        if (!bleeding && !impaired && !infected) injured = injured.concat(Utils.translate("&aNo"));
        else injured = injured.concat(Utils.translate("&4&n&lYES"));
        strings.add(injured);
        if (bleeding) strings.add(Utils.translate("&c  •&nBLEEDING"));
        if (impaired) strings.add(Utils.translate("&6  •&nIMPAIRED"));
        if (infected) strings.add(Utils.translate("&2  •&nINFECTED"));
        int mobsKilled = Counter.getMobKills(uuid);
        strings.add(Utils.translate("&l&8»&7Mobs killed: " + mobsKilled));
        int playersKilled = Counter.getPlayerKills(uuid);
        strings.add(Utils.translate("&l&2»&aPlayers killed: " + playersKilled));
        int deaths = Counter.getDeaths(uuid);
        strings.add(Utils.translate("&l&4»&cDeaths: " + deaths));
        strings.add(Utils.translate("&l&3»&b&nHydration&r&b: " + ThirstManager.formatThirst(thirst) + ChatColor.BOLD + thirst + "%"));
        ArrayList<String> regions = WorldGuardExtension.getRegion(player);
        String location = Utils.translate("&l&1»&l&9Location: &r").concat(regions.isEmpty() ? "Unknown" : String.join("-", regions));
        strings.add(location);
        Utils.addScore(objective, strings);
        player.setScoreboard(board);
    }
}
