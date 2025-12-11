package dev.creoii.greatbigworld.floraandfauna.mixin.client;

import dev.creoii.greatbigworld.floraandfauna.client.FloraAndFaunaClient;
import dev.creoii.greatbigworld.floraandfauna.season.Season;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaTags;
import net.minecraft.client.renderer.WeatherEffectRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WeatherEffectRenderer.class)
public class WeatherRenderingMixin {
    @Redirect(method = "extractRenderState", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/WeatherEffectRenderer;getPrecipitationAt(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/biome/Biome$Precipitation;"))
    private Biome.Precipitation gbw$modifyPrecipitationForWinter(WeatherEffectRenderer instance, Level world, BlockPos pos) {
        return FloraAndFaunaClient.getCurrentSeason() == Season.WINTER && world.dimensionTypeRegistration().is(FloraAndFaunaTags.AFFECTED_BY_SEASONS) && !world.getBiome(pos).is(FloraAndFaunaTags.NOT_AFFECTED_BY_WINTER) ? Biome.Precipitation.SNOW : instance.getPrecipitationAt(world, pos);
    }

    @Redirect(method = "tickRainParticles", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/WeatherEffectRenderer;getPrecipitationAt(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/biome/Biome$Precipitation;"))
    private Biome.Precipitation gbw$modifyRainSplashingForWinter(WeatherEffectRenderer instance, Level world, BlockPos pos) {
        return FloraAndFaunaClient.getCurrentSeason() == Season.WINTER && world.dimensionTypeRegistration().is(FloraAndFaunaTags.AFFECTED_BY_SEASONS) && !world.getBiome(pos).is(FloraAndFaunaTags.NOT_AFFECTED_BY_WINTER) ? Biome.Precipitation.SNOW : instance.getPrecipitationAt(world, pos);
    }
}
