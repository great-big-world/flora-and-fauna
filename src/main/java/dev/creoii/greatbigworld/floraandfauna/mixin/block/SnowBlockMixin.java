package dev.creoii.greatbigworld.floraandfauna.mixin.block;

import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.floraandfauna.season.Season;
import dev.creoii.greatbigworld.floraandfauna.season.SeasonManager;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaTags;
import dev.creoii.greatbigworld.floraandfauna.util.SnowyHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SnowLayerBlock.class)
public abstract class SnowBlockMixin extends Block {
    public SnowBlockMixin(Properties settings) {
        super(settings);
    }

    @Inject(method = "getStateForPlacement", at = @At(value = "RETURN", ordinal = 1), cancellable = true)
    private void gbw$layerSnow(BlockPlaceContext ctx, CallbackInfoReturnable<BlockState> cir, @Local BlockState blockState) {
        if (blockState.hasProperty(SnowyHelper.SNOW_LAYERS)) {
            BlockState down = ctx.getLevel().getBlockState(ctx.getClickedPos().below());
            if (blockState.hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF) && down.hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF) && down.hasProperty(SnowyHelper.SNOW_LAYERS)) {
                if (blockState.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER && down.getValue(SnowyHelper.SNOW_LAYERS) != 8) {
                    return;
                }
            }

            cir.setReturnValue(blockState.setValue(SnowyHelper.SNOW_LAYERS, Math.min(8, blockState.getValue(SnowyHelper.SNOW_LAYERS) + 1)));
        }
    }

    @Inject(method = "canSurvive", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z", ordinal = 0), cancellable = true)
    private void gbw$canPlaceOnSnowyPlants(BlockState state, LevelReader world, BlockPos pos, CallbackInfoReturnable<Boolean> cir, @Local(ordinal = 1) BlockState blockState) {
        if (blockState.hasProperty(SnowyHelper.SNOW_LAYERS) && blockState.getValue(SnowyHelper.SNOW_LAYERS) == 8) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
    private void gbw$meltInNonWinterSeason(BlockState state, ServerLevel world, BlockPos pos, RandomSource random, CallbackInfo ci) {
        Holder<Biome> biomeEntry = world.getBiome(pos);
        SeasonManager seasonManager = SeasonManager.getInstance(world.getServer());
        if (seasonManager != null && ((world.dimensionTypeRegistration().is(FloraAndFaunaTags.AFFECTED_BY_SEASONS) && world.getBrightness(LightLayer.BLOCK, pos) > 11) || (seasonManager.getCurrentSeason() != Season.WINTER && !biomeEntry.is(FloraAndFaunaTags.NOT_AFFECTED_BY_WINTER) && biomeEntry.value().warmEnoughToRain(pos, world.getSeaLevel())))) {
            dropResources(state, world, pos);
            world.removeBlock(pos, false);
            ci.cancel();
        }
    }
}
