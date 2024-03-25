package dev.creoii.greatbigworld.floraandfauna.client;

import dev.creoii.greatbigworld.floraandfauna.FloraAndFauna;
import dev.creoii.greatbigworld.floraandfauna.client.compat.SodiumClientCompat;
import dev.creoii.greatbigworld.floraandfauna.registry.FloraAndFaunaBlocks;
import dev.creoii.greatbigworld.floraandfauna.registry.FloraAndFaunaEntities;
import dev.creoii.greatbigworld.floraandfauna.registry.FloraAndFaunaItems;
import dev.creoii.greatbigworld.floraandfauna.season.Season;
import dev.creoii.greatbigworld.floraandfauna.season.SeasonManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.chunk.ChunkBuilder;
import org.apache.logging.log4j.Level;
import org.jetbrains.annotations.Nullable;

public class FloraAndFaunaClient implements ClientModInitializer {
    private static final boolean SODIUM_LOADED = FabricLoader.getInstance().isModLoaded("sodium");
    @Nullable private static Season currentSeason;

    @Override
    public void onInitializeClient() {
        FloraAndFaunaBlocks.registerClient();
        FloraAndFaunaItems.registerClient();
        FloraAndFaunaEntities.registerClient();

        ClientPlayNetworking.registerGlobalReceiver(SeasonManager.SYNC_SEASON, (client, handler, buf, responseSender) -> {
            Season season = Season.values()[buf.readInt()];
            client.execute(() -> currentSeason = season);
        });

        if (SODIUM_LOADED) {
            FloraAndFauna.LOGGER.log(Level.INFO, "Sodium detected, modifying season sync color rebuilds.");
            SodiumClientCompat.registerSyncSeasonColorReceiver();
        } else {
            ClientPlayNetworking.registerGlobalReceiver(SeasonManager.SYNC_SEASON_COLOR, (client, handler, buf, responseSender) -> {
                client.execute(() -> updateSeason(client));
            });
        }
    }

    @Nullable
    public static Season getCurrentSeason() {
        return currentSeason;
    }

    public static void updateSeason(MinecraftClient client) {
        if (client.worldRenderer.chunks != null) {
            for (ChunkBuilder.BuiltChunk chunk : client.worldRenderer.chunks.chunks) {
                if (chunk == null)
                    continue;
                chunk.scheduleRebuild(true);
            }
        }
    }
}
