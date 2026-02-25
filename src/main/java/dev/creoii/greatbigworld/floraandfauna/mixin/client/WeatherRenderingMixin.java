package dev.creoii.greatbigworld.floraandfauna.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.floraandfauna.client.FloraAndFaunaClient;
import dev.creoii.greatbigworld.floraandfauna.season.Season;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaTags;
import net.minecraft.client.renderer.WeatherEffectRenderer;
import net.minecraft.client.renderer.state.WeatherRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WeatherEffectRenderer.class)
public abstract class WeatherRenderingMixin {
    @Shadow
    protected abstract WeatherEffectRenderer.ColumnInstance createRainColumnInstance(RandomSource randomSource, int i, int j, int k, int l, int m, int n, float f);

    @Shadow
    protected abstract WeatherEffectRenderer.ColumnInstance createSnowColumnInstance(RandomSource randomSource, int i, int j, int k, int l, int m, int n, float f);

    @Redirect(method = "extractRenderState", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/WeatherEffectRenderer;getPrecipitationAt(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/biome/Biome$Precipitation;"))
    private Biome.Precipitation gbw$modifyPrecipitationForWinter(WeatherEffectRenderer instance, Level world, BlockPos pos) {
        return FloraAndFaunaClient.getCurrentSeason() == Season.WINTER && world.dimensionTypeRegistration().is(FloraAndFaunaTags.AFFECTED_BY_SEASONS) && !world.getBiome(pos).is(FloraAndFaunaTags.NOT_AFFECTED_BY_WINTER) ? Biome.Precipitation.SNOW : instance.getPrecipitationAt(world, pos);
    }

    @Redirect(method = "tickRainParticles", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/WeatherEffectRenderer;getPrecipitationAt(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/biome/Biome$Precipitation;"))
    private Biome.Precipitation gbw$modifyRainSplashingForWinter(WeatherEffectRenderer instance, Level world, BlockPos pos) {
        return FloraAndFaunaClient.getCurrentSeason() == Season.WINTER && world.dimensionTypeRegistration().is(FloraAndFaunaTags.AFFECTED_BY_SEASONS) && !world.getBiome(pos).is(FloraAndFaunaTags.NOT_AFFECTED_BY_WINTER) ? Biome.Precipitation.SNOW : instance.getPrecipitationAt(world, pos);
    }

    @Inject(method = "extractRenderState", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"))
    private void gbw$improveWinterPrecipitationTransition(Level level, int i, float f, Vec3 vec3, WeatherRenderState weatherRenderState, CallbackInfo ci, @Local RandomSource randomSource, @Local Biome.Precipitation precipitation, @Local(ordinal = 4) int m, @Local(ordinal = 5) int n, @Local(ordinal = 7) int p, @Local(ordinal = 8) int q, @Local(ordinal = 11) int t) {
        if (FloraAndFaunaClient.getTransitionContext() != null && FloraAndFaunaClient.isTransitioning()) {
            boolean wasOrIsWinter = FloraAndFaunaClient.getTransitionContext().getCurrent() == Season.WINTER || FloraAndFaunaClient.getTransitionContext().getNext() == Season.WINTER;
            if (!wasOrIsWinter)
                return;

            if (precipitation == Biome.Precipitation.RAIN) {
                weatherRenderState.snowColumns.add(createSnowColumnInstance(randomSource, i, n, p, q, m, t, f));
            } else if (precipitation == Biome.Precipitation.SNOW) {
                weatherRenderState.rainColumns.add(createRainColumnInstance(randomSource, i, n, p, q, m, t, f));
            }
        }
    }
}
