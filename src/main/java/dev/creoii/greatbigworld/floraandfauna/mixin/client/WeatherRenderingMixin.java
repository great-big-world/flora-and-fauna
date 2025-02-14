package dev.creoii.greatbigworld.floraandfauna.mixin.client;

import dev.creoii.greatbigworld.floraandfauna.client.FloraAndFaunaClient;
import dev.creoii.greatbigworld.floraandfauna.season.Season;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaTags;
import net.minecraft.client.render.WeatherRendering;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WeatherRendering.class)
public class WeatherRenderingMixin {
    @Redirect(method = "buildPrecipitationPieces", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WeatherRendering;getPrecipitationAt(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/world/biome/Biome$Precipitation;"))
    private Biome.Precipitation gbw$modifyPrecipitationForWinter(WeatherRendering instance, World world, BlockPos pos) {
        return FloraAndFaunaClient.getCurrentSeason() == Season.WINTER && !world.getBiome(pos).isIn(FloraAndFaunaTags.NOT_AFFECTED_BY_WINTER) ? Biome.Precipitation.SNOW : instance.getPrecipitationAt(world, pos);
    }

    @Redirect(method = "addParticlesAndSound", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WeatherRendering;getPrecipitationAt(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/world/biome/Biome$Precipitation;"))
    private Biome.Precipitation gbw$modifyRainSplashingForWinter(WeatherRendering instance, World world, BlockPos pos) {
        return FloraAndFaunaClient.getCurrentSeason() == Season.WINTER && !world.getBiome(pos).isIn(FloraAndFaunaTags.NOT_AFFECTED_BY_WINTER) ? Biome.Precipitation.SNOW : instance.getPrecipitationAt(world, pos);
    }
}
