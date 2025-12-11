package dev.creoii.greatbigworld.floraandfauna.registry;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.floraandfauna.FloraAndFauna;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

public final class FloraAndFaunaBiomes {
    public static final ResourceKey<Biome> COLD_RIVER = ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "cold_river"));
    public static final ResourceKey<Biome> DESERT_RIVER = ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "desert_river"));
    public static final ResourceKey<Biome> JUNGLE_RIVER = ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "jungle_river"));
    public static final ResourceKey<Biome> SWAMP_RIVER = ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "swamp_river"));
}
