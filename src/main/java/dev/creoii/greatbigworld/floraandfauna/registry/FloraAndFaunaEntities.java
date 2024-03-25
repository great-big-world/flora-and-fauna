package dev.creoii.greatbigworld.floraandfauna.registry;

import dev.creoii.greatbigworld.floraandfauna.FloraAndFauna;
import dev.creoii.greatbigworld.floraandfauna.client.render.DuckEntityRenderer;
import dev.creoii.greatbigworld.floraandfauna.entity.DuckEntity;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaEntityModelLayers;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaTags;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class FloraAndFaunaEntities {
    public static final EntityType<DuckEntity> DUCK = FabricEntityTypeBuilder.create(SpawnGroup.CREATURE).dimensions(EntityDimensions.changing(.4f, .85f)).trackRangeChunks(10).entityFactory(DuckEntity::new).build();

    public static void register() {
        Registry.register(Registries.ENTITY_TYPE, new Identifier(FloraAndFauna.NAMESPACE, "duck"), DUCK);
        registerAttributes();
        registerSpawns();
    }

    private static void registerAttributes() {
        FabricDefaultAttributeRegistry.register(DUCK, DuckEntity.createDuckAttributes());
    }

    private static void registerSpawns() {
        BiomeModifications.addSpawn(BiomeSelectors.tag(FloraAndFaunaTags.DUCK_SPAWN_BIOMES), SpawnGroup.CREATURE, DUCK, 1, 2, 5);
    }

    @Environment(EnvType.CLIENT)
    public static void registerClient() {
        EntityRendererRegistry.register(DUCK, DuckEntityRenderer::new);
        FloraAndFaunaEntityModelLayers.register();
    }
}
