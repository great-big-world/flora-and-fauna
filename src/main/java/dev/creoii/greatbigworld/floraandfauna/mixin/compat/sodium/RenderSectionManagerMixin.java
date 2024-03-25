package dev.creoii.greatbigworld.floraandfauna.mixin.compat.sodium;

import dev.creoii.greatbigworld.floraandfauna.util.RenderSectionManagerInvoker;
import me.jellysquid.mods.sodium.client.render.chunk.RenderSection;
import me.jellysquid.mods.sodium.client.render.chunk.RenderSectionManager;
import me.jellysquid.mods.sodium.client.render.chunk.occlusion.GraphDirection;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(RenderSectionManager.class)
public abstract class RenderSectionManagerMixin implements RenderSectionManagerInvoker {
    @Shadow(remap = false) protected abstract RenderSection getRenderSection(int x, int y, int z);

    @Override
    public void gbw$connectNeighborNodes(RenderSection render) {
        for(int direction = 0; direction < Direction.values().length; ++direction) {
            RenderSection adjacent = getRenderSection(render.getChunkX() + GraphDirection.x(direction), render.getChunkY() + GraphDirection.y(direction), render.getChunkZ() + GraphDirection.z(direction));
            if (adjacent != null) {
                adjacent.setAdjacentNode(GraphDirection.opposite(direction), render);
                render.setAdjacentNode(direction, adjacent);
            }
        }
    }
}
