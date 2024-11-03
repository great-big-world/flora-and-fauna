package dev.creoii.greatbigworld.floraandfauna.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.floraandfauna.client.FloraAndFaunaClient;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaTags;
import net.minecraft.block.BlockState;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.World;
import net.minecraft.world.biome.FoliageColors;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockColors.class)
public class BlockColorsMixin {
    @Inject(method = "getParticleColor", at = @At(value = "RETURN", ordinal = 0), cancellable = true)
    private void gbw$modifyParticleColor(BlockState state, World world, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        if (FloraAndFaunaClient.getCurrentSeason() != null && FloraAndFaunaClient.getTransitionContext() != null && !state.isIn(FloraAndFaunaTags.IGNORE_SEASON_COLOR)) {
            cir.setReturnValue(FloraAndFaunaClient.getTransitionContext().getSeasonGrassColor(world, pos, cir.getReturnValue()));
        }
    }

    @Redirect(method = "method_1695", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/FoliageColors;getSpruceColor()I"))
    private static int gbw$modifySpruceColor(@Local(argsOnly = true) BlockState state, @Local(argsOnly = true) BlockRenderView world, @Local(argsOnly = true) BlockPos pos) {
        if (FloraAndFaunaClient.getCurrentSeason() != null && FloraAndFaunaClient.getTransitionContext() != null && !state.isIn(FloraAndFaunaTags.IGNORE_SEASON_COLOR)) {
            return FloraAndFaunaClient.getTransitionContext().getSeasonGrassColor(world, pos, FoliageColors.getSpruceColor());
        }
        return FoliageColors.getSpruceColor();
    }

    @Redirect(method = "method_1687", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/FoliageColors;getBirchColor()I"))
    private static int gbw$modifyBirchColor(@Local(argsOnly = true) BlockState state, @Local(argsOnly = true) BlockRenderView world, @Local(argsOnly = true) BlockPos pos) {
        if (FloraAndFaunaClient.getCurrentSeason() != null && FloraAndFaunaClient.getTransitionContext() != null && !state.isIn(FloraAndFaunaTags.IGNORE_SEASON_COLOR)) {
            return FloraAndFaunaClient.getTransitionContext().getSeasonGrassColor(world, pos, FoliageColors.getBirchColor());
        }
        return FoliageColors.getBirchColor();
    }
}
