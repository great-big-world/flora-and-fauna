package dev.creoii.greatbigworld.floraandfauna.util;

import dev.creoii.greatbigworld.floraandfauna.FloraAndFauna;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

public final class FloraAndFaunaTags {
    public static final TagKey<Block> IGNORE_SEASON_COLOR = TagKey.of(RegistryKeys.BLOCK, new Identifier(FloraAndFauna.NAMESPACE, "ignore_season_color"));
    public static final TagKey<Block> HOLLOW_LOGS = TagKey.of(RegistryKeys.BLOCK, new Identifier(FloraAndFauna.NAMESPACE, "hollow_logs"));
    public static final TagKey<Block> DOES_NOT_SUPPORT_FALLEN_TREES = TagKey.of(RegistryKeys.BLOCK, new Identifier(FloraAndFauna.NAMESPACE, "does_not_support_fallen_trees"));

    public static final TagKey<Biome> NOT_AFFECTED_BY_AUTUMN = TagKey.of(RegistryKeys.BIOME, new Identifier(FloraAndFauna.NAMESPACE, "not_affected_by_autumn"));
    public static final TagKey<Biome> NOT_AFFECTED_BY_WINTER = TagKey.of(RegistryKeys.BIOME, new Identifier(FloraAndFauna.NAMESPACE, "not_affected_by_winter"));
    public static final TagKey<Biome> NOT_AFFECTED_BY_SPRING = TagKey.of(RegistryKeys.BIOME, new Identifier(FloraAndFauna.NAMESPACE, "not_affected_by_spring"));
    public static final TagKey<Biome> NOT_AFFECTED_BY_SUMMER = TagKey.of(RegistryKeys.BIOME, new Identifier(FloraAndFauna.NAMESPACE, "not_affected_by_summer"));
    public static final TagKey<Biome> DUCK_SPAWN_BIOMES = TagKey.of(RegistryKeys.BIOME, new Identifier(FloraAndFauna.NAMESPACE, "duck_spawn_biomes"));

    public static final TagKey<EntityType<?>> IGNORES_ALGAE = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier(FloraAndFauna.NAMESPACE, "ignores_algae"));

    public static final TagKey<DamageType> ALERTS_ANIMALS = TagKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(FloraAndFauna.NAMESPACE, "alerts_animals"));
}
