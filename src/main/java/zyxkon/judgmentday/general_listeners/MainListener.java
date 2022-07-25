package zyxkon.judgmentday.general_listeners;

import zyxkon.judgmentday.Main;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.CauldronLevelChangeEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.material.Door;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Openable;

public class MainListener implements Listener {
    Main plugin;
    public MainListener(Main plugin){
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler
    public void onCombust(EntityCombustEvent event){
        if (event.getEntity() instanceof Zombie) event.setCancelled(true);
    }
    @EventHandler
    public void onCauldronLevelChange(CauldronLevelChangeEvent event){
        switch (event.getReason()){
            case BUCKET_FILL: case BUCKET_EMPTY: case BOTTLE_FILL:
                case BOTTLE_EMPTY: case BANNER_WASH: case ARMOR_WASH: case EXTINGUISH:
                    if (((Player) event.getEntity()).getGameMode() == GameMode.SURVIVAL) event.setNewLevel(event.getOldLevel());
                    break;
            case EVAPORATE: case UNKNOWN:
                event.setNewLevel(event.getOldLevel());
                break;
        }
    }
    @EventHandler
    public void onBreak(BlockBreakEvent event){
        Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.SURVIVAL) event.setCancelled(true);
    }
    @EventHandler
    public void onPlace(BlockPlaceEvent event){
        Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.SURVIVAL) event.setCancelled(true);
    }
    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        Block clickedBlock = event.getClickedBlock();
        if (event.getPlayer().isSneaking()
                || event.getHand() == EquipmentSlot.OFF_HAND) return;
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            if (clickedBlock.getType() == Material.IRON_DOOR_BLOCK){
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
        }
    }
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event){
        Location loc = event.getEntity().getLocation();
        World w = loc.getWorld();
        loc.add(0, 1, 0);
        w.spawnParticle(Particle.BLOCK_CRACK, loc, 150, 0.2, 0.2, 0.2, new MaterialData(Material.REDSTONE_WIRE));
    }
}
