package com.zyxkon.judgmentday.mobs.zombies;

import com.zyxkon.judgmentday.Main;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;

import net.minecraft.server.v1_12_R1.EntityZombie;

import java.util.ArrayList;
import java.util.List;

public class Runner extends EntityZombie {
    private static final List<Runner> runners = new ArrayList<>();
    public Runner(org.bukkit.World world) {
        super( ((CraftWorld) world).getHandle());
        this.setCustomName(ChatColor.translateAlternateColorCodes('&', "&4&o&l&nRunner"));
        this.setSize(3F, 3F);
        this.setCustomNameVisible(true);
//        this.goalSelector.a(0, new PathfinderGoalFloat(this));
//        this.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(this, 1.0D));
//        this.goalSelector.a(6, new PathfinderGoalMoveThroughVillage(this, 1.0D, false));
//        this.goalSelector.a(7, new PathfinderGoalRandomStroll(this, 1.0D));
//        this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
//        this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
//        this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, true));
//        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<>(this, EntityZombie.class, true));
//        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<>(this, EntityCow.class, true));
        AttributeInstance speed = this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED);
        AttributeInstance damage = this.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE);
        speed.setValue(speed.getValue() * 3);
        damage.setValue(damage.getValue() * 2.5);

        this.goalSelector.a(0, new PathfinderGoalFloat(this));
        this.goalSelector.a(2, new PathfinderGoalMoveTowardsRestriction(this, 1.0D));
        this.goalSelector.a(2, new PathfinderGoalMoveThroughVillage(this, 1.0D, false));
        this.goalSelector.a(4, new PathfinderGoalRandomStroll(this, 1.0D));
        this.goalSelector.a(3, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
        this.goalSelector.a(4, new PathfinderGoalRandomLookaround(this));
        this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, true));
        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<>(this, EntityZombie.class, true));
        this.targetSelector.a(0, new PathfinderGoalNearestAttackableTarget<>(this, EntityHuman.class, false));
        this.targetSelector.a(1, new PathfinderGoalNearestAttackableTarget<>(this, EntityCow.class, true));

//        this.goalSelector.a(0, new PathfinderGoalFloat(this));
//        this.goalSelector.a(0, new PathfinderGoalMoveTowardsRestriction(this, 1.0D));
//        this.goalSelector.a(0, new PathfinderGoalMoveThroughVillage(this, 1.0D, false));
//        this.goalSelector.a(0, new PathfinderGoalRandomStroll(this, 1.0D));
//        this.goalSelector.a(0, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
//        this.goalSelector.a(0, new PathfinderGoalRandomLookaround(this));
//        this.targetSelector.a(0, new PathfinderGoalHurtByTarget(this, true));
//        this.targetSelector.a(0, new PathfinderGoalNearestAttackableTarget<>(this, EntityZombie.class, true));
//        this.targetSelector.a(0, new PathfinderGoalNearestAttackableTarget<>(this, EntityCow.class, true));
        runners.add(this);
    }
    public static List<Runner> getRunners(){
        return runners;
    }
    public static void killAll(){
        for (Runner r : runners){
            r.killEntity();
        }
    }

}

