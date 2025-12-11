package dev.creoii.greatbigworld.floraandfauna.mixin.client;

import dev.creoii.greatbigworld.floraandfauna.util.ExtendedGameOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.options.VideoSettingsScreen;

@Mixin(VideoSettingsScreen.class)
public class VideoOptionsScreenMixin {
    @Inject(method = "qualityOptions", at = @At("RETURN"), cancellable = true)
    private static void gbw$addCustomVideoOptions(Options gameOptions, CallbackInfoReturnable<OptionInstance<?>[]> cir) {
        if (gameOptions instanceof ExtendedGameOptions extendedGameOptions) {
            OptionInstance<?>[] newOptions = Arrays.copyOf(cir.getReturnValue(), cir.getReturnValue().length + 1);
            newOptions[newOptions.length - 1] = extendedGameOptions.gbw$getSeasonTransitionQuality();
            cir.setReturnValue(newOptions);
        }
    }
}
