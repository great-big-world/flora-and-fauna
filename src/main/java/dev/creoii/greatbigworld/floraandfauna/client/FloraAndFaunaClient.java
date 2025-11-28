package dev.creoii.greatbigworld.floraandfauna.client;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.floraandfauna.client.compat.SodiumClientCompat;
import dev.creoii.greatbigworld.floraandfauna.registry.FloraAndFaunaBlocks;
import dev.creoii.greatbigworld.floraandfauna.season.Season;
import dev.creoii.greatbigworld.floraandfauna.season.SeasonManager;
import dev.creoii.greatbigworld.floraandfauna.season.TransitionContext;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaTags;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.render.chunk.ChunkBuilder;
import org.apache.logging.log4j.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class FloraAndFaunaClient implements ClientModInitializer {
    private static final boolean SODIUM_LOADED = FabricLoader.getInstance().isModLoaded("sodium");
    @Nullable private static Season currentSeason;
    @Nullable private static TransitionContext transitionContext;

    @Override
    public void onInitializeClient() {
        if (SODIUM_LOADED)
            GreatBigWorld.LOGGER.log(Level.INFO, "Sodium detected, modifying season sync color rebuilds.");

        FloraAndFaunaBlocks.registerClient();

        ClientPlayNetworking.registerGlobalReceiver(SeasonManager.SyncSeason.PACKET_ID, (payload, context) -> {
            byte season = payload.season();
            context.client().execute(() -> currentSeason = Season.values()[season]);
        });

        ClientPlayNetworking.registerGlobalReceiver(SeasonManager.SyncSeasonTransition.PACKET_ID, (payload, context) -> {
            byte[] context1 = payload.context();
            context.client().execute(() -> {
                if (!context.client().world.getDimensionEntry().isIn(FloraAndFaunaTags.AFFECTED_BY_SEASONS)) {
                    transitionContext = null;
                    return;
                }

                transitionContext = new TransitionContext(Season.values()[context1[0]], Season.values()[context1[1]], context1[2] / 100f);
                if (SODIUM_LOADED) {
                    SodiumClientCompat.rebuildSeason(context.client());
                } else {
                    if (context.client().world != null && context.client().worldRenderer.chunks != null && context.client().player != null) {
                        for (ChunkBuilder.BuiltChunk chunk : Objects.requireNonNull(context.client().worldRenderer.chunks).chunks) {
                            if (chunk == null)
                                continue;
                            chunk.scheduleRebuild(true);
                        }
                    }
                }
            });
        });
    }

    public static @Nullable Season getCurrentSeason() {
        return currentSeason;
    }

    public static @Nullable TransitionContext getTransitionContext() {
        return transitionContext;
    }
}
