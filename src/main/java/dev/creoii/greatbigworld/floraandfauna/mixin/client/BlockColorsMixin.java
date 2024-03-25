package dev.creoii.greatbigworld.floraandfauna.mixin.client;

import dev.creoii.greatbigworld.floraandfauna.client.FloraAndFaunaClient;
import dev.creoii.greatbigworld.floraandfauna.season.Season;
import dev.creoii.greatbigworld.floraandfauna.season.SeasonManager;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaTags;
import net.minecraft.block.BlockState;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockColors.class)
public class BlockColorsMixin {
    @Inject(method = "getColor", at = @At("RETURN"), cancellable = true)
    private void gbw$modifyBlockColor(BlockState state, BlockRenderView world, BlockPos pos, int tintIndex, CallbackInfoReturnable<Integer> cir) {
        if (!state.isIn(FloraAndFaunaTags.IGNORE_SEASON_COLOR)) {
            Season season = FloraAndFaunaClient.getCurrentSeason();
            if (season != null) {
                cir.setReturnValue(SeasonManager.getColor(world, pos, cir.getReturnValue()));
            }
        }
    }

    @Inject(method = "getColor", at = @At(value = "RETURN", ordinal = 0), cancellable = true)
    private void gbw$modifyParticleColor(BlockState state, BlockRenderView world, BlockPos pos, int tintIndex, CallbackInfoReturnable<Integer> cir) {
        if (!state.isIn(FloraAndFaunaTags.IGNORE_SEASON_COLOR)) {
            Season season = FloraAndFaunaClient.getCurrentSeason();
            if (season != null) {
                cir.setReturnValue(SeasonManager.getColor(world, pos, cir.getReturnValue()));
            }
        }
    }
}
