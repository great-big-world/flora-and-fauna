package dev.creoii.greatbigworld.floraandfauna.mixin.client.compat.particlerain;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.floraandfauna.client.FloraAndFaunaClient;
import dev.creoii.greatbigworld.floraandfauna.season.Season;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaTags;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import pigcart.particlerain.WeatherParticleManager;
import pigcart.particlerain.config.ConfigData;

@Mixin(WeatherParticleManager.class)
public class WeatherParticleManagerMixin {
    @WrapWithCondition(method = "tickBlockFX", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"))
    private static boolean gbw$overrideParticleRainBlockFX(ClientLevel instance, ParticleOptions parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ, @Local(name = "opts") ConfigData.ParticleData opts, @Local(name = "biome") Holder<Biome> biome) {
        return "snow".equals(opts.id) == (FloraAndFaunaClient.getCurrentSeason() == Season.WINTER && instance.dimensionTypeRegistration().is(FloraAndFaunaTags.AFFECTED_BY_SEASONS) && !biome.is(FloraAndFaunaTags.NOT_AFFECTED_BY_WINTER));
    }

    @WrapWithCondition(method = "tickBlockFX", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/ParticleEngine;add(Lnet/minecraft/client/particle/Particle;)V"))
    private static boolean gbw$overrideParticleRainBlockFX(ParticleEngine instance, Particle particle, @Local(name = "opts") ConfigData.ParticleData opts, @Local(name = "biome") Holder<Biome> biome, @Local(name = "level") ClientLevel level) {
        return "snow".equals(opts.id) == (FloraAndFaunaClient.getCurrentSeason() == Season.WINTER && level.dimensionTypeRegistration().is(FloraAndFaunaTags.AFFECTED_BY_SEASONS) && !biome.is(FloraAndFaunaTags.NOT_AFFECTED_BY_WINTER));
    }

    @WrapWithCondition(method = "tickSkyFX", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"))
    private static boolean gbw$overrideParticleRainSkyFX(ClientLevel instance, ParticleOptions parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ, @Local(name = "biome") Holder<Biome> biome, @Local(name = "data") ConfigData.ParticleData data) {
        return "snow".equals(data.id) == (FloraAndFaunaClient.getCurrentSeason() == Season.WINTER && instance.dimensionTypeRegistration().is(FloraAndFaunaTags.AFFECTED_BY_SEASONS) && !biome.is(FloraAndFaunaTags.NOT_AFFECTED_BY_WINTER));
    }

    @WrapWithCondition(method = "tickSkyFX", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/ParticleEngine;add(Lnet/minecraft/client/particle/Particle;)V"))
    private static boolean gbw$overrideParticleRainSkyFX(ParticleEngine instance, Particle particle, @Local(name = "biome") Holder<Biome> biome, @Local(name = "data") ConfigData.ParticleData data, @Local(argsOnly = true) ClientLevel level) {
        return "snow".equals(data.id) == (FloraAndFaunaClient.getCurrentSeason() == Season.WINTER && level.dimensionTypeRegistration().is(FloraAndFaunaTags.AFFECTED_BY_SEASONS) && !biome.is(FloraAndFaunaTags.NOT_AFFECTED_BY_WINTER));
    }

    @WrapWithCondition(method = "tickSurfaceFX", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"))
    private static boolean gbw$overrideParticleRainSurfaceFX(ClientLevel instance, ParticleOptions parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ, @Local(name = "biome") Holder<Biome> biome, @Local(name = "data") ConfigData.ParticleData data) {
        return "snow".equals(data.id) == (FloraAndFaunaClient.getCurrentSeason() == Season.WINTER && instance.dimensionTypeRegistration().is(FloraAndFaunaTags.AFFECTED_BY_SEASONS) && !biome.is(FloraAndFaunaTags.NOT_AFFECTED_BY_WINTER));
    }

    @WrapWithCondition(method = "tickSurfaceFX", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/ParticleEngine;add(Lnet/minecraft/client/particle/Particle;)V"))
    private static boolean gbw$overrideParticleRainSurfaceFX(ParticleEngine instance, Particle particle, @Local(name = "biome") Holder<Biome> biome, @Local(name = "data") ConfigData.ParticleData data, @Local(argsOnly = true) ClientLevel level) {
        return "snow".equals(data.id) == (FloraAndFaunaClient.getCurrentSeason() == Season.WINTER && level.dimensionTypeRegistration().is(FloraAndFaunaTags.AFFECTED_BY_SEASONS) && !biome.is(FloraAndFaunaTags.NOT_AFFECTED_BY_WINTER));
    }
}
