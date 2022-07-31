package com.zyxkon.judgmentday.extensions;
import com.zyxkon.judgmentday.Main;
import com.zyxkon.judgmentday.Utils;
import net.milkbowl.vault.economy.Economy;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
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
    private static final ItemStack physicalDollar;
    static {
        ItemStack dollar = new ItemStack(Material.ENDER_PEARL, 1);
        ItemMeta im = dollar.getItemMeta();
        im.setDisplayName(Utils.translate("&a&l＄＄＄"));
        dollar.setItemMeta(im);
        net.minecraft.server.v1_12_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(dollar);
        NBTTagCompound compound = (nmsStack.hasTag() ? nmsStack.getTag() : new NBTTagCompound());
        NBTTagList modifiers = new NBTTagList();
        NBTTagCompound modifier = new NBTTagCompound();
        modifier.set("AttributeName", new NBTTagString("generic.attackSpeed"));
        modifier.set("Name", new NBTTagString("generic.attackSpeed"));
        modifier.set("Amount", new NBTTagDouble(1));
        modifier.set("Operation", new NBTTagInt(0));
        modifier.set("UUIDLeast", new NBTTagInt(123));
        modifier.set("UUIDMost", new NBTTagInt(321));
        modifier.set("Slot", new NBTTagString("chest"));
        modifiers.add(modifier);
        compound.set("AttributeModifiers", modifiers);
        compound.set("Unbreakable", new NBTTagByte((byte) 1));
        nmsStack.setTag(compound);
        physicalDollar = CraftItemStack.asBukkitCopy(nmsStack);
    }
    public VaultExtension(Main plugin){
        VaultExtension.plugin = plugin;
        if (!plugin.hasPlugin("Vault")) return;
        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        VaultExtension.eco = rsp.getProvider();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler
    public void onKill(EntityDeathEvent event){
        Entity entity = event.getEntity();
        if (!(entity instanceof Zombie)) return;
        if (eco == null) return;
        if (!eco.isEnabled()) return;
        event.getDrops().clear();
        event.getDrops().add(physicalDollar);
    }
    @EventHandler
    public void onPickup(EntityPickupItemEvent event){
        Entity entity = event.getEntity();
        if (!(entity instanceof Player)) return;
        ItemStack item = event.getItem().getItemStack();
        Player player = (Player) entity;
        if (item.isSimilar(physicalDollar)){
            event.getItem().remove();
            event.setCancelled(true);
            double money = Utils.randRange(50, 300)/100.;
            DecimalFormat df = new DecimalFormat("#.##");
            Utils.sendActionBarMessage(player, Utils.translate(String.format("&a&l+&a%s&2$",df.format(money))));
            eco.depositPlayer(player, money);
        }
    }
    public static double getMoney(Player player){
        return (eco != null ? eco.getBalance(player) : 0);
    }
}
