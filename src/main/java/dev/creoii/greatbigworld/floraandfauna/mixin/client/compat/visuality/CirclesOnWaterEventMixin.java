package dev.creoii.greatbigworld.floraandfauna.mixin.client.compat.visuality;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.floraandfauna.client.FloraAndFaunaClient;
import dev.creoii.greatbigworld.floraandfauna.season.Season;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaTags;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import visuality.event.CirclesOnWaterEvent;

@Mixin(CirclesOnWaterEvent.class)
public class CirclesOnWaterEventMixin {
    @Shadow static AbstractClientPlayer player;

    @WrapOperation(method = "onTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/biome/Biome;warmEnoughToRain(Lnet/minecraft/core/BlockPos;I)Z"))
    private static boolean gbw$noRainSplashDuringSnow(Biome instance, BlockPos pos, int seaLevel, Operation<Boolean> original, @Local(argsOnly = true) ClientLevel var0) {
        return original.call(instance, pos, seaLevel) && (FloraAndFaunaClient.getCurrentSeason() != Season.WINTER || var0.getBiome(player.getOnPos()).is(FloraAndFaunaTags.NOT_AFFECTED_BY_WINTER));
    }
}
