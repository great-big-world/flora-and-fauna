package dev.creoii.greatbigworld.floraandfauna.mixin.world;

import dev.creoii.greatbigworld.floraandfauna.registry.FloraAndFaunaGameRules;
import dev.creoii.greatbigworld.floraandfauna.season.Season;
import dev.creoii.greatbigworld.floraandfauna.season.SeasonManager;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaTags;
import dev.creoii.greatbigworld.floraandfauna.util.SnowyHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.SnowyDirtBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.WritableLevelData;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(ServerLevel.class)
public abstract class ServerWorldMixin extends Level implements WorldGenLevel {
    @Shadow @NotNull public abstract MinecraftServer getServer();
    @Shadow public abstract GameRules getGameRules();

    protected ServerWorldMixin(WritableLevelData properties, ResourceKey<Level> registryRef, RegistryAccess registryManager, Holder<DimensionType> dimensionEntry, boolean isClient, boolean debugWorld, long seed, int maxChainedNeighborUpdates) {
        super(properties, registryRef, registryManager, dimensionEntry, isClient, debugWorld, seed, maxChainedNeighborUpdates);
    }

    @Inject(method = "tickPrecipitation", at = @At("HEAD"), cancellable = true)
    private void gbw$tickSnowyUnderTrees(BlockPos pos, CallbackInfo ci) {
        if (!isRaining())
            return;

        // top including leaves
        int top = getHeight(Heightmap.Types.MOTION_BLOCKING, pos);
        // top excluding leaves
        int noLeavesTop = getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos);

        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        BlockPos.MutableBlockPos below = new BlockPos.MutableBlockPos();

        SeasonManager seasonManager = SeasonManager.getInstance(getServer());
        int maxLayers = getGameRules().get(GameRules.MAX_SNOW_ACCUMULATION_HEIGHT);

        for (int y = top; y >= noLeavesTop; --y) {
            cursor.set(pos.getX(), y, pos.getZ());
            below.set(pos.getX(), y - 1, pos.getZ());

            BlockState state = getBlockState(cursor);
            BlockState stateBelow = getBlockState(below);

            Biome biome = getBiome(cursor).value();

            if (state.is(BlockTags.LEAVES))
                continue;

            if (biome.shouldFreeze(this, below, false)) {
                setBlock(below, Blocks.ICE.defaultBlockState(), 2);
                continue;
            }

            if (seasonManager.getCurrentSeason() == Season.WINTER && !getBiome(pos).is(FloraAndFaunaTags.NOT_AFFECTED_BY_WINTER) && dimensionTypeRegistration().is(FloraAndFaunaTags.AFFECTED_BY_SEASONS) && maxLayers > 0) {
                if (state.is(Blocks.SNOW)) {
                    int layers = state.getValue(SnowLayerBlock.LAYERS);
                    if (layers < Math.min(maxLayers, 8)) {
                        BlockState newState = state.setValue(SnowLayerBlock.LAYERS, layers + 1);
                        Block.pushEntitiesUp(state, newState, this, cursor);
                        setBlock(cursor, newState, 2);
                    }
                } else if (state.hasProperty(SnowyHelper.SNOW_LAYERS)) {
                    if (!(state.hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF) && state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER)) {
                        int layers = state.getValue(SnowyHelper.SNOW_LAYERS);
                        if (layers < Math.min(maxLayers, 8)) {
                            BlockState newState = state.setValue(SnowyHelper.SNOW_LAYERS, layers + 1);
                            Block.pushEntitiesUp(state, newState, this, cursor);
                            setBlockAndUpdate(cursor, newState);

                            if (stateBelow.hasProperty(SnowyDirtBlock.SNOWY) && !stateBelow.getValue(SnowyDirtBlock.SNOWY)) {
                                setBlockAndUpdate(below, stateBelow.setValue(SnowyDirtBlock.SNOWY, true));
                            }
                        }
                    }
                } else if (stateBelow.hasProperty(SnowyHelper.SNOW_LAYERS)) {
                    if (!(stateBelow.hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF) && stateBelow.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER)) {
                        int layers = stateBelow.getValue(SnowyHelper.SNOW_LAYERS);
                        if (layers < Math.min(maxLayers, 8)) {
                            BlockState newState = stateBelow.setValue(SnowyHelper.SNOW_LAYERS, layers + 1);
                            Block.pushEntitiesUp(stateBelow, newState, this, below);
                            setBlockAndUpdate(below, newState);

                            BlockState stateBelow2 = getBlockState(below.below());
                            if (stateBelow2.hasProperty(SnowyDirtBlock.SNOWY) && !stateBelow2.getValue(SnowyDirtBlock.SNOWY)) {
                                setBlockAndUpdate(below.below(), stateBelow2.setValue(SnowyDirtBlock.SNOWY, true));
                            }
                        }
                    }
                } else if (Blocks.SNOW.defaultBlockState().canSurvive(this, cursor)) {
                    setBlockAndUpdate(cursor, Blocks.SNOW.defaultBlockState());

                    if (stateBelow.hasProperty(SnowyDirtBlock.SNOWY) && !stateBelow.getValue(SnowyDirtBlock.SNOWY)) {
                        setBlockAndUpdate(below, stateBelow.setValue(SnowyDirtBlock.SNOWY, true));
                    }
                }
            }

            BlockPos topPos = getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, pos);
            BlockPos downPos = topPos.below();

            Biome.Precipitation precipitation = getBiome(topPos).value().getPrecipitationAt(downPos, getSeaLevel());
            if (precipitation != Biome.Precipitation.NONE) {
                BlockState precipitationState = getBlockState(downPos);
                precipitationState.getBlock().handlePrecipitation(precipitationState, this, downPos, precipitation);
            }
        }

        ci.cancel();
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;wakeUpAllPlayers()V"))
    private void gbw$sleepingSkipsSeasonTime(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        if (getGameRules().get(FloraAndFaunaGameRules.ADVANCE_SEASONS)) {
            SeasonManager seasonManager = SeasonManager.getInstance(getServer());
            if (seasonManager == null)
                return;
            int timeToSkip = 13000; // length of night in ticks, should be subtracted by how long into the night we are
            seasonManager.addSeasonTime(timeToSkip);
        }
    }
}
