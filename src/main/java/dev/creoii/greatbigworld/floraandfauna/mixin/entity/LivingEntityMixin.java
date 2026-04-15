package dev.creoii.greatbigworld.floraandfauna.mixin.entity;

import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.block.HollowLogBlock;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    @Shadow private Optional<BlockPos> lastClimbablePos;
    @Shadow public abstract EntityDimensions getDimensions(Pose pose);

    public LivingEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Inject(method = "onClimbable", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/tags/TagKey;)Z"), cancellable = true)
    private void gbw$livingClimbHollowLogs(CallbackInfoReturnable<Boolean> cir, @Local BlockPos blockPos) {
        MutableBoolean canClimbHollowLog = new MutableBoolean(true);
        BlockPos.betweenClosedStream(getDimensions(Pose.STANDING).makeBoundingBox(blockPos.getCenter())).forEach(pos -> {
            BlockState state = level().getBlockState(pos);
            if (!state.is(FloraAndFaunaTags.HOLLOW_LOGS) || state.getValue(HollowLogBlock.AXIS) != Direction.Axis.Y) {
                canClimbHollowLog.setFalse();
            }
        });

        if (canClimbHollowLog.booleanValue()) {
            lastClimbablePos = Optional.of(blockPos);
            cir.setReturnValue(true);
        }
    }
}
