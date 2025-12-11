package dev.creoii.greatbigworld.floraandfauna.mixin.client.compat.sodium;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.floraandfauna.season.TransitionQuality;
import dev.creoii.greatbigworld.floraandfauna.util.ExtendedGameOptions;
import net.caffeinemc.mods.sodium.api.config.StorageEventHandler;
import net.caffeinemc.mods.sodium.api.config.option.OptionFlag;
import net.caffeinemc.mods.sodium.api.config.option.OptionImpact;
import net.caffeinemc.mods.sodium.api.config.structure.ConfigBuilder;
import net.caffeinemc.mods.sodium.api.config.structure.EnumOptionBuilder;
import net.caffeinemc.mods.sodium.api.config.structure.OptionGroupBuilder;
import net.caffeinemc.mods.sodium.client.gui.SodiumConfigBuilder;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Options;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SodiumConfigBuilder.class)
public class SodiumGameOptionPagesMixin {
    @Shadow
    @Final
    private StorageEventHandler vanillaStorage;

    @Shadow
    @Final
    private Options vanillaOpts;

    @ModifyExpressionValue(method = "buildQualityPage", at = @At(value = "INVOKE", target = "Lnet/caffeinemc/mods/sodium/api/config/structure/OptionGroupBuilder;addOption(Lnet/caffeinemc/mods/sodium/api/config/structure/OptionBuilder;)Lnet/caffeinemc/mods/sodium/api/config/structure/OptionGroupBuilder;", ordinal = 1), remap = false)
    private OptionGroupBuilder gbw$addSodiumSeasonTransitionQualityOption(OptionGroupBuilder original, @Local(argsOnly = true, name = "arg1") ConfigBuilder builder) {
        return original.addOption(builder.createEnumOption(Identifier.fromNamespaceAndPath("sodium", "season_transition_quality"), TransitionQuality.class).setName(Component.translatable("options.seasonTransitionQuality")).setTooltip(Component.empty()).setElementNameProvider(EnumOptionBuilder.nameProviderFrom(TransitionQuality.NAMES)).setBinding(quality -> {
            ClientPlayNetworking.send(new TransitionQuality.SyncTransitionQuality((byte) quality.getQuality()));
        }, () -> ((ExtendedGameOptions) vanillaOpts).gbw$getSeasonTransitionQuality().get()).setStorageHandler(vanillaStorage).setDefaultValue(TransitionQuality.NORMAL).setImpact(OptionImpact.VARIES).setFlags(OptionFlag.REQUIRES_RENDERER_UPDATE));
    }
}
