package com.zyxkon.judgmentday.zombies;

import com.zyxkon.judgmentday.Utils;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Color;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.util.UnsafeList;
import org.bukkit.entity.EntityType;

import java.util.List;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import net.minecraft.server.v1_12_R1.BiomeBase;
import net.minecraft.server.v1_12_R1.BiomeMesa;
import net.minecraft.server.v1_12_R1.EntityInsentient;
import net.minecraft.server.v1_12_R1.EntityTypes;
import net.minecraft.server.v1_12_R1.EntityZombie;
import net.minecraft.server.v1_12_R1.EntitySkeleton;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Runner extends EntityZombie {


    public Runner(org.bukkit.World world) {
        super( ((CraftWorld) world).getHandle());
//        try {
//            Field bField = PathfinderGoalSelector.class.getDeclaredField("b");
//            bField.setAccessible(true);
//            Field cField = PathfinderGoalSelector.class.getDeclaredField("c");
//            cField.setAccessible(true);
//            bField.set(goalSelector, new UnsafeList<PathfinderGoalSelector>());
//            bField.set(targetSelector, new UnsafeList<PathfinderGoalSelector>());
//            cField.set(goalSelector, new UnsafeList<PathfinderGoalSelector>());
//            cField.set(targetSelector, new UnsafeList<PathfinderGoalSelector>());
//        } catch (Exception exc) {
//            exc.printStackTrace();
//// This means that the name of one of the fields changed names or declaration and will have to be re-examined.
//        }
        this.setCustomName("Runner");
        System.out.println("Just spawned a runner.");
        this.goalSelector.a(0, new PathfinderGoalFloat(this));
        this.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(this, 1.0D));
        this.goalSelector.a(6, new PathfinderGoalMoveThroughVillage(this, 1.0D, false));
        this.goalSelector.a(7, new PathfinderGoalRandomStroll(this, 1.0D));
        this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
        this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
        this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, true));
        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<>(this, EntityCow.class,true));
        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<>(this, EntitySkeleton.class, false));
        ((LivingEntity) this.getBukkitEntity()).addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 99999, 32767, false, true, Color.RED));
    }

}

