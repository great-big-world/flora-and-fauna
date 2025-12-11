package dev.creoii.greatbigworld.floraandfauna.mixin.client;

import dev.creoii.greatbigworld.floraandfauna.client.FloraAndFaunaClient;
import dev.creoii.greatbigworld.floraandfauna.season.Season;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaTags;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BiomeColors.class)
public class BiomeColorsMixin {
    @Inject(method = "getAverageFoliageColor", at = @At("RETURN"), cancellable = true)
    private static void gbw$modifyBiomeFoliageColor(BlockAndTintGetter world, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        if (FloraAndFaunaClient.getCurrentSeason() != null && !world.getBlockState(pos).is(FloraAndFaunaTags.IGNORE_SEASON_COLOR)) {
            if (FloraAndFaunaClient.getTransitionContext() != null) {
                cir.setReturnValue(FloraAndFaunaClient.getTransitionContext().getSeasonFoliageColor(world, pos, cir.getReturnValue()));
            } else {
                cir.setReturnValue(FloraAndFaunaClient.getCurrentSeason().getFoliageColorChange().apply(new Season.Context(world, pos, cir.getReturnValue())));
            }
        }
    }

    @Inject(method = "getAverageGrassColor", at = @At("RETURN"), cancellable = true)
    private static void gbw$modifyBiomeGrassColor(BlockAndTintGetter world, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        if (FloraAndFaunaClient.getCurrentSeason() != null && !world.getBlockState(pos).is(FloraAndFaunaTags.IGNORE_SEASON_COLOR)) {
            if (FloraAndFaunaClient.getTransitionContext() != null) {
                cir.setReturnValue(FloraAndFaunaClient.getTransitionContext().getSeasonGrassColor(world, pos, cir.getReturnValue()));
            } else {
                cir.setReturnValue(FloraAndFaunaClient.getCurrentSeason().getGrassColorChange().apply(new Season.Context(world, pos, cir.getReturnValue())));
            }
        }
    }
}
