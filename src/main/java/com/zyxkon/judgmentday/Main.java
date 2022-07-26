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
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Main extends JavaPlugin {
    private static Economy econ = null;
    @Override
    public void onEnable(){
        File file = new File(this.getDataFolder() + File.separator);
        if (!file.exists()) file.mkdir();
        String[] externalPlugins = {"CrackShot", "WorldGuard", "WorldEdit", "Vault"};
        for (String str : externalPlugins){
            Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin(str);
            if (plugin == null) System.out.printf("WARNING: Plugin %s is not installed!%n\n", str);
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
