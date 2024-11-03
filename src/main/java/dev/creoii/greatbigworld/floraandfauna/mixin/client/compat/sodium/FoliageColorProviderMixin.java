package dev.creoii.greatbigworld.floraandfauna.mixin.client.compat.sodium;

import dev.creoii.greatbigworld.floraandfauna.client.FloraAndFaunaClient;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaTags;
import net.caffeinemc.mods.sodium.client.model.color.DefaultColorProviders;
import net.caffeinemc.mods.sodium.client.world.LevelSlice;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DefaultColorProviders.FoliageColorProvider.class)
public class FoliageColorProviderMixin {
    @Inject(method = "getColor", at = @At("RETURN"), cancellable = true, remap = false)
    private <T> void gbw$modifyBlockColor(LevelSlice slice, T state, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        if (FloraAndFaunaClient.getCurrentSeason() != null && FloraAndFaunaClient.getTransitionContext() != null && !slice.getBlockState(pos).isIn(FloraAndFaunaTags.IGNORE_SEASON_COLOR)) {
            cir.setReturnValue(FloraAndFaunaClient.getTransitionContext().getSeasonFoliageColor(slice, pos, cir.getReturnValue()));
        }
    }
}
