package dev.creoii.greatbigworld.floraandfauna.mixin.block;

import dev.creoii.greatbigworld.floraandfauna.client.FloraAndFaunaClient;
import dev.creoii.greatbigworld.floraandfauna.season.Season;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaTags;
import dev.creoii.greatbigworld.floraandfauna.util.SnowyHelper;
import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.LightType;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({
        FenceBlock.class,
        PaneBlock.class
})
public abstract class FenceAndPaneBlockMixin extends HorizontalConnectingBlock {
    protected FenceAndPaneBlockMixin(float radius1, float radius2, float boundingHeight1, float boundingHeight2, float collisionHeight, Settings settings) {
        super(radius1, radius2, boundingHeight1, boundingHeight2, collisionHeight, settings);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void gbw$setSnowyDefaultState(Settings settings, CallbackInfo ci) {
        setDefaultState(getStateManager().getDefaultState().with(NORTH, false).with(EAST, false).with(SOUTH, false).with(WEST, false).with(WATERLOGGED, false).with(SnowyHelper.SNOW_LAYERS, 0));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        VoxelShape shape = super.getOutlineShape(state, world, pos, context);
        if (SnowyHelper.isSnowy(state)) {
            shape = VoxelShapes.union(shape, SnowyHelper.getSnowShape(state));
        }
        return shape;
    }

    @Inject(method = "getPlacementState", at = @At("RETURN"), cancellable = true)
    private void gbw$applyFenceGateSnowPlacementState(ItemPlacementContext ctx, CallbackInfoReturnable<BlockState> cir) {
        BlockState state = ctx.getWorld().getBlockState(ctx.getBlockPos());
        if (state.isOf(Blocks.SNOW)) {
            cir.setReturnValue(getDefaultState().with(SnowyHelper.SNOW_LAYERS, state.get(SnowBlock.LAYERS)));
        }
    }

    @Inject(method = "appendProperties", at = @At("TAIL"))
    private void gbw$appendSnowLayersProperty(StateManager.Builder<Block, BlockState> builder, CallbackInfo ci) {
        builder.add(SnowyHelper.SNOW_LAYERS);
    }

    @SuppressWarnings("deprecation")
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        RegistryEntry<Biome> biomeEntry = world.getBiome(pos);
        if (world.getLightLevel(LightType.BLOCK, pos) > 11 || (FloraAndFaunaClient.getCurrentSeason() != Season.WINTER && !biomeEntry.isIn(FloraAndFaunaTags.NOT_AFFECTED_BY_WINTER) && biomeEntry.value().doesNotSnow(pos))) {
            if (state.get(SnowyHelper.SNOW_LAYERS) > 0)
                dropStacks(Blocks.SNOW.getDefaultState().with(SnowBlock.LAYERS, state.get(SnowyHelper.SNOW_LAYERS)), world, pos);
            world.setBlockState(pos, state.with(SnowyHelper.SNOW_LAYERS, 0));
        }
    }
}
