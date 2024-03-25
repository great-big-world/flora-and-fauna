package dev.creoii.greatbigworld.floraandfauna.mixin.block;

import dev.creoii.greatbigworld.floraandfauna.season.Season;
import dev.creoii.greatbigworld.floraandfauna.season.SeasonManager;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(BeehiveBlockEntity.class)
public class BeehiveBlockEntityMixin {
    @Inject(method = "releaseBee", at = @At("HEAD"), cancellable = true)
    private static void gbw$dontReleaseBeeDuringWinter(World world, BlockPos pos, BlockState state, BeehiveBlockEntity.Bee bee, @Nullable List<Entity> entities, BeehiveBlockEntity.BeeState beeState, @Nullable BlockPos flowerPos, CallbackInfoReturnable<Boolean> cir) {
        if (!world.isClient) {
            if (SeasonManager.getInstance(((ServerWorld) world).getServer()).getCurrentSeason() == Season.WINTER && beeState != BeehiveBlockEntity.BeeState.EMERGENCY)
                cir.setReturnValue(false);
        }
    }
}
