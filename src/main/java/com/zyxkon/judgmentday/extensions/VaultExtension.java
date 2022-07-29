package com.zyxkon.judgmentday.extensions;
import com.zyxkon.judgmentday.Main;
import com.zyxkon.judgmentday.Utils;
import net.milkbowl.vault.economy.Economy;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.text.DecimalFormat;

public class VaultExtension implements Listener {
    static Main plugin;
    private static Economy eco = null;
    public VaultExtension(Main plugin){
        VaultExtension.plugin = plugin;
        if (plugin.hasPlugin("Vault")){
            RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
            VaultExtension.eco = rsp.getProvider();
            Bukkit.getPluginManager().registerEvents(this, plugin);
        }
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onKill(EntityDeathEvent event){
        Entity entity = event.getEntity();
        Player player = event.getEntity().getKiller();
        if (!(entity instanceof Zombie)) return;
        if (player == null) return;
        if (eco == null) return;
        if (!eco.isEnabled()) return;
        if (!eco.hasAccount(player)) return;
        double money = Utils.randRange(0, 300)/100.;
        DecimalFormat df = new DecimalFormat("#.##");
        Utils.sendActionBarMessage(player, Utils.translate(String.format("&a&l+&a%s&2$",df.format(money))));
        eco.depositPlayer(player, money);
//        //Normal ItemMeta stuff.
//        ItemStack dollar = new ItemStack(Material.ENDER_PEARL, 1);
//        ItemMeta im = dollar.getItemMeta();
//        im.setDisplayName("5$");
//        dollar.setItemMeta(im);
//
//        net.minecraft.server.v1_12_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(dollar);
//        NBTTagCompound compound = (nmsStack.hasTag() ? nmsStack.getTag() : new NBTTagCompound());
//        NBTTagList modifiers = new NBTTagList();
//
////        //Attributes are set like this:
////        NBTTagCompound damage = new NBTTagCompound();
////        damage.set("AttributeName", new NBTTagString("generic.attackDamage"));
////        damage.set("Name", new NBTTagString("generic.attackDamage"));
////        damage.set("Amount", new NBTTagInt(6 /*Item damage*/));
////        damage.set("Operation", new NBTTagInt(0));
////        damage.set("UUIDLeast", new NBTTagInt(894654));
////        damage.set("UUIDMost", new NBTTagInt(2872));
////        modifiers.add(damage);
//
//        //What you want.
//        NBTTagCompound speed = new NBTTagCompound();
//        speed.set("AttributeName", new NBTTagString("generic.movementSpeed"));
//        speed.set("Name", new NBTTagString("generic.movementSpeed"));
//        speed.set("Amount", new NBTTagDouble(0.7));
//        speed.set("Operation", new NBTTagInt(0));
//        speed.set("UUIDLeast", new NBTTagInt(894654));
//        speed.set("UUIDMost", new NBTTagInt(2872));
//        modifiers.add(speed);
//
//        //Tags like Unbreakable can be set like this:
//        compound.set("Unbreakable", new NBTTagByte((byte) 1));
//
//        compound.set("AttributeModifiers", modifiers);
//        nmsStack.setTag(compound);
//        dollar = CraftItemStack.asBukkitCopy(nmsStack);
//        event.getDrops().add(dollar);
//    }
//    @EventHandler
//    public void onPickup(EntityPickupItemEvent event){
//        Entity entity = event.getEntity();
//        if (!(entity instanceof Player)) return;
//        ItemStack item = (ItemStack) event.getItem();
////        if (item.isSimilar()){
////
////        }
    }
    public static double getMoney(Player player){
        return (eco != null ? eco.getBalance(player) : 0);
    }
}
