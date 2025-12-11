package dev.creoii.greatbigworld.floraandfauna.world.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public record FallenTreeFeatureConfig(BlockStateProvider state, Optional<BlockStateProvider> leafState, IntProvider length, Optional<IntProvider> leafChance) implements FeatureConfiguration {
    public static final Codec<FallenTreeFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(BlockStateProvider.CODEC.fieldOf("state").forGetter(config -> {
            return config.state;
        }), BlockStateProvider.CODEC.optionalFieldOf("leaf_state").orElse(Optional.empty()).forGetter(config -> {
            return Optional.ofNullable(config.state);
        }), IntProvider.codec(1, 16).fieldOf("length").forGetter(config -> {
            return config.length;
        }), IntProvider.codec(0, 100).optionalFieldOf("leaf_chance").orElse(Optional.ofNullable(ConstantInt.of(0))).forGetter(config -> {
            return config.leafChance;
        })).apply(instance, FallenTreeFeatureConfig::new);
    });
}
