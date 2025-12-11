package dev.creoii.greatbigworld.floraandfauna.mixin.block;

import dev.creoii.greatbigworld.block.OverlayState;
import dev.creoii.greatbigworld.floraandfauna.season.Season;
import dev.creoii.greatbigworld.floraandfauna.season.SeasonManager;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaTags;
import dev.creoii.greatbigworld.floraandfauna.util.SnowyHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FenceGateBlock.class)
public abstract class FenceGateBlockMixin extends HorizontalDirectionalBlock implements OverlayState {
    protected FenceGateBlockMixin(Properties settings) {
        super(settings);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void gbw$setSnowyDefaultState(WoodType type, BlockBehaviour.Properties settings, CallbackInfo ci) {
        registerDefaultState(getStateDefinition().any().setValue(SnowyHelper.SNOW_LAYERS, 0));
    }

    @Inject(method = "getShape", at = @At("RETURN"), cancellable = true)
    private void gbw$mergeSnowShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if (SnowyHelper.isSnowy(state)) {
            cir.setReturnValue(Shapes.or(cir.getReturnValue(), SnowyHelper.getSnowShape(state)));
        }
    }

    @Inject(method = "getCollisionShape", at = @At("RETURN"), cancellable = true)
    private void gbw$mergeSnowCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if (SnowyHelper.isSnowy(state)) {
            cir.setReturnValue(Shapes.or(cir.getReturnValue(), SnowyHelper.LAYERS_TO_SHAPE[state.getValue(SnowyHelper.SNOW_LAYERS) - 1]));
        }
    }

    @Inject(method = "getStateForPlacement", at = @At("RETURN"), cancellable = true)
    private void gbw$applyFenceGateSnowPlacementState(BlockPlaceContext ctx, CallbackInfoReturnable<BlockState> cir) {
        BlockState state = ctx.getLevel().getBlockState(ctx.getClickedPos());
        if (state.is(Blocks.SNOW)) {
            cir.setReturnValue(cir.getReturnValue().setValue(SnowyHelper.SNOW_LAYERS, state.getValue(SnowLayerBlock.LAYERS)));
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
        if (context.getItemInHand().is(Items.SNOW) && context.getClickedFace() == Direction.UP && context.isSecondaryUseActive() && state.getValue(SnowyHelper.SNOW_LAYERS) < 8) {
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
