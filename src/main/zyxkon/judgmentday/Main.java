package main.zyxkon.judgmentday;
import main.zyxkon.judgmentday.extensions.CrackShotExtension;
import main.zyxkon.judgmentday.injuries.bloodloss.BloodLossManager;
import main.zyxkon.judgmentday.injuries.impairment.ImpairmentManager;
import main.zyxkon.judgmentday.injuries.infection.InfectionManager;
import main.zyxkon.judgmentday.runnables.BarbedWireRunnable;
import main.zyxkon.judgmentday.runnables.ZombieSpawnRunnable;
import main.zyxkon.judgmentday.thirst.ThirstManager;
import main.zyxkon.judgmentday.general_listeners.CreatureSpawnListener;
import main.zyxkon.judgmentday.general_listeners.EntityDamageListener;
import main.zyxkon.judgmentday.general_listeners.MainListener;
import main.zyxkon.judgmentday.general_listeners.PlayerDeathListener;
import main.zyxkon.judgmentday.runnables.ScoreboardLoaderRunnable;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Main extends JavaPlugin {
    @Override
    public void onEnable(){
        File file = new File(this.getDataFolder() + File.separator);
        if (!file.exists()) file.mkdir();
        String[] externalPlugins = {"CrackShot", "WorldGuard", "WorldEdit"};
        for (String str : externalPlugins){
            Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin(str);
            if (plugin == null) System.out.printf("WARNING: Plugin %s is not installed!%n\n", str);
            else if (str.equals("CrackShot")) new CrackShotExtension(this);
        }
        new ImpairmentManager(this);
        new InfectionManager(this);
        new BloodLossManager(this);
        new ThirstManager(this);

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
}
