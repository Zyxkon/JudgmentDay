//package com.zyxkon.judgmentday.injuries.poisoning;
//
//import com.zyxkon.judgmentday.Main;
//import com.zyxkon.judgmentday.injuries.InjuryManager;
//import com.zyxkon.judgmentday.injuries.infection.Infection;
//import com.zyxkon.judgmentday.injuries.infection.InfectionListener;
//import org.bukkit.Bukkit;
//import org.bukkit.entity.Player;
//import org.bukkit.potion.PotionEffect;
//
//import java.util.HashMap;
//import java.util.UUID;
//
//public class PoisoningManager extends InjuryManager<Poisoning> {
//    private static final HashMap<UUID, Poisoning> affectedPlayers = new HashMap<>();
//    static Main plugin;
//    public static PoisoningManager instance;
//    public PoisoningManager(final Main plugin){
//        instance = this;
//        PoisoningManager.plugin = plugin;
//        new InfectionListener(plugin);
//    }
//    public static PoisoningManager getInstance(){
//        return instance;
//    }
//
//    @Override
//    public HashMap<UUID, Poisoning> getHashmap() {
//        return affectedPlayers;
//    }
//
//    @Override
//    public void put(UUID uuid, Poisoning process) {
//        affectedPlayers.put(uuid, process);
//    }
//    @Override
//    public Poisoning getInjury(UUID uuid){
//        return getHashmap().get(uuid);
//    }
//    @Override
//    public boolean isInjured(UUID uuid){
//        return getHashmap().containsKey(uuid);
//    }
//    @Override
//    public void affectPlayer(Player player){
//        UUID uuid = player.getUniqueId();
//        affectedPlayers.put(uuid, new Poisoning(plugin, player));
//    }
//    @Override
//    public boolean healPlayer(Player player){
//        UUID uuid = player.getUniqueId();
//        if (isInjured(uuid)) {
//            getInjury(uuid).cancel();
//            affectedPlayers.remove(uuid);
//            return true;
//        }
//        return false;
//    }
//    @Override
//    public void shutDown(){
//        for (UUID uuid : getHashmap().keySet()){
//            Player player = Bukkit.getPlayer(uuid);
//            for(PotionEffect potion : player.getActivePotionEffects()) player.removePotionEffect(potion.getType());
//            player.setWalkSpeed(getInjury(uuid).normalSpeed);
//            getInjury(uuid).cancel();
//        }
//    }
//}
