package com.zyxkon.judgmentday;

import com.zyxkon.judgmentday.Main;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;

public class MadCow extends EntityCow {

    public MadCow(World world) {
        super(((CraftWorld) world).getHandle());
        this.setCustomName(ChatColor.translateAlternateColorCodes('&', "&l&n&cMad Cow&r"));
        this.setHealth(this.getHealth()*10);
        AttributeInstance speed = this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED);
        speed.setValue(speed.getValue() * 2);
        this.goalSelector.a(0, new PathfinderGoalFloat(this));
        this.goalSelector.a(0, new PathfinderGoalMeleeAttack(this, 1.0D, true));
        this.goalSelector.a(2, new PathfinderGoalMoveTowardsRestriction(this, 1.0D));
        this.goalSelector.a(2, new PathfinderGoalMoveThroughVillage(this, 1.0D, false));
        this.goalSelector.a(4, new PathfinderGoalRandomStroll(this, 1.0D));
        this.goalSelector.a(3, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
        this.goalSelector.a(4, new PathfinderGoalRandomLookaround(this));
        this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, true));
        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<>(this, EntityHuman.class, true));
        this.targetSelector.a(1, new PathfinderGoalNearestAttackableTarget<>(this, EntityZombieHusk.class, true));


    }
    protected void initAttributes() {
        super.initAttributes();
        this.getAttributeMap().b(GenericAttributes.ATTACK_DAMAGE).setValue(10);
        this.getAttributeInstance(GenericAttributes.maxHealth).setValue(20.0D);
        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.20000000298023224D);
    }

}
