package com.zyxkon.judgmentday.scoreboard;
import com.zyxkon.judgmentday.Counter;
import com.zyxkon.judgmentday.Main;
import com.zyxkon.judgmentday.Utils;
import com.zyxkon.judgmentday.extensions.Extension;
import com.zyxkon.judgmentday.extensions.VaultExtension;
import com.zyxkon.judgmentday.injuries.bloodloss.BloodLossManager;
import com.zyxkon.judgmentday.injuries.poisoning.PoisoningManager;
import com.zyxkon.judgmentday.thirst.ThirstManager;
import com.zyxkon.judgmentday.extensions.WorldGuardExtension;
import com.zyxkon.judgmentday.injuries.impairment.ImpairmentManager;
import com.zyxkon.judgmentday.injuries.infection.InfectionManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

public class ScoreboardLoader {
    static ScoreboardManager manager = Bukkit.getScoreboardManager();
    public ScoreboardLoader(Main plugin){
        Bukkit.getPluginManager().registerEvents(new ScoreboardListener(plugin), plugin);
//        (new BukkitRunnable(){
//
//            @Override
//            public void run() {
//                for (Player p : Bukkit.getOnlinePlayers()){
//                    loadStats(p);
//                }
//            }
//        }).runTaskTimer(plugin, 0L, 5L);
    }
    public static void loadStats(Player player){
        UUID uuid = player.getUniqueId();
        Scoreboard board = manager.getNewScoreboard();
        Objective objective = board.registerNewObjective("Stats", "dummy");
        objective.setDisplayName(Utils.translate("&c&n"+player.getName()));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        int thirst = ThirstManager.getThirst(player);
        ArrayList<String> scores = new ArrayList<>();
        int count = 0;
        String status = "&6&l»&c&l❤&eStatus: &r";
        ArrayList<String> injuries = new ArrayList<>();
        if (BloodLossManager.getInstance().isInjured(player)) {
            injuries.add("&c    •&nBLEEDING");
            count++;
        }
        if (ImpairmentManager.getInstance().isInjured(player)) {
            injuries.add("&6    •&nIMPAIRED");
            count++;
        }

        if (InfectionManager.getInstance().isInjured(player)) {
            injuries.add("&2    •&nINFECTED");
            count++;

        }if (PoisoningManager.getInstance().isInjured(player)) {
            injuries.add("&5    •&nPOISONED");
            count++;
        }
        switch (count){
            case 0:
                status += "&aFine";
                break;
            case 1:
            case 2:
                status += "&6&nCaution";
                break;
            default:
                status += "&4&l&nDanger";
        }
        scores.add(status);
        scores.addAll(injuries);
        scores.add("&8&l»&7&l☣&7Walkers killed: " + Counter.getWalkerKills(uuid));
        scores.add("&5&l»&d&l⚔&dPlayers killed: " + Counter.getPlayerKills(uuid));
        scores.add("&4&l»&c&l✞&cDeaths: " + Counter.getDeaths(uuid));
        if (Extension.VAULT.isLoaded()){
            DecimalFormat df = new DecimalFormat("#.##");
            String money = df.format(VaultExtension.getMoney(player));
            int index = money.indexOf('.');
            if (index == -1){
                index = money.length()-1;
            }
            String format = money.substring(0, index);
            if (VaultExtension.getMoney(player) != 0) money = String.format("%,d",Integer.parseInt(format))+money.substring(index);
            scores.add(String.format("&2&l»&a&l＄&aBalance: %s$", money));
        }
        if (Extension.WORLDGUARD.isLoaded()){
            ArrayList<String> regions = WorldGuardExtension.getRegions(player);
            if (!regions.isEmpty()){
                for (int i = 0; i<regions.size(); i++) {
                    String str = regions.get(i);
                    regions.set(i, str.substring(0, 1).toUpperCase() + str.substring(1));
                }
            }
            scores.add("&1&l»&9&l۩&9Location: &r" + (regions.isEmpty() ? "Unknown" : String.join("-", regions)));
        }
        scores.add("&3&l»&b&nHydration&r&b: " + ThirstManager.formatThirst(thirst) + ChatColor.BOLD + thirst + "%");
        scores = (ArrayList<String>) scores.stream().map(Utils::translate).collect(Collectors.toList());
        Utils.addScore(objective, scores);
        player.setScoreboard(board);
    }
}

