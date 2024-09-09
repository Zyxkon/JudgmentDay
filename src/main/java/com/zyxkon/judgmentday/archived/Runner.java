//package com.zyxkon.judgmentday;
//
//import net.md_5.bungee.api.ChatColor;
//import net.minecraft.server.v1_12_R1.*;
//import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
//
//import net.minecraft.server.v1_12_R1.EntityZombie;
//import org.bukkit.craftbukkit.v1_12_R1.util.UnsafeList;
//
//import java.lang.reflect.Field;
//import java.util.ArrayList;
//import java.util.List;
//
//public class Runner extends EntityZombie {
//    private static final List<Runner> runners = new ArrayList<>();
//    public Runner(org.bukkit.World world) {
//        super( ((CraftWorld) world).getHandle());
//        this.setCustomName(ChatColor.translateAlternateColorCodes('&', "&4&o&l&nRunner&r"));
//        this.setSize(3F, 3F);
//        this.setCustomNameVisible(true);
//
//        AttributeInstance speed = this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED);
//        AttributeInstance damage = this.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE);
//        speed.setValue(speed.getValue() * 2.25);
//        damage.setValue(damage.getValue() * 3);
////        try {
////            Field bField = PathfinderGoalSelector.class.getDeclaredField("b");
////            bField.setAccessible(true);
////            Field cField = PathfinderGoalSelector.class.getDeclaredField("c");
////            cField.setAccessible(true);
////            bField.set(goalSelector, new UnsafeList<PathfinderGoalSelector>());
////            bField.set(targetSelector, new UnsafeList<PathfinderGoalSelector>());
////            cField.set(goalSelector, new UnsafeList<PathfinderGoalSelector>());
////            cField.set(targetSelector, new UnsafeList<PathfinderGoalSelector>());
////        } catch (Exception exc) {
////            exc.printStackTrace();
////        }
//        this.goalSelector.a(0, new PathfinderGoalFloat(this));
//        this.goalSelector.a(2, new PathfinderGoalMoveTowardsRestriction(this, 1.0D));
//        this.goalSelector.a(2, new PathfinderGoalMoveThroughVillage(this, 1.0D, false));
//        this.goalSelector.a(4, new PathfinderGoalRandomStroll(this, 1.0D));
//        this.goalSelector.a(3, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
//        this.goalSelector.a(4, new PathfinderGoalRandomLookaround(this));
//        this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, true));
//        this.targetSelector.a(0, new PathfinderGoalNearestAttackableTarget<>(this, EntityZombie.class, true));
//        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<>(this, EntityHuman.class, true));
//        this.targetSelector.a(1, new PathfinderGoalNearestAttackableTarget<>(this, EntityCow.class, true));
//
//        runners.add(this);
//    }
//    public static List<Runner> getRunners(){
//        return runners;
//    }
//    public static void killAll(){
//        for (Runner r : runners){
//            runners.remove(r);
//            r.killEntity();
//        }
//    }
//
//}
//
//
