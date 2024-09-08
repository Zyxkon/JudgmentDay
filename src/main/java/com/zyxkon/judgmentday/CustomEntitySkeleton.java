package com.zyxkon.judgmentday;

import com.zyxkon.judgmentday.Main;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.event.entity.EntityCombustEvent;

public class CustomEntitySkeleton extends EntitySkeleton {

    public CustomEntitySkeleton(World world) {
        super(((CraftWorld) world).getHandle());
        this.setCustomName(ChatColor.translateAlternateColorCodes('&', "&l&n&aSkeletor&r"));
    }

    @Override
    public void a(EntityLiving entityliving, float f) {
        for (int i = 0; i < 2; ++i) {
            super.a(entityliving, f);
        }
    }
//    protected void a(DifficultyDamageScaler difficultydamagescaler) {
//        super.a(difficultydamagescaler);
//        this.setSlot(EnumItemSlot.MAINHAND, new ItemStack(Items.BOW));
//        this.setSlot(EnumItemSlot.OFFHAND, new ItemStack(Items.SHIELD));
//    }
    @Override
    public void n() {
        if (this.world.D() && !this.world.isClientSide) {
            float f = this.aw();
            BlockPosition blockposition = this.bJ() instanceof EntityBoat ? (new BlockPosition(this.locX, (double) Math.round(this.locY), this.locZ)).up() : new BlockPosition(this.locX, (double) Math.round(this.locY), this.locZ);
            if (f > 0.5F && this.random.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && this.world.h(blockposition)) {
                boolean flag = true;
                ItemStack itemstack = this.getEquipment(EnumItemSlot.HEAD);
                if (!itemstack.isEmpty()) {
                    if (itemstack.f()) {
                        itemstack.setData(itemstack.i() + this.random.nextInt(2));
                        if (itemstack.i() >= itemstack.k()) {
                            this.b(itemstack);
                            this.setSlot(EnumItemSlot.HEAD, ItemStack.a);
                        }
                    }

                    flag = false;
                }

                if (flag) {
                    EntityCombustEvent event = new EntityCombustEvent(this.getBukkitEntity(), 8);
                    this.world.getServer().getPluginManager().callEvent(event);
                    if (!event.isCancelled()) {
                        this.setOnFire(event.getDuration());
                    }
                }
            }
        }
//        super.n();
    }
}