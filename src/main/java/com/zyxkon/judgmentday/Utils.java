package com.zyxkon.judgmentday;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Objective;

import java.util.*;

public final class Utils {
    public static String translate(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }
    public static int randRange(int a, int b){
        Random rand = new Random();
        return rand.nextInt((b - a) + 1) + a;
    }
    public static boolean isInRange(int n, int a, int b){
        return a <= n && n <= b;
    }
    public static boolean chance(float percentage){
        String str = Float.toString(percentage);
        int exponent = str.substring(str.indexOf('.')).length()-1;
        int base = (int) Math.pow(10, exponent);
        int significand = (int) (percentage*base);
        return isInRange(randRange(0, 100*base), 0, significand);
    }
    public static void healPlayer(Player player, double health){
        double normalHealth = player.getHealth();
        double maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        if (normalHealth+health <= maxHealth){
            player.setHealth(normalHealth+health);
            return;
        }
        player.setHealth(maxHealth);
    }
    public static void sendActionBarMessage(Player player, String message){
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
    }
    public static void addScore(Objective objective, ArrayList<String> strings){
        for (int i = 0; i<strings.size(); i++){
            objective.getScore(strings.get(i)).setScore(strings.size()-i-1);
        }
    }
    public static float chanceOfArmor(final float normalChance, Material material){
        float toReturn = normalChance;
        Material[] leatherArmor = {Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS};
        Material[] goldArmor = {Material.GOLD_HELMET, Material.GOLD_CHESTPLATE, Material.GOLD_LEGGINGS, Material.GOLD_BOOTS};
        Material[] chainmailArmor = {Material.CHAINMAIL_HELMET, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_BOOTS};
        Material[] ironArmor = {Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS};
        Material[] diamondArmor = {Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS};
        if (Arrays.asList(leatherArmor).contains(material)) toReturn -= toReturn*(30./100.);
        else if (Arrays.asList(goldArmor).contains(material)) toReturn -= toReturn*(40./100.);
        else if (Arrays.asList(chainmailArmor).contains(material)) toReturn -= toReturn*(50./100.);
        else if (Arrays.asList(ironArmor).contains(material)) toReturn -= toReturn*(90./100.);
        else if (Arrays.asList(diamondArmor).contains(material)) toReturn -= toReturn*(99.9/100.);
        return toReturn;
    }
    public static boolean randBool(){
        return (new Random()).nextBoolean();
    }
    public static String group(String delimiter, String... strings){
        return String.join(delimiter, strings);
    }
    public static boolean isVulnerable(Player player){
        GameMode gameMode = player.getGameMode();
        return (!(gameMode == GameMode.CREATIVE) && !(gameMode == GameMode.SPECTATOR));
    }
    public static boolean isSolid(Location location){
        return isSolid(location.getBlock());
    }
    public static boolean isSolid(Block block){
        Material[] fluids = {Material.STATIONARY_WATER, Material.WATER, Material.STATIONARY_LAVA, Material.LAVA};
        return (!block.isEmpty() && !Arrays.asList(fluids).contains(block.getType()));
    }
    public static <T> T randElement(ArrayList<T> arrayList){
        if (arrayList.isEmpty()) return null;
        int size = arrayList.size();
        Random random = new Random();
        return arrayList.get(random.nextInt(size));
    }
}
