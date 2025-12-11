package dev.creoii.greatbigworld.floraandfauna.util;

import dev.creoii.greatbigworld.GreatBigWorld;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.dimension.DimensionType;

public final class FloraAndFaunaTags {
    public static final TagKey<Block> IGNORE_SEASON_COLOR = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "ignore_season_color"));
    public static final TagKey<Block> HOLLOW_LOGS = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "hollow_logs"));
    public static final TagKey<Block> DOES_NOT_SUPPORT_FALLEN_TREES = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "does_not_support_fallen_trees"));

    public static final TagKey<Biome> NOT_AFFECTED_BY_AUTUMN = TagKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "not_affected_by_autumn"));
    public static final TagKey<Biome> NOT_AFFECTED_BY_WINTER = TagKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "not_affected_by_winter"));
    public static final TagKey<Biome> NOT_AFFECTED_BY_SPRING = TagKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "not_affected_by_spring"));
    public static final TagKey<Biome> NOT_AFFECTED_BY_SUMMER = TagKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "not_affected_by_summer"));

    public static final TagKey<EntityType<?>> IGNORES_ALGAE = TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "ignores_algae"));

    public static final TagKey<DamageType> ALERTS_ANIMALS = TagKey.create(Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "alerts_animals"));

    public static final TagKey<DimensionType> AFFECTED_BY_SEASONS = TagKey.create(Registries.DIMENSION_TYPE, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "affected_by_seasons"));
}
