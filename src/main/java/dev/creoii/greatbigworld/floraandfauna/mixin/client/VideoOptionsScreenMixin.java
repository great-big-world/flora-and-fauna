package dev.creoii.greatbigworld.floraandfauna.mixin.client;

import dev.creoii.greatbigworld.floraandfauna.util.ExtendedGameOptions;
import net.minecraft.client.gui.screen.option.VideoOptionsScreen;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;

@Mixin(VideoOptionsScreen.class)
public class VideoOptionsScreenMixin {
    @Inject(method = "getOptions", at = @At("RETURN"), cancellable = true)
    private static void gbw$addCustomVideoOptions(GameOptions gameOptions, CallbackInfoReturnable<SimpleOption<?>[]> cir) {
        if (gameOptions instanceof ExtendedGameOptions extendedGameOptions) {
            SimpleOption<?>[] newOptions = Arrays.copyOf(cir.getReturnValue(), cir.getReturnValue().length + 1);
            newOptions[newOptions.length - 1] = extendedGameOptions.gbw$getSeasonTransitionQuality();
            cir.setReturnValue(newOptions);
        }
    }
}
