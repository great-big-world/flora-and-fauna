package dev.creoii.greatbigworld.floraandfauna.mixin.block;

import dev.creoii.greatbigworld.floraandfauna.season.Season;
import dev.creoii.greatbigworld.floraandfauna.season.SeasonManager;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(BeehiveBlockEntity.class)
public class BeehiveBlockEntityMixin {
    @Inject(method = "releaseOccupant", at = @At("HEAD"), cancellable = true)
    private static void gbw$dontReleaseBeeDuringWinter(Level world, BlockPos pos, BlockState state, BeehiveBlockEntity.Occupant bee, List<Entity> entities, BeehiveBlockEntity.BeeReleaseStatus beeState, BlockPos flowerPos, CallbackInfoReturnable<Boolean> cir) {
        if (!world.isClientSide()) {
            if (world.dimensionTypeRegistration().is(FloraAndFaunaTags.AFFECTED_BY_SEASONS)) {
                SeasonManager manager = SeasonManager.getInstance(((ServerLevel) world).getServer());
                if (manager != null && manager.getCurrentSeason() == Season.WINTER && beeState != BeehiveBlockEntity.BeeReleaseStatus.EMERGENCY)
                    cir.setReturnValue(false);
            }
        }
    }
}
