package dev.creoii.greatbigworld.floraandfauna.mixin.client;

import dev.creoii.greatbigworld.floraandfauna.client.FloraAndFaunaClient;
import dev.creoii.greatbigworld.floraandfauna.season.Season;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaTags;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @Shadow private @Nullable ClientWorld world;

    @Redirect(method = "renderWeather", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/Biome;getPrecipitation(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/world/biome/Biome$Precipitation;"))
    private Biome.Precipitation gbw$modifyPrecipitationForWinter(Biome instance, BlockPos pos) {
        return FloraAndFaunaClient.getCurrentSeason() == Season.WINTER && !world.getBiome(pos).isIn(FloraAndFaunaTags.NOT_AFFECTED_BY_WINTER) ? Biome.Precipitation.SNOW : instance.getPrecipitation(pos);
    }

    @Redirect(method = "tickRainSplashing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/Biome;getPrecipitation(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/world/biome/Biome$Precipitation;"))
    private Biome.Precipitation gbw$modifyRainSplashingForWinter(Biome instance, BlockPos pos) {
        return FloraAndFaunaClient.getCurrentSeason() == Season.WINTER && !world.getBiome(pos).isIn(FloraAndFaunaTags.NOT_AFFECTED_BY_WINTER) ? Biome.Precipitation.SNOW : instance.getPrecipitation(pos);
    }
}
