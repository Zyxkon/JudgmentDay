//package com.zyxkon.judgmentday.injuries.poisoning;
//
//import com.zyxkon.judgmentday.Main;
//import com.zyxkon.judgmentday.Utils;
//import org.bukkit.Bukkit;
//import org.bukkit.Material;
//import org.bukkit.event.EventHandler;
//import org.bukkit.event.Listener;
//import org.bukkit.event.player.PlayerItemConsumeEvent;
//import org.bukkit.inventory.ItemStack;
//
//public class PoisoningListener implements Listener {
//    private static PoisoningManager manager;
//    public PoisoningListener(Main plugin){
//        manager = PoisoningManager.getInstance();
//        Bukkit.getPluginManager().registerEvents(this, plugin);
//    }
//    @EventHandler
//    public void onEat(PlayerItemConsumeEvent event){
//        Material food = event.getItem().getType();
//        float chance = 0;
//        switch (food){
//            case POISONOUS_POTATO:
//            case ROTTEN_FLESH:
//            case SPIDER_EYE: {
//                chance += 75;
//                break;
//            }
//            case RAW_BEEF:
//            case RAW_CHICKEN:
//            case MUTTON:
//            case PORK:
//            case RABBIT:
//            case RAW_FISH:{
//                chance += 25;
//                break;
//            }
//            default:
//                chance = 0;
//        }
//        if (Utils.chance(chance)) manager.affectPlayer(event.getPlayer());
//    }
//}
