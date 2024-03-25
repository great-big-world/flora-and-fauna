package dev.creoii.greatbigworld.floraandfauna.mixin.entity;

import dev.creoii.greatbigworld.floraandfauna.block.HollowLogBlock;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaTags;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow public abstract World getWorld();
    @Shadow public abstract BlockPos getBlockPos();
    @Shadow public abstract float getWidth();
    @Shadow public abstract float getHeight();
    @Shadow public abstract boolean isSprinting();
    @Shadow public abstract boolean hasPassengers();
    @Shadow public abstract void setSwimming(boolean swimming);
    @Shadow public abstract void setPose(EntityPose pose);
    @Shadow public abstract EntityType<?> getType();
    @Shadow public abstract Direction getHorizontalFacing();
    @Shadow public abstract boolean isInPose(EntityPose pose);

    @Inject(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;pop()V", ordinal = 2))
    private void gbw$crawlIntoHollowLogs(MovementType movementType, Vec3d movement, CallbackInfo ci) {
        if (isInPose(EntityPose.SWIMMING) || !isSprinting() || hasPassengers())
            return;
        Optional<BlockPos> optionalPos = BlockPos.findClosest(getBlockPos(), (int) (getWidth() + .5f), (int) (getHeight() + .5f), pos -> {
            BlockPos difference = pos.subtract(getBlockPos());
            if (difference.getY() > .5d || difference.getY() < -.5d || difference.equals(BlockPos.ORIGIN))
                return false;
            BlockState state = getWorld().getBlockState(pos);
            if (state.isIn(FloraAndFaunaTags.HOLLOW_LOGS)) {
                Vec3i facingVec = getHorizontalFacing().getVector();
                return switch (state.get(HollowLogBlock.AXIS)) {
                    case X -> difference.getX() != 0d && difference.getX() == facingVec.getX() && difference.getZ() == 0d;
                    case Z -> difference.getZ() != 0d && difference.getZ() == facingVec.getZ() && difference.getX() == 0d;
                    case Y -> false;
                };
            }
            return false;
        });

        if (optionalPos.isPresent()) {
            setSwimming(true);
            setPose(EntityPose.SWIMMING);
        }
    }
}
