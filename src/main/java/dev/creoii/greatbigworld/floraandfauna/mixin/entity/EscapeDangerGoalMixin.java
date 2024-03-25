package dev.creoii.greatbigworld.floraandfauna.mixin.entity;

import net.minecraft.entity.ai.NoPenaltyTargeting;
import net.minecraft.entity.ai.goal.EscapeDangerGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EscapeDangerGoal.class)
public class EscapeDangerGoalMixin {
    @Redirect(method = "findTarget", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/NoPenaltyTargeting;find(Lnet/minecraft/entity/mob/PathAwareEntity;II)Lnet/minecraft/util/math/Vec3d;"))
    private Vec3d gbw$improveEscapeDangerPos(PathAwareEntity entity, int horizontalRange, int verticalRange) {
        Vec3d vec3d = NoPenaltyTargeting.find(entity, horizontalRange, verticalRange);
        if (vec3d != null && entity.getAttacker() != null) {
            Vec3d distance = entity.getPos().subtract(entity.getAttacker().getPos());
            Vec3d direction = distance.normalize().multiply(distance.lengthSquared() / 2d);
            return vec3d.add(direction);
        }
        return vec3d;
    }
}
