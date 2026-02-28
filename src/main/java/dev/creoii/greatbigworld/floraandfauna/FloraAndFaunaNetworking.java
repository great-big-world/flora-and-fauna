package dev.creoii.greatbigworld.floraandfauna;

import dev.creoii.greatbigworld.floraandfauna.season.SeasonManager;
import dev.creoii.greatbigworld.floraandfauna.season.TransitionQuality;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaTags;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public final class FloraAndFaunaNetworking {
    public static void register() {
        PayloadTypeRegistry.playS2C().register(SeasonManager.SyncSeason.PACKET_ID, SeasonManager.SyncSeason.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(SeasonManager.SyncSeasonTransition.PACKET_ID, SeasonManager.SyncSeasonTransition.PACKET_CODEC);
        PayloadTypeRegistry.playC2S().register(TransitionQuality.SyncTransitionQuality.PACKET_ID, TransitionQuality.SyncTransitionQuality.PACKET_CODEC);

        ServerPlayNetworking.registerGlobalReceiver(TransitionQuality.SyncTransitionQuality.PACKET_ID, (payload, context) -> {
            int quality = payload.quality();
            context.server().execute(() -> {
                SeasonManager seasonManager = SeasonManager.getInstance(context.server());
                if (seasonManager == null)
                    return;
                seasonManager.setSeasonTransitionQuality(quality);
                context.server().getAllLevels().forEach(serverWorld -> {
                    if (serverWorld.dimensionTypeRegistration().is(FloraAndFaunaTags.AFFECTED_BY_SEASONS))
                        seasonManager.updateSeasonTime(serverWorld);
                });
            });
        });
    }
}
