package dev.creoii.greatbigworld.floraandfauna.mixin.client.compat.particlerain;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.floraandfauna.client.FloraAndFaunaClient;
import dev.creoii.greatbigworld.floraandfauna.season.Season;
import dev.creoii.greatbigworld.floraandfauna.season.SeasonManager;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import pigcart.particlerain.VersionUtil;

@Mixin(VersionUtil.class)
public class VersionUtilMixin {
    @WrapOperation(method = "getPrecipitationAt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/Biome;getPrecipitation(Lnet/minecraft/util/math/BlockPos;I)Lnet/minecraft/world/biome/Biome$Precipitation;"))
    private static Biome.Precipitation gbw$modifyCanSetSnowForWinter(Biome instance, BlockPos pos, int seaLevel, Operation<Biome.Precipitation> original, @Local(argsOnly = true, name = "arg0") World level) {
        if (level.isClient() && FloraAndFaunaClient.getCurrentSeason() == Season.WINTER && level.getDimensionEntry().isIn(FloraAndFaunaTags.AFFECTED_BY_SEASONS) && !level.getBiome(pos).isIn(FloraAndFaunaTags.NOT_AFFECTED_BY_WINTER))
            return Biome.Precipitation.SNOW;
        else {
            if (level instanceof ServerWorld serverWorld) {
                SeasonManager seasonManager = SeasonManager.getInstance(serverWorld.getServer());
                if (seasonManager == null || !serverWorld.getDimensionEntry().isIn(FloraAndFaunaTags.AFFECTED_BY_SEASONS))
                    return original.call(instance, pos, seaLevel);
                return seasonManager.getCurrentSeason() == Season.WINTER && !level.getBiome(pos).isIn(FloraAndFaunaTags.NOT_AFFECTED_BY_WINTER) ? Biome.Precipitation.SNOW : original.call(instance, pos, seaLevel);
            } else return original.call(instance, pos, seaLevel);
        }
    }
}
