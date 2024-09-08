package com.zyxkon.judgmentday.general_listeners;

import com.zyxkon.judgmentday.Main;
import com.zyxkon.judgmentday.Utils;
import net.minecraft.server.v1_12_R1.AttributeInstance;
import net.minecraft.server.v1_12_R1.EntityLiving;
import net.minecraft.server.v1_12_R1.EntityZombie;
import net.minecraft.server.v1_12_R1.GenericAttributes;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftZombie;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.CauldronLevelChangeEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Door;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Openable;

public class MainListener implements Listener {
    Main plugin;

    public MainListener(Main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onCombust(EntityCombustEvent event) {
        if (event.getEntity() instanceof Zombie) event.setCancelled(true);
    }

    @EventHandler
    public void onCauldronLevelChange(CauldronLevelChangeEvent event) {
        switch (event.getReason()) {
            case BUCKET_FILL: case BUCKET_EMPTY: case BOTTLE_FILL: case BOTTLE_EMPTY: case BANNER_WASH: case ARMOR_WASH:
            case EXTINGUISH:
                if (Utils.isVulnerable((Player) event.getEntity()))
                    event.setNewLevel(event.getOldLevel());
                break;
            case EVAPORATE: case UNKNOWN:
                event.setNewLevel(event.getOldLevel());
                break;
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Block clickedBlock = event.getClickedBlock();
        if (event.getPlayer().isSneaking()
                || event.getHand() == EquipmentSlot.OFF_HAND) return;
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        if (!(clickedBlock.getType() == Material.IRON_DOOR_BLOCK)) return;
        Door door = (Door) clickedBlock.getState().getData();
        if (door.isTopHalf()) clickedBlock = clickedBlock.getRelative(BlockFace.DOWN);
        BlockState state = clickedBlock.getState();
        Openable openable = (Openable) state.getData();
        Sound sound = openable.isOpen() ? Sound.BLOCK_IRON_DOOR_CLOSE : Sound.BLOCK_IRON_DOOR_OPEN;
        event.getPlayer().getWorld().playSound(clickedBlock.getLocation(), sound, 1, 1);
        openable.setOpen(!openable.isOpen());
        state.update();
        event.setCancelled(true);
    }
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event){
        Location loc = event.getEntity().getLocation();
        World w = loc.getWorld();
        loc.add(0, 1, 0);
        w.spawnParticle(Particle.BLOCK_CRACK, loc, 150, 0.2, 0.2, 0.2, new MaterialData(Material.REDSTONE_WIRE));
        MaterialData glass = new MaterialData(Material.STAINED_GLASS, (byte) 14);
        w.spawnParticle(Particle.BLOCK_CRACK, loc, 150, 0.2, 0.2, 0.2, glass);
    }
    @EventHandler
    public void onFallDamage(EntityDamageEvent event){
        Entity victim = event.getEntity();
        if (victim instanceof Zombie && event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            event.setCancelled(true);
            ((Zombie) victim).damage(event.getDamage()/3);
        }
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDeath(EntityDeathEvent event){
        Entity entity = event.getEntity();
        if (!(entity instanceof Zombie)) return;
        event.setDroppedExp(0);
    }
    @EventHandler
    public void onEntityKillByPlayer(EntityDeathEvent event){
        LivingEntity ent = event.getEntity();
        if (ent.getKiller() == null) return;
        EntityLiving _ent = ((CraftLivingEntity) ent).getHandle();
//        Main.broadcast("%s:"+ChatColor.RESET+" Speed(%f) and Dmg(%f)", _ent.getName(),
//                _ent.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue(),
//                _ent.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).getValue()
//        );
    }
    @EventHandler
    public void onPlayerDamageByEntity(EntityDamageByEntityEvent event){
        if (!(event.getEntity() instanceof Player)) return;
        Player p = (Player) event.getEntity();
        double dmg = event.getDamage();
        Entity attacker = event.getDamager();
//        Main.broadcast("%s got damaged (%f) by %s because of %s", p.getName(), dmg, attacker.getName(),
//                event.getCause());
    }
    @EventHandler
    public void onTarget(EntityTargetEvent event){
        Entity target = event.getTarget();
        Entity e = event.getEntity();
        if (!(target instanceof Player)) return;
        Player p = (Player) target;
//        Main.broadcast("%s has been targeted by %s", p.getName(), e.getName());
    }
}
