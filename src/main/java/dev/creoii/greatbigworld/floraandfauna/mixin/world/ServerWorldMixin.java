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
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
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

    protected ServerWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, DynamicRegistryManager registryManager, RegistryEntry<DimensionType> dimensionEntry, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long biomeAccess, int maxChainedNeighborUpdates) {
        super(properties, registryRef, registryManager, dimensionEntry, profiler, isClient, debugWorld, biomeAccess, maxChainedNeighborUpdates);
    }

    @Inject(method = "tickIceAndSnow", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)Z", ordinal = 2), cancellable = true)
    private void gbw$tickSnowyBlocks(BlockPos pos, CallbackInfo ci, @Local(ordinal = 1) BlockPos blockPos) {
        BlockState state = getBlockState(blockPos);
        if (state.contains(SnowyHelper.SNOW_LAYERS)) {
            if (state.get(SnowyHelper.SNOW_LAYERS) == 0) {
                int i = state.get(SnowyHelper.SNOW_LAYERS);
                if (i < Math.min(getGameRules().getInt(GameRules.SNOW_ACCUMULATION_HEIGHT), 8)) {
                    BlockState blockState2 = state.with(SnowyHelper.SNOW_LAYERS, i + 1);
                    Block.pushEntitiesUpBeforeBlockChange(state, blockState2, this, blockPos);
                    setBlockState(blockPos, blockState2);
                }
            }
            ci.cancel();
        }
    }

    @WrapOperation(method = "tickIceAndSnow", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/Biome;canSetSnow(Lnet/minecraft/world/WorldView;Lnet/minecraft/util/math/BlockPos;)Z"))
    private boolean gbw$modifyCanSetIceForWinter(Biome instance, WorldView world, BlockPos pos, Operation<Boolean> original) {
        SeasonManager seasonManager = SeasonManager.getInstance(getServer());
        return original.call(instance, world, pos) || (seasonManager.getCurrentSeason() == Season.WINTER && !getBiome(pos).isIn(FloraAndFaunaTags.NOT_AFFECTED_BY_WINTER) && Blocks.SNOW.getDefaultState().canPlaceAt(world, pos) && pos.getY() >= world.getBottomY() && pos.getY() < world.getTopY() && world.getLightLevel(LightType.BLOCK, pos) < 10);
    }

    @Redirect(method = "tickIceAndSnow", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/Biome;getPrecipitation(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/world/biome/Biome$Precipitation;"))
    private Biome.Precipitation gbw$modifyTickIceAndSnowForWinter(Biome instance, BlockPos pos) {
        SeasonManager seasonManager = SeasonManager.getInstance(getServer());
        return seasonManager.getCurrentSeason() == Season.WINTER && !getBiome(pos).isIn(FloraAndFaunaTags.NOT_AFFECTED_BY_WINTER) ? Biome.Precipitation.SNOW : instance.getPrecipitation(pos);
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
