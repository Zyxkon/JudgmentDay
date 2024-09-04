package com.zyxkon.judgmentday.mobs.zombies;

import net.minecraft.server.v1_12_R1.*;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;

import net.minecraft.server.v1_12_R1.EntityZombie;

public class Runner extends EntityZombie {
    public Runner(org.bukkit.World world) {
        super( ((CraftWorld) world).getHandle());
        this.setCustomName("Runner");
        this.setCustomNameVisible(true);
        this.goalSelector.a(0, new PathfinderGoalFloat(this));
        this.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(this, 1.0D));
        this.goalSelector.a(6, new PathfinderGoalMoveThroughVillage(this, 1.0D, false));
        this.goalSelector.a(7, new PathfinderGoalRandomStroll(this, 1.0D));
        this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
        this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
        this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, true));
        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<>(this, EntityZombie.class, true));
        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<>(this, EntityCow.class, true));
//        this.goalSelector.a(0, new PathfinderGoalFloat(this));
//        this.goalSelector.a(0, new PathfinderGoalMoveTowardsRestriction(this, 1.0D));
//        this.goalSelector.a(0, new PathfinderGoalMoveThroughVillage(this, 1.0D, false));
//        this.goalSelector.a(0, new PathfinderGoalRandomStroll(this, 1.0D));
//        this.goalSelector.a(0, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
//        this.goalSelector.a(0, new PathfinderGoalRandomLookaround(this));
//        this.targetSelector.a(0, new PathfinderGoalHurtByTarget(this, true));
//        this.targetSelector.a(0, new PathfinderGoalNearestAttackableTarget<>(this, EntityZombie.class, true));
//        this.targetSelector.a(0, new PathfinderGoalNearestAttackableTarget<>(this, EntityCow.class, true));
    }
}

