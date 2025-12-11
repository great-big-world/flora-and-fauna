package dev.creoii.greatbigworld.floraandfauna.mixin.entity;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PanicGoal.class)
public class EscapeDangerGoalMixin {
    @Redirect(method = "findRandomPosition", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/util/DefaultRandomPos;getPos(Lnet/minecraft/world/entity/PathfinderMob;II)Lnet/minecraft/world/phys/Vec3;"))
    private Vec3 gbw$improveEscapeDangerPos(PathfinderMob entity, int horizontalRange, int verticalRange) {
        Vec3 vec3d = DefaultRandomPos.getPos(entity, horizontalRange, verticalRange);
        if (vec3d != null && entity.getLastHurtByMob() != null) {
            Vec3 distance = entity.position().subtract(entity.getLastHurtByMob().position());
            Vec3 direction = distance.normalize().scale(distance.lengthSqr() / 2d);
            return vec3d.add(direction);
        }
        return vec3d;
    }
}
