package dev.creoii.greatbigworld.floraandfauna.season;

import dev.creoii.greatbigworld.floraandfauna.FloraAndFauna;
import dev.creoii.greatbigworld.floraandfauna.registry.FloraAndFaunaGameRules;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.*;
import org.jetbrains.annotations.Nullable;

public class SeasonManager extends PersistentState {
    private static final Type<SeasonManager> STATE_TYPE = new Type<>(SeasonManager::new, SeasonManager::createFromNbt, null);
    private static SeasonManager instance;
    private static final int SEASON_TRANSITION_COUNT = 30;
    @Nullable
    private MinecraftServer server = null;
    private Season currentSeason = Season.SUMMER;
    private TransitionContext context = new TransitionContext(Season.SUMMER, Season.AUTUMN, 0f);
    private int seasonLength;
    private int seasonTransitionLength;
    private int seasonTransitionIncrement;
    private float seasonTransitionIncrementAmount;
    private int seasonTime = -1;
    private boolean transitioning = false;

    public static SeasonManager getInstance(MinecraftServer server) {
        return instance == null ? instance = getServerState(server) : instance;
    }

    public void setCurrentSeason(ServerWorld world, Season season, boolean preserveTime) {
        currentSeason = season;
        if (!preserveTime) {
            seasonTime = 0;
        }
        context = new TransitionContext(currentSeason, Season.getNextSeason(currentSeason), 0f);
        load(world);
    }

    public Season getCurrentSeason() {
        return currentSeason;
    }

    public TransitionContext getTransitionContext() {
        return context;
    }

    public void load(ServerWorld world) {
        server = world.getServer();
        updateSeasonTime(world);
        syncAll(server);
    }

    public void tick(ServerWorld world) {
        if (server == null)
            server = world.getServer();
        if (instance == null) {
            instance = getServerState(server);

            if (currentSeason == null)
                currentSeason = Season.SUMMER;
            if (seasonTime == -1)
                seasonTime = 0;
            if (context == null)
                context = new TransitionContext(Season.SUMMER, Season.AUTUMN, 0f);

            load(world);
        }

        if (world.getGameRules().getBoolean(FloraAndFaunaGameRules.DO_SEASON_CYCLE)) {
            if (seasonLength != world.getGameRules().getInt(FloraAndFaunaGameRules.SEASON_LENGTH))
                updateSeasonTime(world);

            // check for SEASON_TRANSITION_COUNT / 2 before transition occurs
            if (++seasonTime > seasonLength - (seasonTransitionLength / 2) || seasonTime <= (seasonTransitionLength / 2)) {
                // begin transition
                transitioning = true;
            } else if (transitioning) {
                // end transition
                context.setSeason(currentSeason, Season.getNextSeason(currentSeason));
                context.setPercentage(0f);
                transitioning = false;
                syncSeasonTransition(server);
            }

            if (seasonTime >= seasonLength) {
                seasonTime = 0;
                currentSeason = Season.getNextSeason(currentSeason);
                syncSeason(server);
            }

            // check for % of every increment
            if (transitioning && seasonTime % seasonTransitionIncrement == 0) {
                // percentage is set at a flat rate 30 times per transition, rather than relative to a time
                context.setPercentage(Math.min(1f, context.getPercentage() + seasonTransitionIncrementAmount));
                syncSeasonTransition(server);
            }
        }
    }

    public void addSeasonTime(int seasonTime) {
        if (server != null && !transitioning) {
            this.seasonTime = Math.min(this.seasonTime + seasonTime, seasonLength - (seasonTransitionLength / 2));
            syncAll(server);
        }
    }

    private void updateSeasonTime(ServerWorld world) {
        seasonLength = world.getGameRules().getInt(FloraAndFaunaGameRules.SEASON_LENGTH);
        seasonTransitionLength = seasonLength / 3;
        seasonTransitionIncrement = seasonTransitionLength / SEASON_TRANSITION_COUNT;
        seasonTransitionIncrementAmount = (float) seasonTransitionIncrement / seasonTransitionLength;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        nbt.putInt("season", currentSeason.ordinal());
        nbt.putInt("season_time", seasonTime);
        nbt.putBoolean("transitioning", transitioning);
        nbt.put("context", context.writeNbt());
        return nbt;
    }

    private static SeasonManager createFromNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup lookup) {
        SeasonManager manager = new SeasonManager();
        manager.currentSeason = Season.values()[nbt.getInt("season")];
        manager.seasonTime = nbt.getInt("season_time");
        manager.transitioning = nbt.getBoolean("transitioning");
        manager.context = TransitionContext.readNbt(nbt.getCompound("context"));
        return manager;
    }

    public void syncAll(MinecraftServer server) {
        PlayerLookup.all(server).forEach(serverPlayer -> {
            ServerPlayNetworking.send(serverPlayer, new SyncSeason(currentSeason.ordinal()));
            ServerPlayNetworking.send(serverPlayer, new SyncSeasonTransition(context));
        });
    }

    private void syncSeason(MinecraftServer server) {
        PlayerLookup.all(server).forEach(serverPlayer -> {
            ServerPlayNetworking.send(serverPlayer, new SyncSeason(currentSeason.ordinal()));
        });
    }

    private void syncSeasonTransition(MinecraftServer server) {
        PlayerLookup.all(server).forEach(serverPlayer -> {
            ServerPlayNetworking.send(serverPlayer, new SyncSeasonTransition(context));
        });
    }

    private static SeasonManager getServerState(MinecraftServer server) {
        SeasonManager manager = server.getWorld(World.OVERWORLD).getPersistentStateManager().getOrCreate(STATE_TYPE, FloraAndFauna.NAMESPACE);
        manager.markDirty();
        return manager;
    }

    public record SyncSeason(int season) implements CustomPayload {
        public static final CustomPayload.Id<SyncSeason> PACKET_ID = new CustomPayload.Id<>(new Identifier(FloraAndFauna.NAMESPACE, "sync_season"));
        public static final PacketCodec<RegistryByteBuf, SyncSeason> PACKET_CODEC = PacketCodec.of(SyncSeason::write, SyncSeason::new);

        public SyncSeason(RegistryByteBuf buf) {
            this(buf.readVarInt());
        }

        public void write(RegistryByteBuf buf) {
            buf.writeVarInt(season);
        }

        @Override
        public Id<? extends CustomPayload> getId() {
            return PACKET_ID;
        }
    }

    public record SyncSeasonTransition(int[] context) implements CustomPayload {
        public static final CustomPayload.Id<SyncSeasonTransition> PACKET_ID = new CustomPayload.Id<>(new Identifier(FloraAndFauna.NAMESPACE, "sync_season_color"));
        public static final PacketCodec<RegistryByteBuf, SyncSeasonTransition> PACKET_CODEC = PacketCodec.of(SyncSeasonTransition::write, SyncSeasonTransition::new);

        public SyncSeasonTransition(TransitionContext context) {
            this(new int[]{context.getCurrent().ordinal(), context.getNext() == null ? context.getCurrent().ordinal() : context.getNext().ordinal(), (int) (context.getPercentage() * 100)});
        }

        public SyncSeasonTransition(RegistryByteBuf buf) {
            this(buf.readIntArray());
        }

        public void write(RegistryByteBuf buf) {
            buf.writeIntArray(context);
        }

        @Override
        public Id<? extends CustomPayload> getId() {
            return PACKET_ID;
        }
    }
}
