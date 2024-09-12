package dev.creoii.greatbigworld.floraandfauna.util;

import dev.creoii.greatbigworld.floraandfauna.season.TransitionQuality;
import net.minecraft.client.option.SimpleOption;

public interface ExtendedGameOptions {
    SimpleOption<TransitionQuality> gbw$getSeasonTransitionQuality();
}
