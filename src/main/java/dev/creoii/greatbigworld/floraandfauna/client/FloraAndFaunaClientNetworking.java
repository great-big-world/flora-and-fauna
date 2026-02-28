package dev.creoii.greatbigworld.floraandfauna.client;

import dev.creoii.greatbigworld.floraandfauna.client.compat.SodiumClientCompat;
import dev.creoii.greatbigworld.floraandfauna.season.Season;
import dev.creoii.greatbigworld.floraandfauna.season.SeasonManager;
import dev.creoii.greatbigworld.floraandfauna.season.TransitionContext;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaTags;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;

import java.util.Objects;

public final class FloraAndFaunaClientNetworking {
    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(SeasonManager.SyncSeason.PACKET_ID, (payload, context) -> {
            byte season = payload.season();
            context.client().execute(() -> FloraAndFaunaClient.currentSeason = Season.values()[season]);
        });

        ClientPlayNetworking.registerGlobalReceiver(SeasonManager.SyncSeasonTransition.PACKET_ID, (payload, context) -> {
            byte[] context1 = payload.context();
            context.client().execute(() -> {
                if (!context.client().level.dimensionTypeRegistration().is(FloraAndFaunaTags.AFFECTED_BY_SEASONS)) {
                    FloraAndFaunaClient.transitionContext = null;
                    return;
                }

                FloraAndFaunaClient.transitionContext = new TransitionContext(Season.values()[context1[0]], Season.values()[context1[1]], context1[2] / 100f);
                FloraAndFaunaClient.transitioning = context1[3] != 0;
                if (FloraAndFaunaClient.SODIUM_LOADED) {
                    SodiumClientCompat.rebuildSeason(context.client());
                } else {
                    if (context.client().level != null && context.client().levelRenderer.viewArea != null && context.client().player != null) {
                        for (SectionRenderDispatcher.RenderSection chunk : Objects.requireNonNull(context.client().levelRenderer.viewArea).sections) {
                            if (chunk == null)
                                continue;
                            chunk.setDirty(true);
                        }
                    }
                }
            });
        });
    }
}
