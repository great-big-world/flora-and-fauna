package dev.creoii.greatbigworld.floraandfauna.season;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.floraandfauna.registry.FloraAndFaunaGameRules;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.*;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import org.jetbrains.annotations.Nullable;

public class SeasonManager extends SavedData {
    public static final Codec<SeasonManager> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(Codec.INT.optionalFieldOf("season", 3).forGetter(manager -> {
            return manager.currentSeason.ordinal();
        }), Codec.INT.optionalFieldOf("season_time", 0).forGetter(manager -> {
            return manager.seasonTime;
        }), Codec.BOOL.optionalFieldOf("transitioning", false).forGetter(manager -> {
            return manager.transitioning;
        }), TransitionContext.CODEC.fieldOf("context").forGetter(manager -> {
            return manager.context;
        })).apply(instance, (season, seasonTime, transitioning, context) -> {
            SeasonManager manager = new SeasonManager();
            manager.currentSeason = Season.values()[season];
            manager.seasonTime = seasonTime;
            manager.transitioning = transitioning;
            manager.context = context;
            return manager;
        });
    });

    private static final SavedDataType<SeasonManager> STATE_TYPE = new SavedDataType<>("gbw_seasons", SeasonManager::new, CODEC, null);
    private static SeasonManager instance;
    @Nullable
    private MinecraftServer server = null;
    protected Season currentSeason = Season.SUMMER;
    private TransitionContext context = new TransitionContext(Season.SUMMER, Season.AUTUMN, 0f);
    private int seasonTransitionQuality = 25;
    private int seasonLength;
    private int seasonTransitionLength;
    private int seasonTransitionIncrement;
    private float seasonTransitionIncrementAmount;
    private int seasonTime = -1;
    private boolean transitioning = false;

    public static SeasonManager getInstance(MinecraftServer server) {
        return instance == null ? instance = getServerState(server) : instance;
    }

    public void setCurrentSeason(ServerLevel world, Season season, boolean preserveTime) {
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

    public void setSeasonTransitionQuality(int seasonTransitionQuality) {
        this.seasonTransitionQuality = seasonTransitionQuality;
    }

    public void load(ServerLevel world) {
        server = world.getServer();
        updateSeasonTime(world);
        syncWorld(world);
    }

    public void tick(ServerLevel world) {
        if (server == null)
            server = world.getServer();
        if (instance == null) {
            instance = getServerState(server);
            if (instance == null)
                return;

            if (currentSeason == null)
                currentSeason = Season.SUMMER;
            if (seasonTime == -1)
                seasonTime = 0;
            if (context == null)
                context = new TransitionContext(Season.SUMMER, Season.AUTUMN, 0f);

            load(world);
        }

        if (world.getGameRules().get(FloraAndFaunaGameRules.ADVANCE_SEASONS)) {
            if (seasonLength != world.getGameRules().get(FloraAndFaunaGameRules.SEASON_LENGTH))
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

    public void updateSeasonTime(ServerLevel world) {
        seasonLength = world.getGameRules().get(FloraAndFaunaGameRules.SEASON_LENGTH);
        seasonTransitionLength = seasonLength / 3;
        seasonTransitionIncrement = Math.max(1, seasonTransitionLength / seasonTransitionQuality);
        seasonTransitionIncrementAmount = (float) seasonTransitionIncrement / seasonTransitionLength;
    }

    public void syncAll(MinecraftServer server) {
        PlayerLookup.all(server).forEach(serverPlayer -> {
            ServerPlayNetworking.send(serverPlayer, new SyncSeason((byte) currentSeason.ordinal()));
            ServerPlayNetworking.send(serverPlayer, new SyncSeasonTransition(context));
        });
    }

    public void syncWorld(ServerLevel world) {
        PlayerLookup.world(world).forEach(serverPlayer -> {
            ServerPlayNetworking.send(serverPlayer, new SyncSeason((byte) currentSeason.ordinal()));
            ServerPlayNetworking.send(serverPlayer, new SyncSeasonTransition(context));
        });
    }

    private void syncSeason(MinecraftServer server) {
        PlayerLookup.all(server).forEach(serverPlayer -> {
            ServerPlayNetworking.send(serverPlayer, new SyncSeason((byte) currentSeason.ordinal()));
        });
    }

    private void syncSeasonTransition(MinecraftServer server) {
        PlayerLookup.all(server).forEach(serverPlayer -> {
            ServerPlayNetworking.send(serverPlayer, new SyncSeasonTransition(context));
        });
    }

    @Nullable
    private static SeasonManager getServerState(MinecraftServer server) {
        ServerLevel serverWorld = server.getLevel(GreatBigWorld.ALTERWORLD_KEY);
        if (serverWorld == null)
            return null;
        SeasonManager manager = serverWorld.getDataStorage().computeIfAbsent(STATE_TYPE);
        manager.setDirty();
        return manager;
    }

    public record SyncSeason(byte season) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<SyncSeason> PACKET_ID = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "sync_season"));
        public static final StreamCodec<RegistryFriendlyByteBuf, SyncSeason> PACKET_CODEC = StreamCodec.ofMember(SyncSeason::write, SyncSeason::new);

        public SyncSeason(RegistryFriendlyByteBuf buf) {
            this(buf.readByte());
        }

        public void write(RegistryFriendlyByteBuf buf) {
            buf.writeByte(season);
        }

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return PACKET_ID;
        }
    }

    public record SyncSeasonTransition(byte[] context) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<SyncSeasonTransition> PACKET_ID = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "sync_season_color"));
        public static final StreamCodec<RegistryFriendlyByteBuf, SyncSeasonTransition> PACKET_CODEC = StreamCodec.ofMember(SyncSeasonTransition::write, SyncSeasonTransition::new);

        public SyncSeasonTransition(TransitionContext context) {
            this(new byte[]{(byte) context.getCurrent().ordinal(), (byte) (context.getNext() == null ? context.getCurrent().ordinal() : context.getNext().ordinal()), (byte) Math.min(127, context.getPercentage() * 100)});
        }

        public SyncSeasonTransition(RegistryFriendlyByteBuf buf) {
            this(buf.readByteArray());
        }

        public void write(RegistryFriendlyByteBuf buf) {
            buf.writeByteArray(context);
        }

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return PACKET_ID;
        }
    }
}
