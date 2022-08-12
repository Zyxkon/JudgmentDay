package com.zyxkon.judgmentday;
import com.zyxkon.judgmentday.extensions.VaultExtension;
import com.zyxkon.judgmentday.injuries.bloodloss.BloodLossManager;
import com.zyxkon.judgmentday.thirst.ThirstManager;
import com.zyxkon.judgmentday.extensions.CrackShotExtension;
import com.zyxkon.judgmentday.injuries.impairment.ImpairmentManager;
import com.zyxkon.judgmentday.injuries.infection.InfectionManager;
import com.zyxkon.judgmentday.runnables.BarbedWireRunnable;
import com.zyxkon.judgmentday.runnables.ZombieSpawnRunnable;
import com.zyxkon.judgmentday.general_listeners.CreatureSpawnListener;
import com.zyxkon.judgmentday.general_listeners.MainListener;
import com.zyxkon.judgmentday.general_listeners.PlayerDeathListener;
import com.zyxkon.judgmentday.runnables.ScoreboardLoaderRunnable;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends JavaPlugin {
    private static Logger logger;
    private static Main instance;
    public static BloodLossManager bloodLossManager;
    public static ImpairmentManager impairmentManager;
    public static InfectionManager infectionManager;
    public static ThirstManager thirstManager;
    @Override
    public void onEnable(){
        Main.logger = this.getLogger();
        instance = this;
        File file = new File(getDataFolder() + File.separator);
        if (!file.exists()) file.mkdir();
        String[] externalPlugins = {"CrackShot", "WorldGuard", "WorldEdit", "Vault"};
        for (String str : externalPlugins){
            if (!hasPlugin(str)) log(Level.WARNING, String.format("Plugin %s is not installed!", str));
            else {
                log(Level.INFO, String.format("Plugin %s found! Begin loading...", str));
                switch (str){
                    case "CrackShot":
                        new CrackShotExtension(this);
                        break;
                    case "Vault":
                        new VaultExtension(this);
                        break;
                }
            }
        }
        setupManagers();
        new Commands(this);
        new ZCommand(this);

        new Counter(this);
        new MainListener(this);
        new CreatureSpawnListener(this);
        new PlayerDeathListener(this);

        new ScoreboardLoaderRunnable(this);
        new ZombieSpawnRunnable(this);
        new BarbedWireRunnable(this);
    }
    private void setupManagers(){
        bloodLossManager = new BloodLossManager(this);
        impairmentManager = new ImpairmentManager(this);
        infectionManager = new InfectionManager(this);
        thirstManager = new ThirstManager(this);
    }
    private void shutdownManagers(){
        bloodLossManager.shutDown();
        impairmentManager.shutDown();
        infectionManager.shutDown();
    }
    @Override
    public void onDisable(){
        ThirstManager.saveData();
        Counter.saveData();
        shutdownManagers();
    }
    public void log(Level level, String str){
        logger.log(level, str);
    }
    public static Main getInstance(){
        return instance;
    }
    public boolean hasPlugin(String name){
        return this.getServer().getPluginManager().getPlugin(name) != null;
    }
}
