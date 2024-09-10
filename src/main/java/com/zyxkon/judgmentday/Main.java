package com.zyxkon.judgmentday;
import com.zyxkon.judgmentday.commands.ZCommand;
import com.zyxkon.judgmentday.extensions.*;
import com.zyxkon.judgmentday.general_listeners.*;
import com.zyxkon.judgmentday.injuries.*;
import com.zyxkon.judgmentday.injuries.bloodloss.BloodLossManager;
import com.zyxkon.judgmentday.injuries.impairment.ImpairmentManager;
import com.zyxkon.judgmentday.injuries.infection.InfectionManager;
import com.zyxkon.judgmentday.injuries.poisoning.PoisoningManager;
import com.zyxkon.judgmentday.runnables.BarbedWireRunnable;
import com.zyxkon.judgmentday.scoreboard.ScoreboardLoader;
import com.zyxkon.judgmentday.runnables.ZombieSpawnRunnable;
import com.zyxkon.judgmentday.thirst.ThirstManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends JavaPlugin {
    private static Logger logger;
    private static Main instance;
    public static BloodLossManager bloodLossManager;
    public static ImpairmentManager impairmentManager;
    public static InfectionManager infectionManager;
    public static PoisoningManager poisoningManager;
    public static ThirstManager thirstManager;
    public static InjuryManager<?>[] injuryManagers;
    public static final String commandName = "judgmentday";
    public static boolean testing = true;

    @Override
    public void onEnable(){
        Main.logger = this.getLogger();
        instance = this;
        File file = new File(getDataFolder() + File.separator);
        if (!file.exists()) file.mkdir();
        for (Extension ext : Extension.values() ){
            if (!hasPlugin(ext.pluginName))
                log(Level.WARNING, String.format("Plugin %s is not installed!", ext.pluginName));
            else {
                try {
                    log(Level.INFO, String.format("Plugin %s found! Attempting hooking...", ext.pluginName));
                    if (Bukkit.getPluginManager().getPlugin(ext.pluginName).isEnabled()){
                        log(Level.INFO, String.format("Plugin %s is ENABLED! Commencing hooking.", ext.pluginName));
                        switch (ext) {
                            case CRACKSHOT:
                                new CrackShotExtension();
                                break;
                            case VAULT:
                                new VaultExtension();
                                break;
                            case WORLDGUARD:
                                new WorldGuardExtension();
                                break;
                            case MYTHICMOBS:
                                new MythicMobsExtension();
                                break;
                        }
                        ext.loadStatus(true);
                        consoleSend(ChatColor.GREEN+"Successfully hooked into %s plugin!", ext.pluginName);
                    }
                    else {
                        consoleSend(ChatColor.RED+"Plugin %s is NOT ENABLED. Hooking failed.", ext.pluginName);
                    }
                } catch (NoClassDefFoundError | ExceptionInInitializerError | Exception e){
                    e.printStackTrace();
                    consoleSend(ChatColor.RED+"Plugin %s could NOT be loaded!", ext.pluginName);
                    ext.loadStatus(false);
                }
            }
        }
        setupManagers();
        consoleSend(ChatColor.GREEN +"Set up managers!");
        setupCommands();
        consoleSend(ChatColor.GREEN +"Set up commands!");
        setupListeners();
        consoleSend(ChatColor.GREEN +"Set up listeners!");
        setupRunnables();
        consoleSend(ChatColor.GREEN +"Set up runnables!");
        new Counter(this);
        consoleSend(ChatColor.GREEN +"Set up counter!");
    }
    private void setupManagers(){
        bloodLossManager = new BloodLossManager(this);
        impairmentManager = new ImpairmentManager(this);
        infectionManager = new InfectionManager(this);
        poisoningManager = new PoisoningManager(this);
        injuryManagers = new InjuryManager[]{
                bloodLossManager, impairmentManager, infectionManager, poisoningManager
        };
        thirstManager = new ThirstManager(this);
    }
    private void setupCommands(){
        new Commands(this);
        new ZCommand(this);
    }
    private void setupListeners(){
        new MainListener(this);
        new CreatureSpawnListener(this);
        new PlayerDeathListener(this);
    }
    private void setupRunnables(){
        new ScoreboardLoader(this);
        new ZombieSpawnRunnable(this);
        new BarbedWireRunnable(this);
    }
    private void shutdownManagers(){
        bloodLossManager.shutDown();
        impairmentManager.shutDown();
        infectionManager.shutDown();
        poisoningManager.shutDown();
    }

    @Override
    public void onDisable(){
        ThirstManager.saveData();
        log(Level.INFO, "Saving Thirst data...");
        Counter.saveData();
        log(Level.INFO, "Saving Counter data...");
        shutdownManagers();
        log(Level.INFO, "Shutting down Managers...");
    }
    public static void log(Level level, String str){
        logger.log(level, str);
    }
    private static void broadcast(String str, Object... strs){
        for (Player p : Main.getInstance().getServer().getOnlinePlayers()){
            String fm = String.format(str, strs);
            p.sendMessage(fm);
        }
    }
    public static void testBroadcast(String str, Object... strs){
        if (testing) {
            broadcast(str, strs);
            return;
        }
        for (String name : getInstance().getDescription().getAuthors()){
            Player p = Bukkit.getPlayer(name);
            if (p != null) {
                String fm = String.format(str, strs);
                p.sendMessage(fm);
            }
        }
    }
    public static void consoleSend(String str, Object... strs){
        Bukkit.getServer().getConsoleSender().sendMessage(
                String.format("[%s] ", instance.getName()) + String.format(str,strs)
        );
    }
    public static Main getInstance(){
        return instance;
    }
    public static boolean hasPlugin(Extension ext){
        return hasPlugin(ext.pluginName);
    }
    public static boolean hasPlugin(String name){
        return instance.getServer().getPluginManager().getPlugin(name) != null;
    }
}
