package dev.creoii.greatbigworld.floraandfauna.registry;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.floraandfauna.world.feature.FallenTreeFeature;
import dev.creoii.greatbigworld.floraandfauna.world.feature.FallenTreeFeatureConfig;
import dev.creoii.greatbigworld.floraandfauna.world.feature.FreezeTopLayerFeature;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

public class FloraAndFaunaFeatures {
    public static final Feature<DefaultFeatureConfig> FREEZE_TOP_LAYER = new FreezeTopLayerFeature(DefaultFeatureConfig.CODEC);
    public static final Feature<FallenTreeFeatureConfig> FALLEN_TREE = new FallenTreeFeature(FallenTreeFeatureConfig.CODEC);

    public static void register() {
        Registry.register(Registries.FEATURE, new Identifier(GreatBigWorld.NAMESPACE, "freeze_top_layer"), FREEZE_TOP_LAYER);
        Registry.register(Registries.FEATURE, new Identifier(GreatBigWorld.NAMESPACE, "fallen_tree"), FALLEN_TREE);
    }
}
