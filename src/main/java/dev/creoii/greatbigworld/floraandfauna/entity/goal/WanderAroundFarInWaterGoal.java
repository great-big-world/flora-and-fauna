package dev.creoii.greatbigworld.floraandfauna.entity.goal;

import dev.creoii.greatbigworld.floraandfauna.util.FuzzyTargetingHelper;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public class WanderAroundFarInWaterGoal extends WanderAroundGoal {
    protected final float probability;

    public WanderAroundFarInWaterGoal(PathAwareEntity mob, double speed, float probability) {
        super(mob, speed);
        this.probability = probability;
    }

    @Nullable
    protected Vec3d getWanderTarget() {
        if (mob.isInsideWaterOrBubbleColumn()) {
            Vec3d vec3d = FuzzyTargetingHelper.findIgnoreWater(mob, 15, 7);
            return vec3d == null ? super.getWanderTarget() : vec3d;
        }
        return mob.getRandom().nextFloat() >= probability ? FuzzyTargetingHelper.findIgnoreWater(mob, 10, 7) : super.getWanderTarget();
    }
}
