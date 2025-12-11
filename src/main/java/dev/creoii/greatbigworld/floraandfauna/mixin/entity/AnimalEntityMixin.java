package dev.creoii.greatbigworld.floraandfauna.mixin.entity;

import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Animal.class)
public abstract class AnimalEntityMixin extends AgeableMob {
    protected AnimalEntityMixin(EntityType<? extends AgeableMob> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(method = "actuallyHurt", at = @At("HEAD"))
    private void gbw$betterAnimalThreatResponse(ServerLevel world, DamageSource source, float amount, CallbackInfo ci) {
        if (source.is(FloraAndFaunaTags.ALERTS_ANIMALS) && source.getEntity() instanceof LivingEntity attacker) {
            level().getEntities((Entity) null, getBoundingBox().inflate(12d), entity -> entity instanceof Animal).forEach(entity -> {                                                   // enchantment for this value
                if (entity instanceof LivingEntity living && (living.distanceToSqr(attacker) < 16d || (attacker.getLookAngle().normalize().dot(living.getLookAngle().normalize()) < -.333334d && living.hasLineOfSight(attacker)))) {
                    living.setLastHurtByMob(attacker);
                }
            });
        }
    }
}
