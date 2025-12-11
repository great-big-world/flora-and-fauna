package dev.creoii.greatbigworld.floraandfauna.mixin.block;

import dev.creoii.greatbigworld.block.OverlayState;
import dev.creoii.greatbigworld.floraandfauna.season.Season;
import dev.creoii.greatbigworld.floraandfauna.season.SeasonManager;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaTags;
import dev.creoii.greatbigworld.floraandfauna.util.SnowyHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CrossCollisionBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({
        FenceBlock.class,
        IronBarsBlock.class
})
public abstract class FenceAndPaneBlockMixin extends CrossCollisionBlock implements OverlayState {
    protected FenceAndPaneBlockMixin(float radius1, float radius2, float boundingHeight1, float boundingHeight2, float collisionHeight, Properties settings) {
        super(radius1, radius2, boundingHeight1, boundingHeight2, collisionHeight, settings);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void gbw$setSnowyDefaultState(Properties settings, CallbackInfo ci) {
        registerDefaultState(getStateDefinition().any().setValue(NORTH, false).setValue(EAST, false).setValue(SOUTH, false).setValue(WEST, false).setValue(WATERLOGGED, false).setValue(SnowyHelper.SNOW_LAYERS, 0));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        VoxelShape shape = super.getShape(state, world, pos, context);
        if (SnowyHelper.isSnowy(state)) {
            shape = Shapes.or(shape, SnowyHelper.getSnowShape(state));
        }
        return shape;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        if (SnowyHelper.isSnowy(state)) {
            return Shapes.or(super.getCollisionShape(state, world, pos, context), SnowyHelper.LAYERS_TO_SHAPE[state.getValue(SnowyHelper.SNOW_LAYERS) - 1]);
        }
        return super.getCollisionShape(state, world, pos, context);
    }

    @Inject(method = "getStateForPlacement", at = @At("RETURN"), cancellable = true)
    private void gbw$applyFenceGateSnowPlacementState(BlockPlaceContext ctx, CallbackInfoReturnable<BlockState> cir) {
        BlockState state = ctx.getLevel().getBlockState(ctx.getClickedPos());
        if (state.is(Blocks.SNOW)) {
            cir.setReturnValue(defaultBlockState().setValue(SnowyHelper.SNOW_LAYERS, state.getValue(SnowLayerBlock.LAYERS)));
        } else if (state.hasProperty(SnowyHelper.SNOW_LAYERS) && SnowyHelper.isSnowy(state)) {
            cir.setReturnValue(defaultBlockState().setValue(SnowyHelper.SNOW_LAYERS, state.getValue(SnowyHelper.SNOW_LAYERS)));
        }
    }

    @Inject(method = "createBlockStateDefinition", at = @At("TAIL"))
    private void gbw$appendSnowLayersProperty(StateDefinition.Builder<Block, BlockState> builder, CallbackInfo ci) {
        builder.add(SnowyHelper.SNOW_LAYERS);
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return SnowyHelper.isSnowy(state);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        Holder<Biome> biomeEntry = world.getBiome(pos);
        SeasonManager seasonManager = SeasonManager.getInstance(world.getServer());
        if (seasonManager != null && world.dimensionTypeRegistration().is(FloraAndFaunaTags.AFFECTED_BY_SEASONS) && world.getBrightness(LightLayer.BLOCK, pos) > 11 || (seasonManager.getCurrentSeason() != Season.WINTER && !biomeEntry.is(FloraAndFaunaTags.NOT_AFFECTED_BY_WINTER) && biomeEntry.value().warmEnoughToRain(pos, world.getSeaLevel()))) {
            if (state.getValue(SnowyHelper.SNOW_LAYERS) > 0)
                dropResources(Blocks.SNOW.defaultBlockState().setValue(SnowLayerBlock.LAYERS, state.getValue(SnowyHelper.SNOW_LAYERS)), world, pos);
            world.setBlockAndUpdate(pos, state.setValue(SnowyHelper.SNOW_LAYERS, 0));
        }
    }

    @Override
    protected boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        if (context.getItemInHand().is(Items.SNOW) && state.getValue(SnowyHelper.SNOW_LAYERS) < 8) {
            return true;
        }
        return super.canBeReplaced(state, context);
    }

    @Override
    public BlockState gbw$getOverlayState(BlockState state, BlockPos pos, RandomSource random) {
        if (SnowyHelper.isSnowy(state)) {
            return Blocks.SNOW.defaultBlockState().setValue(SnowLayerBlock.LAYERS, state.getValue(SnowyHelper.SNOW_LAYERS));
        }
        return Blocks.AIR.defaultBlockState();
    }
}
