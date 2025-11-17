package dev.creoii.greatbigworld.floraandfauna.util;

import dev.creoii.greatbigworld.GreatBigWorld;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;

public final class FloraAndFaunaTags {
    public static final TagKey<Block> IGNORE_SEASON_COLOR = TagKey.of(RegistryKeys.BLOCK, Identifier.of(GreatBigWorld.NAMESPACE, "ignore_season_color"));
    public static final TagKey<Block> HOLLOW_LOGS = TagKey.of(RegistryKeys.BLOCK, Identifier.of(GreatBigWorld.NAMESPACE, "hollow_logs"));
    public static final TagKey<Block> DOES_NOT_SUPPORT_FALLEN_TREES = TagKey.of(RegistryKeys.BLOCK, Identifier.of(GreatBigWorld.NAMESPACE, "does_not_support_fallen_trees"));

    public static final TagKey<Biome> NOT_AFFECTED_BY_AUTUMN = TagKey.of(RegistryKeys.BIOME, Identifier.of(GreatBigWorld.NAMESPACE, "not_affected_by_autumn"));
    public static final TagKey<Biome> NOT_AFFECTED_BY_WINTER = TagKey.of(RegistryKeys.BIOME, Identifier.of(GreatBigWorld.NAMESPACE, "not_affected_by_winter"));
    public static final TagKey<Biome> NOT_AFFECTED_BY_SPRING = TagKey.of(RegistryKeys.BIOME, Identifier.of(GreatBigWorld.NAMESPACE, "not_affected_by_spring"));
    public static final TagKey<Biome> NOT_AFFECTED_BY_SUMMER = TagKey.of(RegistryKeys.BIOME, Identifier.of(GreatBigWorld.NAMESPACE, "not_affected_by_summer"));

    public static final TagKey<EntityType<?>> IGNORES_ALGAE = TagKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(GreatBigWorld.NAMESPACE, "ignores_algae"));

    public static final TagKey<DamageType> ALERTS_ANIMALS = TagKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of(GreatBigWorld.NAMESPACE, "alerts_animals"));

    public static final TagKey<DimensionType> AFFECTED_BY_SEASONS = TagKey.of(RegistryKeys.DIMENSION_TYPE, Identifier.of(GreatBigWorld.NAMESPACE, "affected_by_seasons"));
}
