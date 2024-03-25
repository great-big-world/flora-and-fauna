package dev.creoii.greatbigworld.floraandfauna.util;

import net.minecraft.entity.ai.FuzzyPositions;
import net.minecraft.entity.ai.FuzzyTargeting;
import net.minecraft.entity.ai.NavigationConditions;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.ToDoubleFunction;

public final class FuzzyTargetingHelper {
    @Nullable
    public static Vec3d findIgnoreWater(PathAwareEntity entity, int horizontalRange, int verticalRange) {
        Objects.requireNonNull(entity);
        return findIgnoreWater(entity, horizontalRange, verticalRange, entity::getPathfindingFavor);
    }

    @Nullable
    public static Vec3d findIgnoreWater(PathAwareEntity entity, int horizontalRange, int verticalRange, ToDoubleFunction<BlockPos> scorer) {
        boolean bl = NavigationConditions.isPositionTargetInRange(entity, horizontalRange);
        return FuzzyPositions.guessBest(() -> {
            BlockPos blockPos = FuzzyPositions.localFuzz(entity.getRandom(), horizontalRange, verticalRange);
            BlockPos blockPos2 = FuzzyTargeting.towardTarget(entity, horizontalRange, bl, blockPos);
            return blockPos2 == null ? null : validateIgnoreWater(entity, blockPos2);
        }, scorer);
    }

    @Nullable
    public static BlockPos validateIgnoreWater(PathAwareEntity entity, BlockPos pos) {
        pos = FuzzyPositions.upWhile(pos, entity.getWorld().getTopY(), currentPos -> NavigationConditions.isSolidAt(entity, currentPos) || NavigationConditions.isWaterAt(entity, currentPos));
        return !NavigationConditions.hasPathfindingPenalty(entity, pos) ? pos : null;
    }
}
