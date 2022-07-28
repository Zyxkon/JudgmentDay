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
import com.zyxkon.judgmentday.general_listeners.EntityDamageListener;
import com.zyxkon.judgmentday.general_listeners.MainListener;
import com.zyxkon.judgmentday.general_listeners.PlayerDeathListener;
import com.zyxkon.judgmentday.runnables.ScoreboardLoaderRunnable;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends JavaPlugin {
    private static Logger logger;
    private static File regionsFile;
    private static FileConfiguration regionsConfig;
    private static Main instance;
    @Override
    public void onEnable(){
        Main.logger = this.getLogger();
        instance = this;
        initializeData();
        if (!regionsFile.exists()){
            saveResource("regions.yml", false);
        }
        String[] externalPlugins = {"CrackShot", "WorldGuard", "WorldEdit", "Vault"};
        for (String str : externalPlugins){
            if (hasPlugin(str)) log(Level.WARNING, String.format("Plugin %s is not installed!", str));
            else log(Level.INFO, String.format("Plugin %s found! Begin loading...", str));
            switch (str){
                case "CrackShot":
                    new CrackShotExtension(this);
                    break;
                case "Vault":
                    new VaultExtension(this);
                    break;
            }
        }
        new ImpairmentManager(this);
        new InfectionManager(this);
        new BloodLossManager(this);
        new ThirstManager(this);

        new Commands(this);
        new ZCommand(this);

        new Counter(this);
        new MainListener(this);
        new CreatureSpawnListener(this);
        new PlayerDeathListener(this);
        new EntityDamageListener(this);

        new ScoreboardLoaderRunnable(this);
        new ZombieSpawnRunnable(this);
        new BarbedWireRunnable(this);
    }
    @Override
    public void onDisable(){
        ThirstManager.saveData();
        Counter.saveData();
        BloodLossManager.shutDown();
        ImpairmentManager.shutDown();
        InfectionManager.shutDown();
    }
    public void log(Level level, String str){
        logger.log(level, str);
    }
    private void initializeData(){
        File file = new File(getDataFolder() + File.separator);
        if (!file.exists()) file.mkdir();
        reload();
    }
    public void reload(){
        regionsFile = new File(getDataFolder(), "regions.yml");
        regionsConfig = YamlConfiguration.loadConfiguration(regionsFile);
    }
    public File getRegionsFile(){
        return regionsFile;
    }
    public FileConfiguration getRegionsConfig(){
        return regionsConfig;
    }
    public static Main getInstance(){
        return instance;
    }
    public boolean hasPlugin(String name){
        return this.getServer().getPluginManager().getPlugin(name) != null;
    }
}
