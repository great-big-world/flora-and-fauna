package dev.creoii.greatbigworld.floraandfauna.mixin.compat.sodium;

import me.jellysquid.mods.sodium.client.render.SodiumWorldRenderer;
import me.jellysquid.mods.sodium.client.render.chunk.RenderSectionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SodiumWorldRenderer.class)
public interface SodiumWorldRendererAccessor {
    @Accessor(value = "renderSectionManager", remap = false)
    RenderSectionManager gbw$getRenderSectionManager();
}
