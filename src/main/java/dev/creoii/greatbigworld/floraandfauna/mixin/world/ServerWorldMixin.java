package dev.creoii.greatbigworld.floraandfauna.mixin.world;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.floraandfauna.registry.FloraAndFaunaGameRules;
import dev.creoii.greatbigworld.floraandfauna.season.Season;
import dev.creoii.greatbigworld.floraandfauna.season.SeasonManager;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaTags;
import dev.creoii.greatbigworld.floraandfauna.util.SnowyHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SnowBlock;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.profiler.Profiler;
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
import java.util.function.Supplier;

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
    private void gbw$tickSnowyUnderTrees(BlockPos pos, CallbackInfo ci, @Local(ordinal = 1) BlockPos blockPos, @Local Biome biome) {
        if (isRaining()) {
            BlockState downState = getBlockState(blockPos.down());
            if (downState.isIn(BlockTags.LEAVES)) {
                BlockPos top = getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, pos);
                BlockPos top2 = top.down();
                Biome biome2 = getBiome(top).value();
                if (biome2.canSetIce(this, top2)) {
                    setBlockState(top2, Blocks.ICE.getDefaultState());
                }

                int i = getGameRules().getInt(GameRules.SNOW_ACCUMULATION_HEIGHT);
                if (i > 0 && biome.canSetSnow(this, top)) {
                    BlockState blockState1 = getBlockState(top);
                    if (blockState1.isOf(Blocks.SNOW)) {
                        int j = blockState1.get(SnowBlock.LAYERS);
                        if (j < Math.min(i, 8)) {
                            BlockState blockState2 = blockState1.with(SnowBlock.LAYERS, j + 1);
                            Block.pushEntitiesUpBeforeBlockChange(blockState1, blockState2, this, top);
                            setBlockState(top, blockState2);
                        }
                    } else {
                        if (blockState1.contains(SnowyHelper.SNOW_LAYERS)) {
                            SnowyHelper.tickSnowUnderLeaves((ServerWorld) (Object) this, blockState1, top);
                        } else setBlockState(top, Blocks.SNOW.getDefaultState());
                    }
                }
            }
        }
    }

    @WrapOperation(method = "tickIceAndSnow", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/Biome;canSetSnow(Lnet/minecraft/world/WorldView;Lnet/minecraft/util/math/BlockPos;)Z"))
    private boolean gbw$modifyCanSetIceForWinter(Biome instance, WorldView world, BlockPos pos, Operation<Boolean> original) {
        SeasonManager seasonManager = SeasonManager.getInstance(getServer());
        return original.call(instance, world, pos) || (seasonManager.getCurrentSeason() == Season.WINTER && !getBiome(pos).isIn(FloraAndFaunaTags.NOT_AFFECTED_BY_WINTER) && Blocks.SNOW.getDefaultState().canPlaceAt(world, pos) && world.isInHeightLimit(pos.getY()) && world.getLightLevel(LightType.BLOCK, pos) < 10);
    }

    @Redirect(method = "tickIceAndSnow", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/Biome;getPrecipitation(Lnet/minecraft/util/math/BlockPos;I)Lnet/minecraft/world/biome/Biome$Precipitation;"))
    private Biome.Precipitation gbw$modifyTickIceAndSnowForWinter(Biome instance, BlockPos pos, int seaLevel) {
        SeasonManager seasonManager = SeasonManager.getInstance(getServer());
        return seasonManager.getCurrentSeason() == Season.WINTER && !getBiome(pos).isIn(FloraAndFaunaTags.NOT_AFFECTED_BY_WINTER) ? Biome.Precipitation.SNOW : instance.getPrecipitation(pos, seaLevel);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;wakeSleepingPlayers()V"))
    private void gbw$sleepingSkipsSeasonTime(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        if (getGameRules().getBoolean(FloraAndFaunaGameRules.DO_SEASON_CYCLE)) {
            SeasonManager seasonManager = SeasonManager.getInstance(getServer());
            int timeToSkip = 13000; // length of night in ticks, should be subtracted by how long into the night we are
            seasonManager.addSeasonTime(timeToSkip);
        }
    }
}
