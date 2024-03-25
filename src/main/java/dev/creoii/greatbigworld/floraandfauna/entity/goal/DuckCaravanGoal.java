package dev.creoii.greatbigworld.floraandfauna.entity.goal;

import dev.creoii.greatbigworld.floraandfauna.entity.DuckEntity;
import dev.creoii.greatbigworld.floraandfauna.entity.DuckLike;
import dev.creoii.greatbigworld.floraandfauna.registry.FloraAndFaunaCriteria;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;
import java.util.List;

public class DuckCaravanGoal extends Goal {
    public final MobEntity duck;
    private final int maxCaravanLength;
    private final double speed;
    private int counter;

    public DuckCaravanGoal(MobEntity duck, int maxCaravanLength, double speed) {
        this.duck = duck;
        this.maxCaravanLength = maxCaravanLength;
        this.speed = speed;
        setControls(EnumSet.of(Goal.Control.MOVE));
    }

    @Override
    public boolean canStart() {
        if (duck instanceof DuckLike duckLike) {
            if (duckLike.isFollowing() || !duck.isBaby()) {
                return false;
            }
            List<Entity> followTargets = duck.getWorld().getOtherEntities(duck, duck.getBoundingBox().expand(9d, 3d, 9d), entity -> {
                return entity instanceof DuckLike duckLike1 && !duckLike1.hasFollower();
            });
            LivingEntity living = null;
            for (Entity entity : followTargets) {
                if (entity instanceof DuckLike duckLike2) {
                    living = (LivingEntity) entity;
                    if ((!duckLike2.isFollowing() && !living.isBaby()) || (living.isBaby() && !duckLike2.hasFollower())) {
                        break;
                    }
                }
            }
            if (living == null)
                return false;
            duckLike.follow(living);
            if (!living.isBaby() && duck.getWorld() instanceof ServerWorld serverWorld && living instanceof ChickenEntity && duck instanceof DuckEntity) {
                ServerPlayerEntity serverPlayer = (ServerPlayerEntity) serverWorld.getClosestPlayer(duck, 16d);
                if (serverPlayer != null) {
                    FloraAndFaunaCriteria.DUCK_FOLLOW_CHICKEN.trigger(serverPlayer, conditions -> true);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean shouldContinue() {
        if (duck instanceof DuckLike duckLike && duckLike.isFollowing() && duckLike.gbw$getFollowing() != null && duckLike.gbw$getFollowing().isAlive()) {
            double d = duck.squaredDistanceTo(duckLike.gbw$getFollowing());
            if (d > 676d) {
                if (speed <= 3d) {
                    counter = toGoalTicks(40);
                    return true;
                }
                if (counter == 0) {
                    return false;
                }
            }
            if (counter > 0) {
                --counter;
            }
            return true;
        }
        return false;
    }

    @Override
    public void stop() {
        if (duck instanceof DuckLike duckLike)
            duckLike.stopFollowing();
    }

    @Override
    public void tick() {
        if (duck instanceof DuckLike duckLike) {
            if (!duckLike.isFollowing()) {
                return;
            }
            LivingEntity living = duckLike.gbw$getFollowing();
            double d = duck.distanceTo(living);
            Vec3d vec3d = new Vec3d(living.getX() - duck.getX(), living.getY() - duck.getY(), living.getZ() - duck.getZ()).normalize().multiply(Math.max(d - .2d, 0d));
            duck.getNavigation().startMovingTo(duck.getX() + vec3d.x, duck.getY() + vec3d.y, duck.getZ() + vec3d.z, speed);
        }
    }
}
