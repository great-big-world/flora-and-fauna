package dev.creoii.greatbigworld.floraandfauna;

import dev.creoii.greatbigworld.floraandfauna.registry.*;
import dev.creoii.greatbigworld.floraandfauna.season.SeasonManager;
import dev.creoii.greatbigworld.floraandfauna.season.TransitionQuality;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaTags;
import dev.creoii.greatbigworld.mixin.AbstractBlockStateAccessor;
import dev.creoii.greatbigworld.util.EntityBlockCollisionSpliterator;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.registry.TillableBlockRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.item.HoeItem;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;

public class FloraAndFauna implements ModInitializer {
    @Override
    public void onInitialize() {
        FloraAndFaunaBlocks.register();
        FloraAndFaunaItems.register();
        FloraAndFaunaFeatures.register();
        FloraAndFaunaCommands.register();
        FloraAndFaunaGameRules.register();

        PayloadTypeRegistry.playS2C().register(SeasonManager.SyncSeason.PACKET_ID, SeasonManager.SyncSeason.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(SeasonManager.SyncSeasonTransition.PACKET_ID, SeasonManager.SyncSeasonTransition.PACKET_CODEC);
        PayloadTypeRegistry.playC2S().register(TransitionQuality.SyncTransitionQuality.PACKET_ID, TransitionQuality.SyncTransitionQuality.PACKET_CODEC);

        EntityBlockCollisionSpliterator.INTERACTIONS.put(EntityTypeTags.BOAT, context -> !context.state().isOf(Blocks.LILY_PAD));

        ServerPlayNetworking.registerGlobalReceiver(TransitionQuality.SyncTransitionQuality.PACKET_ID, (payload, context) -> {
            int quality = payload.quality();
            context.server().execute(() -> {
                SeasonManager seasonManager = SeasonManager.getInstance(context.server());
                if (seasonManager == null)
                    return;
                seasonManager.setSeasonTransitionQuality(quality);
                context.server().getWorlds().forEach(serverWorld -> {
                    if (serverWorld.getDimensionEntry().isIn(FloraAndFaunaTags.AFFECTED_BY_SEASONS))
                        seasonManager.updateSeasonTime(serverWorld);
                });
            });
        });

        ServerWorldEvents.LOAD.register((server, world) -> {
            if (world.getDimensionEntry().isIn(FloraAndFaunaTags.AFFECTED_BY_SEASONS)) {
                SeasonManager manager = SeasonManager.getInstance(server);
                if (manager != null)
                    manager.load(world);
            }
        });
        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if (world.getDimensionEntry().isIn(FloraAndFaunaTags.AFFECTED_BY_SEASONS) && entity instanceof ServerPlayerEntity serverPlayer) {
                SeasonManager seasonManager = SeasonManager.getInstance(world.getServer());
                if (seasonManager != null) {
                    ServerPlayNetworking.send(serverPlayer, new SeasonManager.SyncSeason((byte) seasonManager.getCurrentSeason().ordinal()));
                    ServerPlayNetworking.send(serverPlayer, new SeasonManager.SyncSeasonTransition(seasonManager.getTransitionContext()));
                }
            }
        });
        ServerTickEvents.END_WORLD_TICK.register(world -> {
            if (world.getDimensionEntry().isIn(FloraAndFaunaTags.AFFECTED_BY_SEASONS) && world.getTickManager().shouldTick()) {
                SeasonManager manager = SeasonManager.getInstance(world.getServer());
                if (manager != null)
                    manager.tick(world);
            }
        });

        Blocks.BROWN_MUSHROOM.getStateManager().getStates().forEach(state -> ((AbstractBlockStateAccessor) state).setLuminance(0));
        Blocks.SCULK_SHRIEKER.getStateManager().getStates().forEach(state -> ((AbstractBlockStateAccessor) state).setLuminance(3));

        TillableBlockRegistry.register(FloraAndFaunaBlocks.HUMUS, HoeItem::canTillFarmland, Blocks.FARMLAND.getDefaultState());
    }
}
