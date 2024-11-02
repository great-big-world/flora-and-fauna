package dev.creoii.greatbigworld.floraandfauna.registry;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.floraandfauna.FloraAndFauna;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

public final class FloraAndFaunaBiomes {
    public static final RegistryKey<Biome> COLD_RIVER = RegistryKey.of(RegistryKeys.BIOME, new Identifier(GreatBigWorld.NAMESPACE, "cold_river"));
    public static final RegistryKey<Biome> DESERT_RIVER = RegistryKey.of(RegistryKeys.BIOME, new Identifier(GreatBigWorld.NAMESPACE, "desert_river"));
    public static final RegistryKey<Biome> JUNGLE_RIVER = RegistryKey.of(RegistryKeys.BIOME, new Identifier(GreatBigWorld.NAMESPACE, "jungle_river"));
    public static final RegistryKey<Biome> SWAMP_RIVER = RegistryKey.of(RegistryKeys.BIOME, new Identifier(GreatBigWorld.NAMESPACE, "swamp_river"));
}
