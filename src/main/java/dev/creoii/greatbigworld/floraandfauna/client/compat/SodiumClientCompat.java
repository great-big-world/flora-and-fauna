package dev.creoii.greatbigworld.floraandfauna.client.compat;

import dev.creoii.greatbigworld.floraandfauna.mixin.client.compat.sodium.RenderSectionManagerAccessor;
import dev.creoii.greatbigworld.floraandfauna.mixin.client.compat.sodium.SodiumWorldRendererAccessor;
import dev.creoii.greatbigworld.floraandfauna.util.RenderSectionManagerInvoker;
import me.jellysquid.mods.sodium.client.render.chunk.ChunkUpdateType;
import me.jellysquid.mods.sodium.client.render.chunk.RenderSection;
import me.jellysquid.mods.sodium.client.render.chunk.RenderSectionManager;
import me.jellysquid.mods.sodium.client.render.chunk.map.ChunkTracker;
import me.jellysquid.mods.sodium.client.render.chunk.map.ChunkTrackerHolder;
import me.jellysquid.mods.sodium.client.world.WorldRendererExtended;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.ChunkSectionPos;

public final class SodiumClientCompat {
    public static void rebuildSeason(MinecraftClient client) {
        if (client.world != null) {
            RenderSectionManager renderSectionManager = ((SodiumWorldRendererAccessor) ((WorldRendererExtended) client.worldRenderer).sodium$getWorldRenderer()).gbw$getRenderSectionManager();
            ChunkTracker.forEachChunk(ChunkTrackerHolder.get(client.world).getReadyChunks(), (x, z) -> {
                for (int y = client.world.getBottomSectionCoord(); y < client.world.getTopSectionCoord(); ++y) {
                    RenderSection renderSection = ((RenderSectionManagerAccessor) renderSectionManager).getSectionByPosition().get(ChunkSectionPos.asLong(x, y, z));
                    if (renderSection != null) {
                        renderSection.setPendingUpdate(ChunkUpdateType.REBUILD);
                        ((RenderSectionManagerInvoker) renderSectionManager).gbw$connectNeighborNodes(renderSection);
                    }
                }
            });
        }
    }
}
