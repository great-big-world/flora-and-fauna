package dev.creoii.greatbigworld.floraandfauna.client.compat;

import dev.creoii.greatbigworld.floraandfauna.season.SeasonManager;
import dev.creoii.greatbigworld.floraandfauna.mixin.compat.sodium.RenderSectionManagerAccessor;
import dev.creoii.greatbigworld.floraandfauna.mixin.compat.sodium.SodiumWorldRendererAccessor;
import dev.creoii.greatbigworld.floraandfauna.util.RenderSectionManagerInvoker;
import me.jellysquid.mods.sodium.client.render.chunk.ChunkUpdateType;
import me.jellysquid.mods.sodium.client.render.chunk.RenderSection;
import me.jellysquid.mods.sodium.client.render.chunk.RenderSectionManager;
import me.jellysquid.mods.sodium.client.render.chunk.map.ChunkTracker;
import me.jellysquid.mods.sodium.client.render.chunk.map.ChunkTrackerHolder;
import me.jellysquid.mods.sodium.client.world.WorldRendererExtended;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.ChunkSectionPos;

public final class SodiumClientCompat {
    public static void registerSyncSeasonColorReceiver() {
        ClientPlayNetworking.registerGlobalReceiver(SeasonManager.SYNC_SEASON_COLOR, (client, handler, buf, responseSender) -> {
            client.execute(() -> updateSeason(client));
        });
    }

    public static void updateSeason(MinecraftClient client) {
        if (client.world != null) {
            RenderSectionManager renderSectionManager = ((SodiumWorldRendererAccessor) ((WorldRendererExtended) client.worldRenderer).sodium$getWorldRenderer()).gbw$getRenderSectionManager();
            RenderSectionManagerAccessor accessor = (RenderSectionManagerAccessor) renderSectionManager;
            RenderSectionManagerInvoker invoker = (RenderSectionManagerInvoker) renderSectionManager;

            ChunkTracker.forEachChunk(ChunkTrackerHolder.get(client.world).getReadyChunks(), (x, z) -> {
                for (int y = client.world.getBottomSectionCoord(); y < client.world.getTopSectionCoord(); ++y) {
                    RenderSection renderSection = accessor.getSectionByPosition().get(ChunkSectionPos.asLong(x, y, z));
                    if (renderSection != null) {
                        renderSection.setPendingUpdate(ChunkUpdateType.REBUILD);
                        invoker.gbw$connectNeighborNodes(renderSection);
                    }
                }
            });
        }
    }
}
