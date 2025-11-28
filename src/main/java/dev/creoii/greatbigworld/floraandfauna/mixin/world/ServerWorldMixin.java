package dev.creoii.greatbigworld.floraandfauna.mixin.world;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.floraandfauna.registry.FloraAndFaunaGameRules;
import dev.creoii.greatbigworld.floraandfauna.season.Season;
import dev.creoii.greatbigworld.floraandfauna.season.SeasonManager;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaTags;
import dev.creoii.greatbigworld.floraandfauna.util.SnowyHelper;
import net.minecraft.block.*;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World implements StructureWorldAccess {
    @Shadow @NotNull public abstract MinecraftServer getServer();
    @Shadow public abstract GameRules getGameRules();

    protected ServerWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, DynamicRegistryManager registryManager, RegistryEntry<DimensionType> dimensionEntry, boolean isClient, boolean debugWorld, long seed, int maxChainedNeighborUpdates) {
        super(properties, registryRef, registryManager, dimensionEntry, isClient, debugWorld, seed, maxChainedNeighborUpdates);
    }

    @Inject(method = "tickIceAndSnow", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)Z", ordinal = 2), cancellable = true)
    private void gbw$tickSnowyBlocks(BlockPos pos, CallbackInfo ci, @Local(ordinal = 1) BlockPos blockPos) {
        BlockState state = getBlockState(blockPos);
        if (state.contains(SnowyHelper.SNOW_LAYERS)) {
            SnowyHelper.tickSnowUnderLeaves((ServerWorld) (Object) this, state, blockPos);
            ci.cancel();
        }
    }

    @Inject(method = "tickIceAndSnow", at = @At("TAIL"))
    private void gbw$tickSnowyUnderTrees(BlockPos pos, CallbackInfo ci) {
        if (!isRaining())
            return;

        // top including leaves
        int top = getTopY(Heightmap.Type.MOTION_BLOCKING, pos.getX(), pos.getZ());
        // top excluding leaves
        int noLeavesTop = getTopY(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, pos.getX(), pos.getZ());

        BlockPos.Mutable cursor = new BlockPos.Mutable();
        BlockPos.Mutable below = new BlockPos.Mutable();

        SeasonManager seasonManager = SeasonManager.getInstance(getServer());
        int maxLayers = getGameRules().getInt(GameRules.SNOW_ACCUMULATION_HEIGHT);

        for (int y = top; y >= noLeavesTop; --y) {
            cursor.set(pos.getX(), y, pos.getZ());
            below.set(pos.getX(), y - 1, pos.getZ());

            BlockState state = getBlockState(cursor);
            BlockState stateBelow = getBlockState(below);

            Biome biome = getBiome(cursor).value();

            if (state.isIn(BlockTags.LEAVES))
                continue;

            if (biome.canSetIce(this, below, false)) {
                setBlockState(below, Blocks.ICE.getDefaultState(), 2);
                continue;
            }

            boolean winterAllowed = seasonManager.getCurrentSeason() == Season.WINTER && !getBiome(pos).isIn(FloraAndFaunaTags.NOT_AFFECTED_BY_WINTER) && getDimensionEntry().isIn(FloraAndFaunaTags.AFFECTED_BY_SEASONS);

            if (maxLayers > 0 && (biome.canSetSnow(this, cursor) || winterAllowed)) {
                if (state.isOf(Blocks.SNOW)) {
                    int layers = state.get(SnowBlock.LAYERS);
                    if (layers < Math.min(maxLayers, 8)) {
                        BlockState newState = state.with(SnowBlock.LAYERS, layers + 1);
                        Block.pushEntitiesUpBeforeBlockChange(state, newState, this, cursor);
                        setBlockState(cursor, newState, 2);
                    }
                    continue;
                }

                if (Blocks.SNOW.getDefaultState().canPlaceAt(this, cursor)) {
                    setBlockState(cursor, Blocks.SNOW.getDefaultState(), 2);
                }
            } else if (state.contains(SnowyHelper.SNOW_LAYERS)) {
                if (!(state.contains(Properties.DOUBLE_BLOCK_HALF) && state.get(Properties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER)) {
                    int layers = state.get(SnowyHelper.SNOW_LAYERS);
                    if (layers < Math.min(maxLayers, 8)) {
                        BlockState newState = state.with(SnowyHelper.SNOW_LAYERS, layers + 1);
                        Block.pushEntitiesUpBeforeBlockChange(state, newState, this, cursor);
                        setBlockState(cursor, newState, 2);

                        if (stateBelow.contains(SnowyBlock.SNOWY)) {
                            if (!stateBelow.get(SnowyBlock.SNOWY))
                                setBlockState(below, stateBelow.with(SnowyBlock.SNOWY, true), 2);
                        }
                    }
                }
            } else if (stateBelow.contains(SnowyHelper.SNOW_LAYERS)) {
                if (!(stateBelow.contains(Properties.DOUBLE_BLOCK_HALF) && stateBelow.get(Properties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER)) {
                    int layers = stateBelow.get(SnowyHelper.SNOW_LAYERS);
                    if (layers < Math.min(maxLayers, 8)) {
                        BlockState newState = stateBelow.with(SnowyHelper.SNOW_LAYERS, layers + 1);
                        Block.pushEntitiesUpBeforeBlockChange(stateBelow, newState, this, below);
                        setBlockState(below, newState, 2);

                        BlockState stateBelow2 = getBlockState(below.down());
                        if (stateBelow2.contains(SnowyBlock.SNOWY)) {
                            if (!stateBelow.get(SnowyBlock.SNOWY))
                                setBlockState(below.down(), stateBelow2.with(SnowyBlock.SNOWY, true), 2);
                        }
                    }
                }
            }
        }
    }

    @WrapOperation(method = "tickIceAndSnow", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/Biome;canSetSnow(Lnet/minecraft/world/WorldView;Lnet/minecraft/util/math/BlockPos;)Z"))
    private boolean gbw$modifyCanSetIceForWinter(Biome instance, WorldView world, BlockPos pos, Operation<Boolean> original) {
        SeasonManager seasonManager = SeasonManager.getInstance(getServer());
        if (seasonManager == null) {
            return original.call(instance, world, pos);
        }
        return original.call(instance, world, pos) || (seasonManager.getCurrentSeason() == Season.WINTER && !getBiome(pos).isIn(FloraAndFaunaTags.NOT_AFFECTED_BY_WINTER) && getDimensionEntry().isIn(FloraAndFaunaTags.AFFECTED_BY_SEASONS) && Blocks.SNOW.getDefaultState().canPlaceAt(world, pos) && world.isInHeightLimit(pos.getY()) && world.getLightLevel(LightType.BLOCK, pos) < 10);
    }

    @Redirect(method = "tickIceAndSnow", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/Biome;getPrecipitation(Lnet/minecraft/util/math/BlockPos;I)Lnet/minecraft/world/biome/Biome$Precipitation;"))
    private Biome.Precipitation gbw$modifyTickIceAndSnowForWinter(Biome instance, BlockPos pos, int seaLevel) {
        SeasonManager seasonManager = SeasonManager.getInstance(getServer());
        if (seasonManager == null) {
            return instance.getPrecipitation(pos, seaLevel);
        }
        return seasonManager.getCurrentSeason() == Season.WINTER && !getBiome(pos).isIn(FloraAndFaunaTags.NOT_AFFECTED_BY_WINTER) && getDimensionEntry().isIn(FloraAndFaunaTags.AFFECTED_BY_SEASONS) ? Biome.Precipitation.SNOW : instance.getPrecipitation(pos, seaLevel);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;wakeSleepingPlayers()V"))
    private void gbw$sleepingSkipsSeasonTime(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        if (getGameRules().getBoolean(FloraAndFaunaGameRules.DO_SEASON_CYCLE)) {
            SeasonManager seasonManager = SeasonManager.getInstance(getServer());
            if (seasonManager == null)
                return;
            int timeToSkip = 13000; // length of night in ticks, should be subtracted by how long into the night we are
            seasonManager.addSeasonTime(timeToSkip);
        }
    }
}
