package dev.creoii.greatbigworld.floraandfauna.mixin.client.compat.particlerain;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.floraandfauna.client.FloraAndFaunaClient;
import dev.creoii.greatbigworld.floraandfauna.season.Season;
import dev.creoii.greatbigworld.floraandfauna.season.SeasonManager;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import pigcart.particlerain.VersionUtil;

@Mixin(VersionUtil.class)
public class VersionUtilMixin {
    @WrapOperation(method = "getPrecipitationAt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/biome/Biome;getPrecipitationAt(Lnet/minecraft/core/BlockPos;I)Lnet/minecraft/world/level/biome/Biome$Precipitation;"))
    private static Biome.Precipitation gbw$modifyCanSetSnowForWinter(Biome instance, BlockPos pos, int seaLevel, Operation<Biome.Precipitation> original, @Local(argsOnly = true) Level level) {
        if (level.isClientSide() && FloraAndFaunaClient.getCurrentSeason() == Season.WINTER && level.dimensionTypeRegistration().is(FloraAndFaunaTags.AFFECTED_BY_SEASONS) && !level.getBiome(pos).is(FloraAndFaunaTags.NOT_AFFECTED_BY_WINTER))
            return Biome.Precipitation.SNOW;
        else {
            if (level instanceof ServerLevel serverWorld) {
                SeasonManager seasonManager = SeasonManager.getInstance(serverWorld.getServer());
                if (seasonManager == null || !serverWorld.dimensionTypeRegistration().is(FloraAndFaunaTags.AFFECTED_BY_SEASONS))
                    return original.call(instance, pos, seaLevel);
                return seasonManager.getCurrentSeason() == Season.WINTER && !level.getBiome(pos).is(FloraAndFaunaTags.NOT_AFFECTED_BY_WINTER) ? Biome.Precipitation.SNOW : original.call(instance, pos, seaLevel);
            } else return original.call(instance, pos, seaLevel);
        }
    }
}
