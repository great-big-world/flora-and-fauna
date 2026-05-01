package dev.creoii.greatbigworld.floraandfauna.mixin.client;

import dev.creoii.greatbigworld.floraandfauna.client.FloraAndFaunaClient;
import dev.creoii.greatbigworld.floraandfauna.mixin.client.compat.sodium.LevelSliceAccessor;
import dev.creoii.greatbigworld.floraandfauna.season.Season;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaTags;
import net.caffeinemc.mods.sodium.client.world.LevelSlice;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.chunk.RenderSectionRegion;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
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
    private void gbw$modifyParticleColor(BlockState state, Level level, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        if (!state.is(FloraAndFaunaTags.IGNORE_SEASON_COLOR)) {
            if (level != null) {
                if (level.dimensionTypeRegistration().is(BuiltinDimensionTypes.OVERWORLD)) {
                    cir.setReturnValue(Season.applyOverworldColorChange(cir.getReturnValue()));
                    return;
                } else if (!level.dimensionTypeRegistration().is(FloraAndFaunaTags.AFFECTED_BY_SEASONS)) {
                    cir.setReturnValue(cir.getReturnValue());
                    return;
                }
            }

            if (FloraAndFaunaClient.getCurrentSeason() != null) {
                if (FloraAndFaunaClient.getTransitionContext() != null) {
                    cir.setReturnValue(FloraAndFaunaClient.getTransitionContext().getSeasonGrassColor(level, pos, cir.getReturnValue()));
                } else
                    cir.setReturnValue(FloraAndFaunaClient.getCurrentSeason().getParticleColorChange().apply(new Season.Context(level, pos, cir.getReturnValue())));
            }
        }
    }

    @Inject(method = "getColor(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;I)I", at = @At(value = "RETURN"), cancellable = true)
    private void gbw$modifyParticleColor(BlockState state, BlockAndTintGetter world, BlockPos pos, int i, CallbackInfoReturnable<Integer> cir) {
        if (!state.is(FloraAndFaunaTags.IGNORE_SEASON_COLOR)) {
            Level level = world instanceof RenderSectionRegion region ? region.level : world instanceof ClientLevel clientLevel ? clientLevel : null;

            if (level == null && FabricLoader.getInstance().isModLoaded("sodium")) {
                if (world instanceof LevelSlice levelSlice) level = ((LevelSliceAccessor) (Object) levelSlice).gbw$getLevel();
            }

            if (level != null) {
                if (level.dimensionTypeRegistration().is(BuiltinDimensionTypes.OVERWORLD)) {
                    cir.setReturnValue(Season.applyOverworldColorChange(cir.getReturnValue()));
                    return;
                } else if (!level.dimensionTypeRegistration().is(FloraAndFaunaTags.AFFECTED_BY_SEASONS)) {
                    cir.setReturnValue(cir.getReturnValue());
                    return;
                }
            }

            if (FloraAndFaunaClient.getCurrentSeason() != null) {
                if (FloraAndFaunaClient.getTransitionContext() != null) {
                    cir.setReturnValue(FloraAndFaunaClient.getTransitionContext().getSeasonGrassColor(level, pos, cir.getReturnValue()));
                } else
                    cir.setReturnValue(FloraAndFaunaClient.getCurrentSeason().getParticleColorChange().apply(new Season.Context(level, pos, cir.getReturnValue())));
            }
        }
    }

    @Inject(method = "method_1695", at = @At("HEAD"), cancellable = true)
    private static void gbw$modifySpruceColor(BlockState state, BlockAndTintGetter world, BlockPos pos, int i, CallbackInfoReturnable<Integer> cir) {
        if (!state.is(FloraAndFaunaTags.IGNORE_SEASON_COLOR)) {
            Level level = world instanceof RenderSectionRegion region ? region.level : world instanceof ClientLevel clientLevel ? clientLevel : null;

            if (level == null && FabricLoader.getInstance().isModLoaded("sodium")) {
                if (world instanceof LevelSlice levelSlice) level = ((LevelSliceAccessor) (Object) levelSlice).gbw$getLevel();
            }

            if (level != null) {
                if (level.dimensionTypeRegistration().is(BuiltinDimensionTypes.OVERWORLD)) {
                    cir.setReturnValue(Season.applyOverworldColorChange(-10380959));
                    return;
                } else if (!level.dimensionTypeRegistration().is(FloraAndFaunaTags.AFFECTED_BY_SEASONS)) {
                    cir.setReturnValue(-10380959);
                    return;
                }
            }

            if (FloraAndFaunaClient.getCurrentSeason() != null) {
                if (FloraAndFaunaClient.getTransitionContext() != null) {
                    cir.setReturnValue(FloraAndFaunaClient.getTransitionContext().getSeasonFoliageColor(world, pos, -10380959));
                } else
                    cir.setReturnValue(FloraAndFaunaClient.getCurrentSeason().getFoliageColorChange().apply(new Season.Context(world, pos, -10380959)));
            }
        }
    }

    @Inject(method = "method_1687", at = @At("HEAD"), cancellable = true)
    private static void gbw$modifyBirchColor(BlockState state, BlockAndTintGetter world, BlockPos pos, int i, CallbackInfoReturnable<Integer> cir) {
        if (!state.is(FloraAndFaunaTags.IGNORE_SEASON_COLOR)) {
            Level level = world instanceof RenderSectionRegion region ? region.level : world instanceof ClientLevel clientLevel ? clientLevel : null;

            if (level == null && FabricLoader.getInstance().isModLoaded("sodium")) {
                if (world instanceof LevelSlice levelSlice) level = ((LevelSliceAccessor) (Object) levelSlice).gbw$getLevel();
            }

            if (level != null) {
                if (level.dimensionTypeRegistration().is(BuiltinDimensionTypes.OVERWORLD)) {
                    cir.setReturnValue(Season.applyOverworldColorChange(-8345771));
                    return;
                } else if (!level.dimensionTypeRegistration().is(FloraAndFaunaTags.AFFECTED_BY_SEASONS)) {
                    cir.setReturnValue(-8345771);
                    return;
                }
            }

            if (FloraAndFaunaClient.getCurrentSeason() != null) {
                if (FloraAndFaunaClient.getTransitionContext() != null) {
                    cir.setReturnValue(FloraAndFaunaClient.getTransitionContext().getSeasonFoliageColor(world, pos, -8345771));
                } else
                    cir.setReturnValue(FloraAndFaunaClient.getCurrentSeason().getFoliageColorChange().apply(new Season.Context(world, pos, -8345771)));
            }
        }
    }
}
