package dev.creoii.greatbigworld.floraandfauna.client.compat;

import dev.creoii.greatbigworld.floraandfauna.mixin.client.compat.sodium.RenderSectionManagerAccessor;
import dev.creoii.greatbigworld.floraandfauna.mixin.client.compat.sodium.SodiumWorldRendererAccessor;
import dev.creoii.greatbigworld.floraandfauna.util.RenderSectionManagerInvoker;
import net.caffeinemc.mods.sodium.client.render.chunk.RenderSection;
import net.caffeinemc.mods.sodium.client.render.chunk.RenderSectionManager;
import net.caffeinemc.mods.sodium.client.render.chunk.map.ChunkTracker;
import net.caffeinemc.mods.sodium.client.render.chunk.map.ChunkTrackerHolder;
import net.caffeinemc.mods.sodium.client.world.LevelRendererExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.core.SectionPos;

public final class SodiumClientCompat {
    public static void rebuildSeason(Minecraft client) {
        if (client.level != null) {
            RenderSectionManager renderSectionManager = ((SodiumWorldRendererAccessor) ((LevelRendererExtension) client.levelRenderer).sodium$getWorldRenderer()).gbw$getRenderSectionManager();
            ChunkTracker.forEachChunk(ChunkTrackerHolder.get(client.level).getReadyChunks(), (x, z) -> {
                for (int y = client.level.getMinSectionY(); y < client.level.getMaxSectionY(); ++y) {
                    RenderSection renderSection = ((RenderSectionManagerAccessor) renderSectionManager).getSectionByPosition().get(SectionPos.asLong(x, y, z));
                    if (renderSection != null) {
                        renderSection.setPendingUpdate(2, System.nanoTime());
                        ((RenderSectionManagerInvoker) renderSectionManager).gbw$connectNeighborNodes(renderSection);
                    }
                }
            });
        }
    }
}
