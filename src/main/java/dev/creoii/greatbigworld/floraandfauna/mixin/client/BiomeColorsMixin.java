package dev.creoii.greatbigworld.floraandfauna.mixin.client;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.floraandfauna.client.FloraAndFaunaClient;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaTags;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BiomeColors.class)
public class BiomeColorsMixin {
    @Inject(method = "getFoliageColor", at = @At("RETURN"), cancellable = true)
    private static void gbw$modifyBiomeFoliageColor(BlockRenderView world, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        if (world instanceof ClientWorld clientWorld && clientWorld.getRegistryKey() == GreatBigWorld.ALTERWORLD_KEY) {
            if (FloraAndFaunaClient.getCurrentSeason() != null && FloraAndFaunaClient.getTransitionContext() != null && !world.getBlockState(pos).isIn(FloraAndFaunaTags.IGNORE_SEASON_COLOR)) {
                cir.setReturnValue(FloraAndFaunaClient.getTransitionContext().getSeasonFoliageColor(world, pos, cir.getReturnValue()));
            }
        }
    }

    @Inject(method = "getGrassColor", at = @At("RETURN"), cancellable = true)
    private static void gbw$modifyBiomeGrassColor(BlockRenderView world, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        if (world instanceof ClientWorld clientWorld && clientWorld.getRegistryKey() == GreatBigWorld.ALTERWORLD_KEY) {
            if (FloraAndFaunaClient.getCurrentSeason() != null && FloraAndFaunaClient.getTransitionContext() != null && !world.getBlockState(pos).isIn(FloraAndFaunaTags.IGNORE_SEASON_COLOR)) {
                cir.setReturnValue(FloraAndFaunaClient.getTransitionContext().getSeasonGrassColor(world, pos, cir.getReturnValue()));
            }
        }
    }
}
