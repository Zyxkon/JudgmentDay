package com.zyxkon.judgmentday;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Objective;

import java.util.*;
import java.util.stream.Collectors;

public final class Utils {
    public static String translate(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }
    public static int randRange(int a, int b){
        Random rand = new Random();
        return rand.nextInt((b - a) + 1) + a;
    }
    public static double randRange(double a, double b, double digits){
        double mul = Math.pow(10, digits);
        return randRange( (int) (a*mul), (int) (b*mul) )/mul;
    }
    public static double randRange(double a, double b){
        return randRange(a, b, 5);
    }
    public static double randRange(double b){
        return randRange(0, b);
    }
    public static boolean isInRange(double n, double a, double b){
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
    static public <T> T randElement(ArrayList<T> arrayList){
        if (arrayList.isEmpty()) return null;
        int size = arrayList.size();
        Random random = new Random();
        return arrayList.get(random.nextInt(size));
    }
    public static boolean equatesTo(String s1, String s2){
        if (s1.length() > s2.length()){
            return false;
        }
        for (int i = 0; i<s1.length(); i++){
            if (s1.charAt(i) != s2.charAt(i)){
                return false;
            }
        }
        return true;
    }
    public static boolean isValidPlayer(Player p){
        return Arrays.stream(Main.getInstance().getServer().getOfflinePlayers()).anyMatch(s -> s == p);
    }
    public static boolean isValidSpawnPosition(Location loc){
        Block bl = loc.getBlock();
        return !Utils.isSolid(bl.getRelative(BlockFace.UP))
                && !Utils.isSolid(bl)
                && Utils.isSolid(bl.getRelative(BlockFace.DOWN));
    }
    public static Location getRandomLocationWithinRange(
            Location centerLocation, int x_range, int y_range, int z_range,
            int horizontalDistanceFromCenter,
            int verticalDistanceFromCenter){
        World world = centerLocation.getWorld();
        int randXoffset = Utils.randRange(horizontalDistanceFromCenter,x_range);
        int randYoffset = Utils.randRange(verticalDistanceFromCenter, y_range);
        int randZoffset = Utils.randRange(horizontalDistanceFromCenter,z_range);
        if (randBool()) randXoffset *= -1;
        if (randBool()) randYoffset *= -1;
        if (randBool()) randZoffset *= -1;
        double x = centerLocation.getX();
        double y = centerLocation.getY();
        double z = centerLocation.getZ();
        return new Location(world, x+randXoffset, y+randYoffset, z+randZoffset);
    }
    public static Color randColor(int nCombination){
        Color[] colors = {
                Color.WHITE, Color.SILVER, Color.GRAY, Color.BLACK, Color.RED, Color.MAROON, Color.YELLOW,
                Color.OLIVE, Color.LIME, Color.GREEN, Color.AQUA, Color.TEAL, Color.BLUE, Color.NAVY, Color.FUCHSIA,
                Color.PURPLE, Color.ORANGE
        };
        Color r = colors[(int) Utils.randRange(colors.length)];
        for (int i = 0; i < nCombination; i++){
            r = r.mixColors(colors[(int) Utils.randRange(colors.length)]);
        }
        //    1 public static final Color WHITE = fromRGB(16777215);
        //    2 public static final Color SILVER = fromRGB(12632256);
        //    3 public static final Color GRAY = fromRGB(8421504);
        //    4 public static final Color BLACK = fromRGB(0);
        //    5 public static final Color RED = fromRGB(16711680);
        //    6 public static final Color MAROON = fromRGB(8388608);
        //    7 public static final Color YELLOW = fromRGB(16776960);

        //    8 public static final Color OLIVE = fromRGB(8421376);
        //    9 public static final Color LIME = fromRGB(65280);
        //    10 public static final Color GREEN = fromRGB(32768);
        //    11 public static final Color AQUA = fromRGB(65535);
        //    12 public static final Color TEAL = fromRGB(32896);
        //    13 public static final Color BLUE = fromRGB(255);
        //    14 public static final Color NAVY = fromRGB(128);
        //    15 public static final Color FUCHSIA = fromRGB(16711935);
        //    16 public static final Color PURPLE = fromRGB(8388736);
        //    17 public static final Color ORANGE = fromRGB(16753920);
        return r;
    }

    public static String returnUsage(Class<?>... args){
        List<String> params = new ArrayList<>();
        Arrays.stream(args).forEach(
                arg -> params
                        .add(
                                arg.isEnum()
                                        ? Arrays.stream(arg.getEnumConstants())
                                        .map(Object::toString)
                                        .collect(Collectors.joining("|"))
                                        : arg.getSimpleName()
                        )
        );
        return (params.stream()
                .map(s -> "[" + s + "]"))
                .collect(Collectors.joining(" "));
    }
    public static void sendMultilineMessage(CommandSender p, String m){
        for (String s : m.split("\n")){
            p.sendMessage("["+Main.commandName+"] "+s);
        }
    }
}
