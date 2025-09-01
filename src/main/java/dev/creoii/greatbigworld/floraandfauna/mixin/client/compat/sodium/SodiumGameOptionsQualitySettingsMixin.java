package dev.creoii.greatbigworld.floraandfauna.mixin.client.compat.sodium;

import dev.creoii.greatbigworld.floraandfauna.season.TransitionQuality;
import dev.creoii.greatbigworld.floraandfauna.util.SodiumExtendedGameOptions;
import net.caffeinemc.mods.sodium.client.gui.SodiumGameOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SodiumGameOptions.QualitySettings.class)
public class SodiumGameOptionsQualitySettingsMixin implements SodiumExtendedGameOptions {
    @Unique TransitionQuality seasonTransitionQuality;

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    private void gbw$initSodiumSeasonTransitionQuality(CallbackInfo ci) {
        seasonTransitionQuality = TransitionQuality.NORMAL;
    }

    @Override
    public TransitionQuality gbw$getSeasonTransitionQuality() {
        return seasonTransitionQuality;
    }
}
