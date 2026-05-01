package dev.creoii.greatbigworld.floraandfauna.mixin.client;

import dev.creoii.greatbigworld.floraandfauna.client.FloraAndFaunaClient;
import dev.creoii.greatbigworld.floraandfauna.mixin.client.compat.sodium.LevelSliceAccessor;
import dev.creoii.greatbigworld.floraandfauna.season.Season;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaTags;
import net.caffeinemc.mods.sodium.client.world.LevelSlice;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.chunk.RenderSectionRegion;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BiomeColors.class)
public class BiomeColorsMixin {
    @Inject(method = "getAverageFoliageColor", at = @At("RETURN"), cancellable = true)
    private static void gbw$modifyBiomeFoliageColor(BlockAndTintGetter world, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        if (!world.getBlockState(pos).is(FloraAndFaunaTags.IGNORE_SEASON_COLOR)) {
            Level level = world instanceof RenderSectionRegion region ? region.level : world instanceof ClientLevel clientLevel ? clientLevel : null;

            if (level == null && FabricLoader.getInstance().isModLoaded("sodium")) {
                if (world instanceof LevelSlice levelSlice) level = ((LevelSliceAccessor) (Object) levelSlice).gbw$getLevel();
            }

            if (level != null && level.dimensionTypeRegistration().is(BuiltinDimensionTypes.OVERWORLD)) {
                cir.setReturnValue(Season.applyOverworldColorChange(cir.getReturnValue()));
                return;
            }

            if (FloraAndFaunaClient.getCurrentSeason() != null) {
                if (FloraAndFaunaClient.getTransitionContext() != null) {
                    cir.setReturnValue(FloraAndFaunaClient.getTransitionContext().getSeasonFoliageColor(world, pos, cir.getReturnValue()));
                } else {
                    cir.setReturnValue(FloraAndFaunaClient.getCurrentSeason().getFoliageColorChange().apply(new Season.Context(world, pos, cir.getReturnValue())));
                }
            }
        }
    }

    @Inject(method = "getAverageGrassColor", at = @At("RETURN"), cancellable = true)
    private static void gbw$modifyBiomeGrassColor(BlockAndTintGetter world, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        if (!world.getBlockState(pos).is(FloraAndFaunaTags.IGNORE_SEASON_COLOR)) {
            Level level = world instanceof RenderSectionRegion region ? region.level : world instanceof ClientLevel clientLevel ? clientLevel : null;

            if (level == null && FabricLoader.getInstance().isModLoaded("sodium")) {
                if (world instanceof LevelSlice levelSlice) level = ((LevelSliceAccessor) (Object) levelSlice).gbw$getLevel();
            }

            if (level != null && level.dimensionTypeRegistration().is(BuiltinDimensionTypes.OVERWORLD)) {
                cir.setReturnValue(Season.applyOverworldColorChange(cir.getReturnValue()));
                return;
            }

            if (FloraAndFaunaClient.getCurrentSeason() != null) {
                if (FloraAndFaunaClient.getTransitionContext() != null) {
                    cir.setReturnValue(FloraAndFaunaClient.getTransitionContext().getSeasonGrassColor(world, pos, cir.getReturnValue()));
                } else {
                    cir.setReturnValue(FloraAndFaunaClient.getCurrentSeason().getGrassColorChange().apply(new Season.Context(world, pos, cir.getReturnValue())));
                }
            }
        }
    }

    @Inject(method = "getAverageDryFoliageColor(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;)I", at = @At("RETURN"), cancellable = true)
    private static void gbw$modifyBiomeDryFoliageColor(BlockAndTintGetter world, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        if (!world.getBlockState(pos).is(FloraAndFaunaTags.IGNORE_SEASON_COLOR)) {
            Level level = world instanceof RenderSectionRegion region ? region.level : world instanceof ClientLevel clientLevel ? clientLevel : null;

            if (level == null && FabricLoader.getInstance().isModLoaded("sodium")) {
                if (world instanceof LevelSlice levelSlice) level = ((LevelSliceAccessor) (Object) levelSlice).gbw$getLevel();
            }

            if (level != null && level.dimensionTypeRegistration().is(BuiltinDimensionTypes.OVERWORLD)) {
                cir.setReturnValue(Season.applyOverworldColorChange(cir.getReturnValue()));
                return;
            }

            if (FloraAndFaunaClient.getCurrentSeason() != null) {
                if (FloraAndFaunaClient.getTransitionContext() != null) {
                    cir.setReturnValue(FloraAndFaunaClient.getTransitionContext().getSeasonGrassColor(world, pos, cir.getReturnValue()));
                } else {
                    cir.setReturnValue(FloraAndFaunaClient.getCurrentSeason().getGrassColorChange().apply(new Season.Context(world, pos, cir.getReturnValue())));
                }
            }
        }
    }
}
