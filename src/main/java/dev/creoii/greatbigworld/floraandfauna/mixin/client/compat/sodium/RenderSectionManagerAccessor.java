package dev.creoii.greatbigworld.floraandfauna.mixin.client.compat.sodium;

import it.unimi.dsi.fastutil.longs.Long2ReferenceMap;
import net.caffeinemc.mods.sodium.client.render.chunk.RenderSection;
import net.caffeinemc.mods.sodium.client.render.chunk.RenderSectionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RenderSectionManager.class)
public interface RenderSectionManagerAccessor {
    @Accessor(value = "sectionByPosition", remap = false)
    Long2ReferenceMap<RenderSection> getSectionByPosition();
}
