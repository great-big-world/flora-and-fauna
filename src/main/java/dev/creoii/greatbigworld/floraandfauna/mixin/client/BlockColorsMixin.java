package dev.creoii.greatbigworld.floraandfauna.mixin.client;

import dev.creoii.greatbigworld.floraandfauna.client.FloraAndFaunaClient;
import dev.creoii.greatbigworld.floraandfauna.season.Season;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaTags;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * method_1684 -> Lily Pad
 * method_1685 -> Sugar Cane
 * method_1686 -> Large Fern/Tall Grass
 * method_1687 -> Birch Leaves
 * method_1688 -> Redstone Wire
 * method_1692 -> Regular Leaves
 * method_1693 -> Short Grass/Fern
 * method_1695 -> Spruce Leaves
 * method_1696 -> Pumpkin/Melon Stem
 */
@Mixin(BlockColors.class)
public class BlockColorsMixin {
    @Inject(method = "getColor(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)I", at = @At(value = "RETURN", ordinal = 0), cancellable = true)
    private void gbw$modifyParticleColor(BlockState state, Level world, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        if (FloraAndFaunaClient.getCurrentSeason() != null && !state.is(FloraAndFaunaTags.IGNORE_SEASON_COLOR) && world.dimensionTypeRegistration().is(FloraAndFaunaTags.AFFECTED_BY_SEASONS)) {
            if (FloraAndFaunaClient.getTransitionContext() != null) {
                cir.setReturnValue(FloraAndFaunaClient.getTransitionContext().getSeasonGrassColor(world, pos, cir.getReturnValue()));
            } else cir.setReturnValue(FloraAndFaunaClient.getCurrentSeason().getParticleColorChange().apply(new Season.Context(world, pos, cir.getReturnValue())));
        }
    }

    @Inject(method = "getColor(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;I)I", at = @At(value = "RETURN"), cancellable = true)
    private void gbw$modifyParticleColor(BlockState state, BlockAndTintGetter world, BlockPos pos, int i, CallbackInfoReturnable<Integer> cir) {
        if (FloraAndFaunaClient.getCurrentSeason() != null && world instanceof Level level && !state.is(FloraAndFaunaTags.IGNORE_SEASON_COLOR) && level.dimensionTypeRegistration().is(FloraAndFaunaTags.AFFECTED_BY_SEASONS)) {
            if (FloraAndFaunaClient.getTransitionContext() != null) {
                cir.setReturnValue(FloraAndFaunaClient.getTransitionContext().getSeasonGrassColor(level, pos, cir.getReturnValue()));
            } else cir.setReturnValue(FloraAndFaunaClient.getCurrentSeason().getParticleColorChange().apply(new Season.Context(level, pos, cir.getReturnValue())));
        }
    }

    @Inject(method = "method_1693", at = @At("HEAD"), cancellable = true)
    private static void gbw$modifySmallPlantColor(BlockState state, BlockAndTintGetter world, BlockPos pos, int i, CallbackInfoReturnable<Integer> cir) {
        int baseColor = world != null && pos != null ? BiomeColors.getAverageGrassColor(world, pos) : GrassColor.getDefaultColor();
        if (FloraAndFaunaClient.getCurrentSeason() != null && !state.is(FloraAndFaunaTags.IGNORE_SEASON_COLOR) && world instanceof ClientLevel clientWorld && clientWorld.dimensionTypeRegistration().is(FloraAndFaunaTags.AFFECTED_BY_SEASONS)) {
            if (FloraAndFaunaClient.getTransitionContext() != null) {
                cir.setReturnValue(FloraAndFaunaClient.getTransitionContext().getSeasonGrassColor(world, pos, baseColor));
            } else cir.setReturnValue(FloraAndFaunaClient.getCurrentSeason().getGrassColorChange().apply(new Season.Context(world, pos, baseColor)));
            return;
        }
        cir.setReturnValue(baseColor);
    }

    @Inject(method = "method_1695", at = @At("HEAD"), cancellable = true)
    private static void gbw$modifySpruceColor(BlockState state, BlockAndTintGetter world, BlockPos pos, int i, CallbackInfoReturnable<Integer> cir) {
        if (world instanceof ClientLevel clientWorld && !clientWorld.dimensionTypeRegistration().is(FloraAndFaunaTags.AFFECTED_BY_SEASONS)) {
            cir.setReturnValue(-10380959);
            return;
        }
        if (FloraAndFaunaClient.getCurrentSeason() != null && !state.is(FloraAndFaunaTags.IGNORE_SEASON_COLOR)) {
            if (FloraAndFaunaClient.getTransitionContext() != null) {
                cir.setReturnValue(FloraAndFaunaClient.getTransitionContext().getSeasonFoliageColor(world, pos, -10380959));
            } else cir.setReturnValue(FloraAndFaunaClient.getCurrentSeason().getFoliageColorChange().apply(new Season.Context(world, pos, -10380959)));
        }
    }

    @Inject(method = "method_1687", at = @At("HEAD"), cancellable = true)
    private static void gbw$modifyBirchColor(BlockState state, BlockAndTintGetter world, BlockPos pos, int i, CallbackInfoReturnable<Integer> cir) {
        if (world instanceof ClientLevel clientWorld && !clientWorld.dimensionTypeRegistration().is(FloraAndFaunaTags.AFFECTED_BY_SEASONS)) {
            cir.setReturnValue(-8345771);
            return;
        }
        if (FloraAndFaunaClient.getCurrentSeason() != null && !state.is(FloraAndFaunaTags.IGNORE_SEASON_COLOR)) {
            if (FloraAndFaunaClient.getTransitionContext() != null) {
                cir.setReturnValue(FloraAndFaunaClient.getTransitionContext().getSeasonFoliageColor(world, pos, -8345771));
            } else cir.setReturnValue(FloraAndFaunaClient.getCurrentSeason().getFoliageColorChange().apply(new Season.Context(world, pos, -8345771)));
        }
    }
}
