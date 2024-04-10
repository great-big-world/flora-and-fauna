package dev.creoii.greatbigworld.floraandfauna.season;

import dev.creoii.greatbigworld.floraandfauna.FloraAndFauna;
import dev.creoii.greatbigworld.floraandfauna.registry.FloraAndFaunaGameRules;
import dev.creoii.greatbigworld.floraandfauna.util.ColorHelper;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;
import net.minecraft.world.biome.Biome;

public class SeasonManager extends PersistentState {
    public static final Identifier SYNC_SEASON = new Identifier(FloraAndFauna.NAMESPACE, "sync_season");
    public static final Identifier SYNC_SEASON_COLOR = new Identifier(FloraAndFauna.NAMESPACE, "sync_season_color");
    private static final Type<SeasonManager> STATE_TYPE = new Type<>(SeasonManager::new, SeasonManager::createFromNbt, null);
    private static SeasonManager instance;
    private Season currentSeason = Season.SUMMER;
    private static int syncSeasonTime;
    private static int syncSeasonColorTime;
    private static int syncSeasonColorTimeLength;
    private int seasonTime = -1;
    private int seasonColorTime = -1;

    public static SeasonManager getInstance(MinecraftServer server) {
        return instance == null ? instance = getServerState(server) : instance;
    }

    public void setCurrentSeason(ServerWorld world, Season season, boolean preserveTime) {
        instance.currentSeason = season;
        if (!preserveTime) {
            instance.seasonTime = 0;
            instance.seasonColorTime = 0;
        }
        load(world);
    }

    public Season getCurrentSeason() {
        return currentSeason;
    }

    public static Season getNextSeason(Season season) {
        return switch (season) {
            case AUTUMN -> Season.WINTER;
            case WINTER -> Season.SPRING;
            case SPRING -> Season.SUMMER;
            case SUMMER -> Season.AUTUMN;
        };
    }

    public static int getColor(BlockRenderView world, BlockPos pos, int color) {
        if (world == null)
            return color;

        Season.Context context = new Season.Context(world, pos, color);
        RegistryEntry<Biome> biomeEntry = world.getBiomeFabric(pos);
        if (biomeEntry == null || !biomeEntry.hasKeyAndValue()) {
            return color;
        }

        return ColorHelper.interpolate(instance.getSeasonColorPercentage(), instance.currentSeason.getColorChange().apply(context), getNextSeason(instance.currentSeason).getColorChange().apply(context));
    }

    public void load(ServerWorld world) {
        instance.syncSeason(world.getServer());
        instance.updateSeasonTime(world);
        instance.syncSeasonColor(world.getServer());
    }

    public void tick(ServerWorld world) {
        MinecraftServer server = world.getServer();
        if (!server.getTickManager().shouldTick())
            return;

        if (instance == null) {
            instance = getServerState(server);

            if (instance.currentSeason == null) {
                instance.currentSeason = Season.SUMMER;
            }
            if (instance.seasonTime == -1) {
                instance.seasonTime = 0;
            }
            if (instance.seasonColorTime == -1) {
                instance.seasonColorTime = 0;
            }
            load(world);
        }

        if (world.getRegistryKey() == World.OVERWORLD && world.getGameRules().getBoolean(FloraAndFaunaGameRules.DO_SEASON_CYCLE)) {
            // season transition
            if (++instance.seasonTime >= syncSeasonTime) {
                instance.currentSeason = getNextSeason(instance.currentSeason);
                instance.syncSeason(server);
                instance.seasonTime = 0;
                instance.seasonColorTime = 0;
            }

            if (syncSeasonTime != world.getGameRules().getInt(FloraAndFaunaGameRules.SEASON_LENGTH))
                instance.updateSeasonTime(world);

            // color transition
            if (instance.seasonTime >= syncSeasonColorTimeLength && instance.seasonTime < syncSeasonColorTimeLength * 2) {
                if (++instance.seasonColorTime % syncSeasonColorTime == 0) {
                    syncSeasonColor(server);
                }
            }
        }
    }

    private void updateSeasonTime(ServerWorld world) {
        syncSeasonTime = world.getGameRules().getInt(FloraAndFaunaGameRules.SEASON_LENGTH);
        syncSeasonColorTimeLength = syncSeasonTime / 3;
        syncSeasonColorTime = syncSeasonColorTimeLength / 30;
    }

    private float getSeasonColorPercentage() {
        return (float) instance.seasonColorTime / syncSeasonColorTimeLength;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putInt("season", instance.currentSeason.ordinal());
        nbt.putInt("season_time", instance.seasonTime);
        nbt.putInt("season_color_time", instance.seasonColorTime);
        return nbt;
    }

    private static SeasonManager createFromNbt(NbtCompound nbt) {
        SeasonManager manager = new SeasonManager();
        manager.currentSeason = Season.values()[nbt.getInt("season")];
        manager.seasonTime = nbt.getInt("season_time");
        manager.seasonColorTime = nbt.getInt("season_color_time");
        return manager;
    }

    private PacketByteBuf getSyncData() {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(instance.currentSeason.ordinal());
        return buf;
    }

    private void syncSeason(MinecraftServer server) {
        server.execute(() -> {
            server.getPlayerManager().getPlayerList().forEach(serverPlayer -> {
                ServerPlayNetworking.send(serverPlayer, SYNC_SEASON, instance.getSyncData());
            });
        });
    }

    private void syncSeasonColor(MinecraftServer server) {
        server.execute(() -> {
            server.getPlayerManager().getPlayerList().forEach(serverPlayer -> {
                ServerPlayNetworking.send(serverPlayer, SYNC_SEASON_COLOR, PacketByteBufs.empty());
            });
        });
    }

    private static SeasonManager getServerState(MinecraftServer server) {
        PersistentStateManager persistentStateManager = server.getWorld(World.OVERWORLD).getPersistentStateManager();
        SeasonManager manager = persistentStateManager.getOrCreate(STATE_TYPE, FloraAndFauna.NAMESPACE);
        manager.markDirty();
        return manager;
    }
}
