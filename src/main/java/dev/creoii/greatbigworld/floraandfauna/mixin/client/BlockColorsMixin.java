package dev.creoii.greatbigworld.floraandfauna.mixin.client;

import dev.creoii.greatbigworld.floraandfauna.client.FloraAndFaunaClient;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaTags;
import net.minecraft.block.BlockState;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockColors.class)
public class BlockColorsMixin {
    @Inject(method = "getColor", at = @At("RETURN"), cancellable = true)
    private void gbw$modifyBlockColor(BlockState state, BlockRenderView world, BlockPos pos, int tintIndex, CallbackInfoReturnable<Integer> cir) {
        if (FloraAndFaunaClient.getCurrentSeason() != null && !state.isIn(FloraAndFaunaTags.IGNORE_SEASON_COLOR)) {
            cir.setReturnValue(FloraAndFaunaClient.getSeasonColor(world, pos, cir.getReturnValue()));
        }
    }

    @Inject(method = "getParticleColor", at = @At(value = "RETURN", ordinal = 0), cancellable = true)
    private void gbw$modifyParticleColor(BlockState state, World world, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        if (FloraAndFaunaClient.getCurrentSeason() != null && !state.isIn(FloraAndFaunaTags.IGNORE_SEASON_COLOR)) {
            cir.setReturnValue(FloraAndFaunaClient.getSeasonColor(world, pos, cir.getReturnValue()));
        }
    }
}
