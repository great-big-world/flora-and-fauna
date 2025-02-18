package dev.creoii.greatbigworld.floraandfauna.mixin.client.compat.visuality;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.floraandfauna.client.FloraAndFaunaClient;
import dev.creoii.greatbigworld.floraandfauna.season.Season;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaTags;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import visuality.event.CirclesOnWaterEvent;

@Mixin(CirclesOnWaterEvent.class)
public class CirclesOnWaterEventMixin {
    @Shadow static AbstractClientPlayerEntity player;

    @WrapOperation(method = "onTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/Biome;doesNotSnow(Lnet/minecraft/util/math/BlockPos;I)Z"))
    private static boolean gbw$noRainSplashDuringSnow(Biome instance, BlockPos pos, int seaLevel, Operation<Boolean> original, @Local(argsOnly = true) ClientWorld var0) {
        return original.call(instance, pos, seaLevel) && (FloraAndFaunaClient.getCurrentSeason() != Season.WINTER || var0.getBiome(player.getSteppingPos()).isIn(FloraAndFaunaTags.NOT_AFFECTED_BY_WINTER));
    }
}
