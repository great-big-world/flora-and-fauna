package dev.creoii.greatbigworld.floraandfauna.registry;

import dev.creoii.greatbigworld.floraandfauna.season.SeasonManager;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaTags;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;

public final class FloraAndFaunaEvents {
    public static void register() {
        ServerWorldEvents.LOAD.register((server, world) -> {
            if (world.dimensionTypeRegistration().is(FloraAndFaunaTags.AFFECTED_BY_SEASONS)) {
                SeasonManager manager = SeasonManager.getInstance(server);
                if (manager != null)
                    manager.load(world);
            }
        });
        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if (world.dimensionTypeRegistration().is(FloraAndFaunaTags.AFFECTED_BY_SEASONS) && entity instanceof ServerPlayer serverPlayer) {
                SeasonManager seasonManager = SeasonManager.getInstance(world.getServer());
                if (seasonManager != null) {
                    ServerPlayNetworking.send(serverPlayer, new SeasonManager.SyncSeason((byte) seasonManager.getCurrentSeason().ordinal()));
                    ServerPlayNetworking.send(serverPlayer, new SeasonManager.SyncSeasonTransition(seasonManager, seasonManager.getTransitionContext()));
                }
            }
        });
        ServerTickEvents.END_WORLD_TICK.register(world -> {
            if (world.dimensionTypeRegistration().is(FloraAndFaunaTags.AFFECTED_BY_SEASONS) && world.tickRateManager().runsNormally()) {
                SeasonManager manager = SeasonManager.getInstance(world.getServer());
                if (manager != null)
                    manager.tick(world);
            }
        });
    }
}
