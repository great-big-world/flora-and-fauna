package dev.creoii.greatbigworld.floraandfauna.mixin.client.compat.particlerain;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.floraandfauna.client.FloraAndFaunaClient;
import dev.creoii.greatbigworld.floraandfauna.season.Season;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaTags;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import pigcart.particlerain.WeatherParticleManager;
import pigcart.particlerain.config.ConfigData;

@Mixin(WeatherParticleManager.class)
public class WeatherParticleSpawnerMixin {
    /*@WrapOperation(method = "spawnParticle", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;addParticleClient(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V", ordinal = 2))
    private static void gbw$snowDuringWinter(ClientWorld instance, ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ, Operation<Void> original, @Local(argsOnly = true) RegistryEntry<Biome> biome) {
        if (FloraAndFaunaClient.getCurrentSeason() != Season.WINTER || biome.isIn(FloraAndFaunaTags.NOT_AFFECTED_BY_WINTER)) {
            original.call(instance, parameters, x, y, z, velocityX, velocityX, velocityZ);
        } else {
            if (instance.random.nextFloat() < ConfigData.CONFIG.snow.density) {
                instance.addParticleClient(ParticleRainClient.SNOW, x, y, z, 0d, 0d, 0d);
            }
        }
    }*/
}
