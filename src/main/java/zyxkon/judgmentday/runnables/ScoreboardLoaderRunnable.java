package zyxkon.judgmentday.runnables;
import zyxkon.judgmentday.Counter;
import zyxkon.judgmentday.Main;
import zyxkon.judgmentday.Utils;
import zyxkon.judgmentday.extensions.WorldGuardExtension;
import zyxkon.judgmentday.injuries.bloodloss.BloodLossManager;
import zyxkon.judgmentday.injuries.impairment.ImpairmentManager;
import zyxkon.judgmentday.injuries.infection.InfectionManager;
import zyxkon.judgmentday.thirst.ThirstManager;
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
        int thirst = ThirstManager.thirstPlayers.get(uuid);
        boolean bleeding = BloodLossManager.affectedPlayers.containsKey(uuid);
        boolean impaired = ImpairmentManager.affectedPlayers.containsKey(uuid);
        boolean infected = InfectionManager.affectedPlayers.containsKey(uuid);
        ArrayList<String> strings = new ArrayList<>();
        String injured = Utils.translate("&l&6»&eInjured: &r");
        if (!bleeding && !impaired && !infected) injured = injured.concat(Utils.translate("&aNo"));
        else injured = injured.concat(Utils.translate("&4&n&lYES"));
        strings.add(injured);
        if (bleeding) strings.add(Utils.translate("&c  •&nBLEEDING"));
        if (impaired) strings.add(Utils.translate("&6  •&nIMPAIRED"));
        if (infected) strings.add(Utils.translate("&2  •&nINFECTED"));
        Integer mobsKilled = Counter.mobKills.get(uuid);
        strings.add(Utils.translate("&l&8»&7Mobs killed: " + mobsKilled));
        Integer playersKilled = Counter.playerKills.get(uuid);
        strings.add(Utils.translate("&l&2»&aPlayers killed: " + playersKilled));
        Integer deaths = Counter.deaths.get(uuid);
        strings.add(Utils.translate("&l&4»&cDeaths: " + deaths));
        strings.add(Utils.translate("&l&3»&b&nHydration&r&b: " + ThirstManager.formatThirst(thirst) + ChatColor.BOLD + thirst + "%"));
        ArrayList<String> regions = WorldGuardExtension.getRegion(player);
        String location = Utils.translate("&l&1»&l&9Location: &r").concat(regions.isEmpty() ? "Unknown" : String.join("-", regions));
        strings.add(location);
        Utils.addScore(objective, strings);
        player.setScoreboard(board);
    }
}
