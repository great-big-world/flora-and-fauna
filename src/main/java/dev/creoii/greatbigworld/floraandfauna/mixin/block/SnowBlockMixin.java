package dev.creoii.greatbigworld.floraandfauna.mixin.block;

import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.floraandfauna.season.Season;
import dev.creoii.greatbigworld.floraandfauna.season.SeasonManager;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaTags;
import dev.creoii.greatbigworld.floraandfauna.util.SnowyHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SnowBlock;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LightType;
import net.minecraft.world.WorldView;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SnowBlock.class)
public abstract class SnowBlockMixin extends Block {
    public SnowBlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "getPlacementState", at = @At(value = "RETURN", ordinal = 1), cancellable = true)
    private void gbw$layerSnow(ItemPlacementContext ctx, CallbackInfoReturnable<BlockState> cir, @Local BlockState blockState) {
        if (blockState.contains(SnowyHelper.SNOW_LAYERS)) {
            BlockState down = ctx.getWorld().getBlockState(ctx.getBlockPos().down());
            if (blockState.contains(Properties.DOUBLE_BLOCK_HALF) && down.contains(Properties.DOUBLE_BLOCK_HALF) && down.contains(SnowyHelper.SNOW_LAYERS)) {
                if (blockState.get(Properties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER && down.get(SnowyHelper.SNOW_LAYERS) != 8) {
                    return;
                }
            }

            cir.setReturnValue(blockState.with(SnowyHelper.SNOW_LAYERS, Math.min(8, blockState.get(SnowyHelper.SNOW_LAYERS) + 1)));
        }
    }

    @Inject(method = "canPlaceAt", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isIn(Lnet/minecraft/registry/tag/TagKey;)Z", ordinal = 0), cancellable = true)
    private void gbw$canPlaceOnSnowyPlants(BlockState state, WorldView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir, @Local(ordinal = 1) BlockState blockState) {
        if (blockState.contains(SnowyHelper.SNOW_LAYERS) && blockState.get(SnowyHelper.SNOW_LAYERS) == 8) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
    private void gbw$meltInNonWinterSeason(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        RegistryEntry<Biome> biomeEntry = world.getBiome(pos);
        SeasonManager seasonManager = SeasonManager.getInstance(world.getServer());
        if (seasonManager != null && ((world.getDimensionEntry().isIn(FloraAndFaunaTags.AFFECTED_BY_SEASONS) && world.getLightLevel(LightType.BLOCK, pos) > 11) || (seasonManager.getCurrentSeason() != Season.WINTER && !biomeEntry.isIn(FloraAndFaunaTags.NOT_AFFECTED_BY_WINTER) && biomeEntry.value().doesNotSnow(pos, world.getSeaLevel())))) {
            dropStacks(state, world, pos);
            world.removeBlock(pos, false);
            ci.cancel();
        }
    }
}
