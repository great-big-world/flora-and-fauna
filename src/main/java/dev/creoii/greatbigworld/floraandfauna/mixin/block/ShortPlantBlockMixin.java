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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.TallGrassBlock;
import net.minecraft.world.level.block.VegetationBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TallGrassBlock.class)
public abstract class ShortPlantBlockMixin extends VegetationBlock implements BonemealableBlock, OverlayState {
    protected ShortPlantBlockMixin(Properties settings) {
        super(settings);
    }

    @Override
    public boolean hasDynamicShape() {
        return true;
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void gbw$setSnowyDefaultState(Properties settings, CallbackInfo ci) {
        BlockState defaultState = getStateDefinition().any();
        if (defaultState.hasProperty(SnowyHelper.SNOW_LAYERS))
            defaultState = defaultState.setValue(SnowyHelper.SNOW_LAYERS, 0);
        registerDefaultState(defaultState);
    }

    @Inject(method = "getShape", at = @At("RETURN"), cancellable = true)
    private void gbw$mergeSnowShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if (SnowyHelper.isSnowy(state)) {
            cir.setReturnValue(Shapes.or(cir.getReturnValue(), SnowyHelper.getSnowShape(state)));
        }
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return SnowyHelper.isSnowy(state);
    }

    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        Holder<Biome> biomeEntry = world.getBiome(pos);
        SeasonManager seasonManager = SeasonManager.getInstance(world.getServer());
        if (seasonManager != null && world.dimensionTypeRegistration().is(FloraAndFaunaTags.AFFECTED_BY_SEASONS) && world.getBrightness(LightLayer.BLOCK, pos) > 11 || (seasonManager.getCurrentSeason() != Season.WINTER && !biomeEntry.is(FloraAndFaunaTags.NOT_AFFECTED_BY_WINTER) && biomeEntry.value().warmEnoughToRain(pos, world.getSeaLevel()))) {
            dropResources(state, world, pos);
            world.setBlockAndUpdate(pos, state.setValue(SnowyHelper.SNOW_LAYERS, 0));
        }
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        int i = state.getValue(SnowyHelper.SNOW_LAYERS);
        if (i > 0) {
            if (context.getItemInHand().is(Items.SNOW) && i < 8) {
                return true;
            } else return i == 1;
        }
        return super.canBeReplaced(state, context);
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        if (type == PathComputationType.LAND)
            return state.getValue(SnowyHelper.SNOW_LAYERS) < 5;
        return false;
    }

    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        if (SnowyHelper.isSnowy(state)) {
            return SnowyHelper.LAYERS_TO_SHAPE[state.getValue(SnowyHelper.SNOW_LAYERS) - 1];
        }
        return Shapes.empty();
    }

    public VoxelShape getBlockSupportShape(BlockState state, BlockGetter world, BlockPos pos) {
        if (SnowyHelper.isSnowy(state)) {
            return SnowyHelper.getSnowShape(state);
        }
        return Shapes.empty();
    }

    public VoxelShape getVisualShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        if (SnowyHelper.isSnowy(state)) {
            return SnowyHelper.getSnowShape(state);
        }
        return Shapes.empty();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SnowyHelper.SNOW_LAYERS);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Level world = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        BlockState state = world.getBlockState(pos);
        if (state.is(Blocks.SNOW)) {
            return defaultBlockState().setValue(SnowyHelper.SNOW_LAYERS, state.getValue(SnowLayerBlock.LAYERS));
        }

        return super.getStateForPlacement(ctx);
    }

    @Override
    public BlockState gbw$getOverlayState(BlockState state, BlockPos pos, RandomSource random) {
        if (SnowyHelper.isSnowy(state)) {
            return Blocks.SNOW.defaultBlockState().setValue(SnowLayerBlock.LAYERS, state.getValue(SnowyHelper.SNOW_LAYERS));
        }
        return Blocks.AIR.defaultBlockState();
    }
}
