package dev.creoii.greatbigworld.floraandfauna.mixin.entity;

import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.floraandfauna.block.HollowLogBlock;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaTags;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Shadow private Optional<BlockPos> climbingPos;

    @Inject(method = "isClimbing", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isIn(Lnet/minecraft/registry/tag/TagKey;)Z"), cancellable = true)
    private void gbw$livingClimbHollowLogs(CallbackInfoReturnable<Boolean> cir, @Local BlockPos blockPos, @Local BlockState blockState) {
        if (blockState.isIn(FloraAndFaunaTags.HOLLOW_LOGS) && blockState.get(HollowLogBlock.AXIS) == Direction.Axis.Y) {
            climbingPos = Optional.of(blockPos);
            cir.setReturnValue(true);
        }
    }
}
