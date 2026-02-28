package dev.creoii.greatbigworld.floraandfauna.client;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.floraandfauna.registry.FloraAndFaunaBlocks;
import dev.creoii.greatbigworld.floraandfauna.season.Season;
import dev.creoii.greatbigworld.floraandfauna.season.TransitionContext;
import dev.creoii.greatbigworld.floraandfauna.season.TransitionQuality;
import dev.creoii.greatbigworld.util.OptionsAPI;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.OptionInstance;
import net.minecraft.resources.Identifier;
import org.apache.logging.log4j.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class FloraAndFaunaClient implements ClientModInitializer {
    protected static final boolean SODIUM_LOADED = FabricLoader.getInstance().isModLoaded("sodium");
    @Nullable protected static Season currentSeason;
    @Nullable protected static TransitionContext transitionContext;
    protected static boolean transitioning = false;

    @Override
    public void onInitializeClient() {
        if (SODIUM_LOADED)
            GreatBigWorld.LOGGER.log(Level.INFO, "Sodium detected, modifying season sync color rebuilds.");

        FloraAndFaunaBlocks.registerClient();
        FloraAndFaunaClientNetworking.register();

        Identifier seasonTransitionQualityId = Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "season_transition_quality");
        OptionsAPI.registerVideoOption(seasonTransitionQualityId, new OptionInstance<>(
                "options.seasonTransitionQuality",
                OptionInstance.noTooltip(),
                (optionText, value) -> TransitionQuality.NAMES[value.ordinal()],
                new OptionInstance.Enum<>(Arrays.asList(TransitionQuality.values()), TransitionQuality.CODEC),
                TransitionQuality.NORMAL,
                value -> {
                    @SuppressWarnings("unchecked")
                    OptionInstance<TransitionQuality> optionInstance = (OptionInstance<TransitionQuality>) OptionsAPI.getOption(seasonTransitionQualityId);
                    ClientPlayNetworking.send(new TransitionQuality.SyncTransitionQuality((byte) optionInstance.get().getQuality()));
                }));
    }

    public static @Nullable Season getCurrentSeason() {
        return currentSeason;
    }

    public static @Nullable TransitionContext getTransitionContext() {
        return transitionContext;
    }

    public static boolean isTransitioning() {
        return transitioning;
    }
}
