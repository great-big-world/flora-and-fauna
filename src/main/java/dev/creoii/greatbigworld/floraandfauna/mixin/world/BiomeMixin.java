package dev.creoii.greatbigworld.floraandfauna.mixin.world;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.floraandfauna.client.FloraAndFaunaClient;
import dev.creoii.greatbigworld.floraandfauna.season.Season;
import dev.creoii.greatbigworld.floraandfauna.season.SeasonManager;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Biome.class)
public class BiomeMixin {
    @WrapOperation(method = "shouldSnow", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/biome/Biome;getPrecipitationAt(Lnet/minecraft/core/BlockPos;I)Lnet/minecraft/world/level/biome/Biome$Precipitation;"))
    private Biome.Precipitation gbw$modifyCanSetSnowForWinter(Biome instance, BlockPos pos, int seaLevel, Operation<Biome.Precipitation> original, @Local(argsOnly = true) LevelReader world) {
        if (world.isClientSide() && (FloraAndFaunaClient.getCurrentSeason() == Season.WINTER && !world.getBiome(pos).is(FloraAndFaunaTags.NOT_AFFECTED_BY_WINTER) && Blocks.SNOW.defaultBlockState().canSurvive(world, pos) && world.isInsideBuildHeight(pos.getY()) && world.getBrightness(LightLayer.BLOCK, pos) < 10))
            return Biome.Precipitation.SNOW;
        else {
            if (world instanceof ServerLevel serverWorld) {
                SeasonManager seasonManager = SeasonManager.getInstance(serverWorld.getServer());
                if (seasonManager == null || !serverWorld.dimensionTypeRegistration().is(FloraAndFaunaTags.AFFECTED_BY_SEASONS))
                    return original.call(instance, pos, seaLevel);
                return (seasonManager.getCurrentSeason() == Season.WINTER && !world.getBiome(pos).is(FloraAndFaunaTags.NOT_AFFECTED_BY_WINTER) && Blocks.SNOW.defaultBlockState().canSurvive(world, pos) && world.isInsideBuildHeight(pos.getY()) && world.getBrightness(LightLayer.BLOCK, pos) < 10) ? Biome.Precipitation.SNOW : original.call(instance, pos, seaLevel);
            } else return original.call(instance, pos, seaLevel);
        }
    }

    @WrapOperation(method = "shouldFreeze(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;Z)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/biome/Biome;warmEnoughToRain(Lnet/minecraft/core/BlockPos;I)Z"))
    private boolean gbw$modifyCanSetIceForWinter(Biome instance, BlockPos pos, int seaLevel, Operation<Boolean> original, @Local(argsOnly = true) LevelReader world) {
        if (world.isClientSide())
            return original.call(instance, pos, seaLevel) && (FloraAndFaunaClient.getCurrentSeason() != Season.WINTER || world.getBiome(pos).is(FloraAndFaunaTags.NOT_AFFECTED_BY_WINTER));
        else {
            if (world instanceof ServerLevel serverWorld) {
                SeasonManager seasonManager = SeasonManager.getInstance(serverWorld.getServer());
                if (seasonManager == null || !serverWorld.dimensionTypeRegistration().is(FloraAndFaunaTags.AFFECTED_BY_SEASONS))
                    return original.call(instance, pos, seaLevel);
                return original.call(instance, pos, seaLevel) && (seasonManager.getCurrentSeason() != Season.WINTER || world.getBiome(pos).is(FloraAndFaunaTags.NOT_AFFECTED_BY_WINTER));
            } else return original.call(instance, pos, seaLevel);
        }
    }
}
