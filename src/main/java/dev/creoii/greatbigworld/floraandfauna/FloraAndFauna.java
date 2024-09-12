package dev.creoii.greatbigworld.floraandfauna;

import dev.creoii.creoapi.api.modification.BlockModification;
import dev.creoii.greatbigworld.floraandfauna.registry.*;
import dev.creoii.greatbigworld.floraandfauna.season.SeasonManager;
import dev.creoii.greatbigworld.floraandfauna.season.TransitionQuality;
import dev.creoii.greatbigworld.floraandfauna.world.feature.FallenTreeFeature;
import dev.creoii.greatbigworld.floraandfauna.world.feature.FallenTreeFeatureConfig;
import dev.creoii.greatbigworld.floraandfauna.world.feature.FreezeTopLayerFeature;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.dimension.DimensionTypes;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FloraAndFauna implements ModInitializer {
    public static final String NAMESPACE = "great_big_world";
    public static final Logger LOGGER = LogManager.getLogger(FloraAndFauna.class);
    public static final Feature<DefaultFeatureConfig> FREEZE_TOP_LAYER = new FreezeTopLayerFeature(DefaultFeatureConfig.CODEC);
    public static final Feature<FallenTreeFeatureConfig> FALLEN_TREE = new FallenTreeFeature(FallenTreeFeatureConfig.CODEC);

    @Override
    public void onInitialize() {
        FloraAndFaunaBlocks.register();
        FloraAndFaunaItems.register();
        FloraAndFaunaTreeDecoratorTypes.register();
        FloraAndFaunaCommands.register();
        FloraAndFaunaGameRules.register();
        Registry.register(Registries.FEATURE, new Identifier(NAMESPACE, "freeze_top_layer"), FREEZE_TOP_LAYER);
        Registry.register(Registries.FEATURE, new Identifier(NAMESPACE, "fallen_tree"), FALLEN_TREE);

        PayloadTypeRegistry.playS2C().register(SeasonManager.SyncSeason.PACKET_ID, SeasonManager.SyncSeason.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(SeasonManager.SyncSeasonTransition.PACKET_ID, SeasonManager.SyncSeasonTransition.PACKET_CODEC);
        PayloadTypeRegistry.playC2S().register(TransitionQuality.SyncTransitionQuality.PACKET_ID, TransitionQuality.SyncTransitionQuality.PACKET_CODEC);

        ServerPlayNetworking.registerGlobalReceiver(TransitionQuality.SyncTransitionQuality.PACKET_ID, (payload, context) -> {
            int quality = payload.quality();
            context.server().execute(() -> {
                SeasonManager seasonManager = SeasonManager.getInstance(context.server());
                seasonManager.setSeasonTransitionQuality(quality);
                seasonManager.updateSeasonTime(context.server().getOverworld());
            });
        });

        ServerWorldEvents.LOAD.register((server, world) -> {
            if (world.getDimensionEntry().matches(key -> key == DimensionTypes.OVERWORLD)) {
                SeasonManager.getInstance(server).load(world);
            }
        });
        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if (entity instanceof ServerPlayerEntity serverPlayer) {
                SeasonManager seasonManager = SeasonManager.getInstance(world.getServer());
                ServerPlayNetworking.send(serverPlayer, new SeasonManager.SyncSeason((byte) seasonManager.getCurrentSeason().ordinal()));
                ServerPlayNetworking.send(serverPlayer, new SeasonManager.SyncSeasonTransition(seasonManager.getTransitionContext()));
            }
        });
        ServerTickEvents.END_WORLD_TICK.register(world -> {
            if (world.getDimensionEntry().matches(key -> key == DimensionTypes.OVERWORLD) && world.getTickManager().shouldTick()) {
                SeasonManager.getInstance(world.getServer()).tick(world);
            }
        });

        BlockModification.INSTANCE.setLuminance(Blocks.BROWN_MUSHROOM, 0);
    }
}
