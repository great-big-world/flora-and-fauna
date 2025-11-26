package dev.creoii.greatbigworld.floraandfauna.mixin.client.compat.particlerain;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.floraandfauna.client.FloraAndFaunaClient;
import dev.creoii.greatbigworld.floraandfauna.season.Season;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaTags;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import pigcart.particlerain.WeatherParticleManager;
import pigcart.particlerain.config.ConfigData;

@Mixin(WeatherParticleManager.class)
public class WeatherParticleManagerMixin {
    @WrapWithCondition(method = "tickBlockFX", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;addParticleClient(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V"))
    private static boolean gbw$overrideParticleRainBlockFX(ClientWorld instance, ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ, @Local(name = "opts") ConfigData.ParticleData opts, @Local(name = "biome") RegistryEntry<Biome> biome) {
        return "snow".equals(opts.id) == (FloraAndFaunaClient.getCurrentSeason() == Season.WINTER && !biome.isIn(FloraAndFaunaTags.NOT_AFFECTED_BY_WINTER));
    }

    @WrapWithCondition(method = "tickBlockFX", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/ParticleManager;addParticle(Lnet/minecraft/client/particle/Particle;)V"))
    private static boolean gbw$overrideParticleRainBlockFX(ParticleManager instance, Particle particle, @Local(name = "opts") ConfigData.ParticleData opts, @Local(name = "biome") RegistryEntry<Biome> biome) {
        return "snow".equals(opts.id) == (FloraAndFaunaClient.getCurrentSeason() == Season.WINTER && !biome.isIn(FloraAndFaunaTags.NOT_AFFECTED_BY_WINTER));
    }

    @WrapWithCondition(method = "tickSkyFX", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;addParticleClient(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V"))
    private static boolean gbw$overrideParticleRainSkyFX(ClientWorld instance, ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ, @Local(name = "biome") RegistryEntry<Biome> biome, @Local(name = "data") ConfigData.ParticleData data) {
        return "snow".equals(data.id) == (FloraAndFaunaClient.getCurrentSeason() == Season.WINTER && !biome.isIn(FloraAndFaunaTags.NOT_AFFECTED_BY_WINTER));
    }

    @WrapWithCondition(method = "tickSkyFX", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/ParticleManager;addParticle(Lnet/minecraft/client/particle/Particle;)V"))
    private static boolean gbw$overrideParticleRainSkyFX(ParticleManager instance, Particle particle, @Local(name = "biome") RegistryEntry<Biome> biome, @Local(name = "data") ConfigData.ParticleData data) {
        return "snow".equals(data.id) == (FloraAndFaunaClient.getCurrentSeason() == Season.WINTER && !biome.isIn(FloraAndFaunaTags.NOT_AFFECTED_BY_WINTER));
    }

    @WrapWithCondition(method = "tickSurfaceFX", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;addParticleClient(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V"))
    private static boolean gbw$overrideParticleRainSurfaceFX(ClientWorld instance, ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ, @Local(name = "biome") RegistryEntry<Biome> biome, @Local(name = "data") ConfigData.ParticleData data) {
        return "snow".equals(data.id) == (FloraAndFaunaClient.getCurrentSeason() == Season.WINTER && !biome.isIn(FloraAndFaunaTags.NOT_AFFECTED_BY_WINTER));
    }

    @WrapWithCondition(method = "tickSurfaceFX", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/ParticleManager;addParticle(Lnet/minecraft/client/particle/Particle;)V"))
    private static boolean gbw$overrideParticleRainSurfaceFX(ParticleManager instance, Particle particle, @Local(name = "biome") RegistryEntry<Biome> biome, @Local(name = "data") ConfigData.ParticleData data) {
        return "snow".equals(data.id) == (FloraAndFaunaClient.getCurrentSeason() == Season.WINTER && !biome.isIn(FloraAndFaunaTags.NOT_AFFECTED_BY_WINTER));
    }
}
