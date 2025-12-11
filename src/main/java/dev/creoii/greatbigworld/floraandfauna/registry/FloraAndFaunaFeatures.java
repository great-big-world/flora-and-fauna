package dev.creoii.greatbigworld.floraandfauna.registry;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.floraandfauna.world.feature.FallenTreeFeature;
import dev.creoii.greatbigworld.floraandfauna.world.feature.FallenTreeFeatureConfig;
import dev.creoii.greatbigworld.floraandfauna.world.feature.FreezeTopLayerFeature;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class FloraAndFaunaFeatures {
    public static final Feature<NoneFeatureConfiguration> FREEZE_TOP_LAYER = new FreezeTopLayerFeature(NoneFeatureConfiguration.CODEC);
    public static final Feature<FallenTreeFeatureConfig> FALLEN_TREE = new FallenTreeFeature(FallenTreeFeatureConfig.CODEC);

    public static void register() {
        Registry.register(BuiltInRegistries.FEATURE, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "freeze_top_layer"), FREEZE_TOP_LAYER);
        Registry.register(BuiltInRegistries.FEATURE, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "fallen_tree"), FALLEN_TREE);
    }
}
