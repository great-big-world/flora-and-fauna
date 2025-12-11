package dev.creoii.greatbigworld.floraandfauna.util;

import dev.creoii.greatbigworld.floraandfauna.season.TransitionQuality;
import net.minecraft.client.OptionInstance;

public interface ExtendedGameOptions {
    OptionInstance<TransitionQuality> gbw$getSeasonTransitionQuality();
}
