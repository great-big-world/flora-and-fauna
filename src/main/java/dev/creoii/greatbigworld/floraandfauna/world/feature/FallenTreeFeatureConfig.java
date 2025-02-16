package dev.creoii.greatbigworld.floraandfauna.world.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

import java.util.Optional;

public record FallenTreeFeatureConfig(BlockStateProvider state, Optional<BlockStateProvider> leafState, IntProvider length, Optional<IntProvider> leafChance) implements FeatureConfig {
    public static final Codec<FallenTreeFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(BlockStateProvider.TYPE_CODEC.fieldOf("state").forGetter(config -> {
            return config.state;
        }), BlockStateProvider.TYPE_CODEC.optionalFieldOf("leaf_state").orElse(Optional.empty()).forGetter(config -> {
            return Optional.ofNullable(config.state);
        }), IntProvider.createValidatingCodec(1, 16).fieldOf("length").forGetter(config -> {
            return config.length;
        }), IntProvider.createValidatingCodec(0, 100).optionalFieldOf("leaf_chance").orElse(Optional.ofNullable(ConstantIntProvider.create(0))).forGetter(config -> {
            return config.leafChance;
        })).apply(instance, FallenTreeFeatureConfig::new);
    });
}
