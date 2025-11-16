package dev.creoii.greatbigworld.floraandfauna.mixin.entity;

import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.floraandfauna.block.HollowLogBlock;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaTags;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    @Shadow private Optional<BlockPos> climbingPos;
    @Shadow public abstract EntityDimensions getDimensions(EntityPose pose);

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "isClimbing", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isIn(Lnet/minecraft/registry/tag/TagKey;)Z"), cancellable = true)
    private void gbw$livingClimbHollowLogs(CallbackInfoReturnable<Boolean> cir, @Local BlockPos blockPos, @Local BlockState blockState) {
        MutableBoolean canClimbHollowLog = new MutableBoolean(true);
        BlockPos.stream(getDimensions(EntityPose.STANDING).getBoxAt(blockPos.toCenterPos())).forEach(pos -> {
            BlockState state = getEntityWorld().getBlockState(pos);
            if (!state.isIn(FloraAndFaunaTags.HOLLOW_LOGS) || state.get(HollowLogBlock.AXIS) != Direction.Axis.Y) {
                canClimbHollowLog.setFalse();
            }
        });

        if (canClimbHollowLog.booleanValue()) {
            climbingPos = Optional.of(blockPos);
            cir.setReturnValue(true);
        }
    }
}
