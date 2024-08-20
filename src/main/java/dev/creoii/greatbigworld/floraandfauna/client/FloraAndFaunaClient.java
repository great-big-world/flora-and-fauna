package dev.creoii.greatbigworld.floraandfauna.client;

import dev.creoii.greatbigworld.floraandfauna.FloraAndFauna;
import dev.creoii.greatbigworld.floraandfauna.client.compat.SodiumClientCompat;
import dev.creoii.greatbigworld.floraandfauna.registry.FloraAndFaunaBlocks;
import dev.creoii.greatbigworld.floraandfauna.registry.FloraAndFaunaEntities;
import dev.creoii.greatbigworld.floraandfauna.registry.FloraAndFaunaGameRules;
import dev.creoii.greatbigworld.floraandfauna.registry.FloraAndFaunaItems;
import dev.creoii.greatbigworld.floraandfauna.season.Season;
import dev.creoii.greatbigworld.floraandfauna.season.SeasonManager;
import dev.creoii.greatbigworld.floraandfauna.util.ColorHelper;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.chunk.ChunkBuilder;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.biome.Biome;
import org.apache.logging.log4j.Level;
import org.jetbrains.annotations.Nullable;

public class FloraAndFaunaClient implements ClientModInitializer {
    private static final boolean SODIUM_LOADED = FabricLoader.getInstance().isModLoaded("sodium");
    @Nullable private static Season currentSeason;
    private static int seasonColorTime;
    private static int syncSeasonTime;

    @Override
    public void onInitializeClient() {
        FloraAndFaunaBlocks.registerClient();
        FloraAndFaunaItems.registerClient();
        FloraAndFaunaEntities.registerClient();

        ClientPlayNetworking.registerGlobalReceiver(SeasonManager.SyncSeason.PACKET_ID, (payload, context) -> {
            Season season = Season.values()[payload.season()];
            int colorTime = payload.colorTime();
            int syncTime = payload.syncSeasonTime();
            context.client().execute(() -> {
                currentSeason = season;
                seasonColorTime = colorTime;
                syncSeasonTime = syncTime;
            });
        });

        if (SODIUM_LOADED) {
            FloraAndFauna.LOGGER.log(Level.INFO, "Sodium detected, modifying season sync color rebuilds.");
            SodiumClientCompat.registerSyncSeasonColorReceiver();
        } else {
            ClientPlayNetworking.registerGlobalReceiver(SeasonManager.SyncSeasonColor.PACKET_ID, (payload, context) -> {
                context.client().execute(() -> updateSeason(context.client()));
            });
        }
    }

    @Nullable
    public static Season getCurrentSeason() {
        return currentSeason;
    }

    public static void updateSeason(MinecraftClient client) {
        if (client.world != null && client.worldRenderer.chunks != null && client.player != null) {
            for (ChunkBuilder.BuiltChunk chunk : client.worldRenderer.chunks.chunks) {
                if (chunk == null)
                    continue;
                chunk.scheduleRebuild(true);
            }
        }
    }

    public static int getSeasonColor(BlockRenderView world, BlockPos pos, int color) {
        if (world == null || currentSeason == null)
            return color;

        RegistryEntry<Biome> biomeEntry = world.getBiomeFabric(pos);
        if (biomeEntry == null || !biomeEntry.hasKeyAndValue()) {
            return color;
        }

        Season.Context context = new Season.Context(world, pos, color);
        return ColorHelper.interpolate(getSeasonColorPercentage(), currentSeason.getColorChange().apply(context), SeasonManager.getNextSeason(currentSeason).getColorChange().apply(context));
    }

    private static float getSeasonColorPercentage() {
        if (MinecraftClient.getInstance().world == null || MinecraftClient.getInstance().player == null) {
            return 1f;
        }
        return (float) seasonColorTime / syncSeasonTime;
    }
}
