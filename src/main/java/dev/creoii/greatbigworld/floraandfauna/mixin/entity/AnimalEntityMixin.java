package dev.creoii.greatbigworld.floraandfauna.mixin.entity;

import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaTags;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AnimalEntity.class)
public abstract class AnimalEntityMixin extends PassiveEntity {
    protected AnimalEntityMixin(EntityType<? extends PassiveEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "applyDamage", at = @At("HEAD"))
    private void gbw$betterAnimalThreatResponse(DamageSource source, float amount, CallbackInfo ci) {
        if (source.isIn(FloraAndFaunaTags.ALERTS_ANIMALS) && source.getAttacker() instanceof LivingEntity attacker) {
            getWorld().getOtherEntities(null, getBoundingBox().expand(12d), entity -> entity instanceof AnimalEntity).forEach(entity -> {                                                   // enchantment for this value
                if (entity instanceof LivingEntity living && (living.squaredDistanceTo(attacker) < 16d || (attacker.getRotationVector().normalize().dotProduct(living.getRotationVector().normalize()) < -.333334d && living.canSee(attacker)))) {
                    living.setAttacker(attacker);
                }
            });
        }
    }
}
