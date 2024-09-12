package dev.creoii.greatbigworld.floraandfauna.mixin.client;

import dev.creoii.greatbigworld.floraandfauna.season.TransitionQuality;
import dev.creoii.greatbigworld.floraandfauna.util.ExtendedGameOptions;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Arrays;

@Mixin(GameOptions.class)
public abstract class GameOptionsMixin implements ExtendedGameOptions {
    @Unique
    private final SimpleOption<TransitionQuality> seasonTransitionQuality = new SimpleOption<>(
            "options.seasonTransitionQuality",
            SimpleOption.emptyTooltip(),
            SimpleOption.enumValueText(),
            new SimpleOption.PotentialValuesBasedCallbacks<>(
                    Arrays.asList(TransitionQuality.values()),
                    TransitionQuality.CODEC
            ),
            TransitionQuality.NORMAL,
            value -> ClientPlayNetworking.send(new TransitionQuality.SyncTransitionQuality((byte) gbw$getSeasonTransitionQuality().getValue().getQuality()))
    );

    @Override
    public SimpleOption<TransitionQuality> gbw$getSeasonTransitionQuality() {
        return seasonTransitionQuality;
    }
}
