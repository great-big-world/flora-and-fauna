package dev.creoii.greatbigworld.floraandfauna.client;

import dev.creoii.greatbigworld.floraandfauna.FloraAndFauna;
import dev.creoii.greatbigworld.floraandfauna.client.compat.SodiumClientCompat;
import dev.creoii.greatbigworld.floraandfauna.registry.FloraAndFaunaBlocks;
import dev.creoii.greatbigworld.floraandfauna.registry.FloraAndFaunaItems;
import dev.creoii.greatbigworld.floraandfauna.season.Season;
import dev.creoii.greatbigworld.floraandfauna.season.SeasonManager;
import dev.creoii.greatbigworld.floraandfauna.season.TransitionContext;
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
    @Nullable private static TransitionContext transitionContext;

    @Override
    public void onInitializeClient() {
        if (SODIUM_LOADED)
            FloraAndFauna.LOGGER.log(Level.INFO, "Sodium detected, modifying season sync color rebuilds.");

        FloraAndFaunaBlocks.registerClient();
        FloraAndFaunaItems.registerClient();

        ClientPlayNetworking.registerGlobalReceiver(SeasonManager.SyncSeason.PACKET_ID, (payload, context) -> {
            int season = payload.season();
            context.client().execute(() -> {
                currentSeason = Season.values()[season];
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(SeasonManager.SyncSeasonColor.PACKET_ID, (payload, context) -> {
            int[] context1 = payload.context();
            context.client().execute(() -> {
                transitionContext = new TransitionContext(Season.values()[context1[0]], Season.values()[context1[1]], context1[2] / 100f);
                if (SODIUM_LOADED) {
                    SodiumClientCompat.rebuildSeason(context.client());
                } else rebuildSeason(context.client());
            });
        });
    }

    public static @Nullable Season getCurrentSeason() {
        return currentSeason;
    }

    public static @Nullable TransitionContext getTransitionContext() {
        return transitionContext;
    }

    public static void rebuildSeason(MinecraftClient client) {
        if (client.world != null && client.worldRenderer.chunks != null && client.player != null) {
            for (ChunkBuilder.BuiltChunk chunk : client.worldRenderer.chunks.chunks) {
                if (chunk == null)
                    continue;
                chunk.scheduleRebuild(true);
            }
        }
    }

    public static int getSeasonColor(BlockRenderView world, BlockPos pos, int color) {
        if (world == null || transitionContext == null)
            return color;

        RegistryEntry<Biome> biomeEntry = world.getBiomeFabric(pos);
        if (biomeEntry == null || !biomeEntry.hasKeyAndValue()) {
            return color;
        }

        Season.Context context = new Season.Context(world, pos, color);
        return ColorHelper.interpolate(transitionContext.getPercentage(), transitionContext.getCurrent().getColorChange().apply(context), transitionContext.getNext() == null ? transitionContext.getCurrent().getColorChange().apply(context) : transitionContext.getNext().getColorChange().apply(context));
    }
}
