package dev.creoii.greatbigworld.floraandfauna.mixin.client.compat.sodium;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.floraandfauna.season.TransitionQuality;
import dev.creoii.greatbigworld.floraandfauna.util.ExtendedGameOptions;
import net.caffeinemc.mods.sodium.client.gui.SodiumGameOptionPages;
import net.caffeinemc.mods.sodium.client.gui.options.*;
import net.caffeinemc.mods.sodium.client.gui.options.control.CyclingControl;
import net.caffeinemc.mods.sodium.client.gui.options.storage.MinecraftOptionsStorage;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(SodiumGameOptionPages.class)
public class SodiumGameOptionPagesMixin {
    @Shadow(remap = false) @Final private static MinecraftOptionsStorage vanillaOpts;

    @ModifyExpressionValue(method = "quality", at = @At(value = "INVOKE", target = "Lnet/caffeinemc/mods/sodium/client/gui/options/OptionGroup$Builder;add(Lnet/caffeinemc/mods/sodium/client/gui/options/Option;)Lnet/caffeinemc/mods/sodium/client/gui/options/OptionGroup$Builder;", ordinal = 1), remap = false)
    private static OptionGroup.Builder gbw$addSodiumSeasonTransitionQualityOption(OptionGroup.Builder original, @Local List<OptionGroup> groups) {
        return original.add(OptionImpl.createBuilder(TransitionQuality.class, vanillaOpts).setName(Text.translatable("options.seasonTransitionQuality")).setTooltip(Text.empty()).setControl(option -> new CyclingControl<>(option, TransitionQuality.class)).setBinding((opts, value) -> {
            ClientPlayNetworking.send(new TransitionQuality.SyncTransitionQuality((byte) value.getQuality()));
        }, opts -> ((ExtendedGameOptions) opts).gbw$getSeasonTransitionQuality().getValue()).setImpact(OptionImpact.VARIES).setFlags(new OptionFlag[]{OptionFlag.REQUIRES_RENDERER_UPDATE}).build());
    }
}
