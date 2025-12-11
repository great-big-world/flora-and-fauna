package dev.creoii.greatbigworld.floraandfauna.mixin.client;

import dev.creoii.greatbigworld.floraandfauna.season.TransitionQuality;
import dev.creoii.greatbigworld.floraandfauna.util.ExtendedGameOptions;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Arrays;

@Mixin(Options.class)
public abstract class GameOptionsMixin implements ExtendedGameOptions {
    @Unique
    private final OptionInstance<TransitionQuality> seasonTransitionQuality = new OptionInstance<>(
            "options.seasonTransitionQuality",
            OptionInstance.noTooltip(),
            (optionText, value) -> TransitionQuality.NAMES[value.ordinal()],
            new OptionInstance.Enum<>(Arrays.asList(TransitionQuality.values()), TransitionQuality.CODEC),
            TransitionQuality.NORMAL,
            value -> ClientPlayNetworking.send(new TransitionQuality.SyncTransitionQuality((byte) gbw$getSeasonTransitionQuality().get().getQuality())));

    @Override
    public OptionInstance<TransitionQuality> gbw$getSeasonTransitionQuality() {
        return seasonTransitionQuality;
    }
}
