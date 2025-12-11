package dev.creoii.greatbigworld.floraandfauna.mixin.block;

import dev.creoii.greatbigworld.block.OverlayState;
import dev.creoii.greatbigworld.floraandfauna.season.Season;
import dev.creoii.greatbigworld.floraandfauna.season.SeasonManager;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaProperties;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaTags;
import dev.creoii.greatbigworld.floraandfauna.util.SnowyHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.MushroomBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.VegetationBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MushroomBlock.class)
public abstract class MushroomPlantBlockMixin extends VegetationBlock implements BonemealableBlock, OverlayState {
    @Unique
    private static final VoxelShape LARGE_SHAPE = Block.box(3.5d, 0d, 3.5d, 12.5d, 10d, 12.5d);

    protected MushroomPlantBlockMixin(Properties settings) {
        super(settings);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void gbw$setSnowyDefaultState(ResourceKey<ConfiguredFeature<?, ?>> featureKey, Properties settings, CallbackInfo ci) {
        BlockState defaultState = getStateDefinition().any();
        if (defaultState.hasProperty(SnowyHelper.SNOW_LAYERS))
            defaultState = defaultState.setValue(SnowyHelper.SNOW_LAYERS, 0);
        if (defaultState.hasProperty(FloraAndFaunaProperties.MUSHROOMS))
            defaultState = defaultState.setValue(FloraAndFaunaProperties.MUSHROOMS, 1);
        registerDefaultState(defaultState);
    }

    @Inject(method = "getShape", at = @At("RETURN"), cancellable = true)
    private void gbw$mergeSnowShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> cir) {
        VoxelShape snowShape = Shapes.empty();
        if (SnowyHelper.isSnowy(state)) {
            snowShape = SnowyHelper.getSnowShape(state);
        }
        cir.setReturnValue(Shapes.or(state.getValue(FloraAndFaunaProperties.MUSHROOMS) > 2 ? LARGE_SHAPE : cir.getReturnValue(), snowShape));
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        if (type == PathComputationType.LAND)
            return state.getValue(SnowyHelper.SNOW_LAYERS) < 5;
        return false;
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
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
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        if (SnowyHelper.isSnowy(state)) {
            return SnowyHelper.LAYERS_TO_SHAPE[state.getValue(SnowyHelper.SNOW_LAYERS) - 1];
        }
        return Shapes.empty();
    }

    @Override
    public VoxelShape getBlockSupportShape(BlockState state, BlockGetter world, BlockPos pos) {
        if (SnowyHelper.isSnowy(state)) {
            return SnowyHelper.getSnowShape(state);
        }
        return Shapes.empty();
    }

    @Override
    public VoxelShape getVisualShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        if (SnowyHelper.isSnowy(state)) {
            return SnowyHelper.getSnowShape(state);
        }
        return Shapes.empty();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FloraAndFaunaProperties.MUSHROOMS, SnowyHelper.SNOW_LAYERS);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState state = ctx.getLevel().getBlockState(ctx.getClickedPos());
        if (state.is(Blocks.SNOW)) {
            return defaultBlockState().setValue(SnowyHelper.SNOW_LAYERS, state.getValue(SnowLayerBlock.LAYERS));
        } else if (state.is(this)) {
            return state.setValue(FloraAndFaunaProperties.MUSHROOMS, Math.min(4, state.getValue(FloraAndFaunaProperties.MUSHROOMS) + 1));
        } else if (state.hasProperty(SnowyHelper.SNOW_LAYERS) && SnowyHelper.isSnowy(state)) {
            return defaultBlockState().setValue(SnowyHelper.SNOW_LAYERS, state.getValue(SnowyHelper.SNOW_LAYERS));
        }
        return super.getStateForPlacement(ctx);
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        return !context.isSecondaryUseActive() && ((context.getItemInHand().is(asItem()) && state.getValue(FloraAndFaunaProperties.MUSHROOMS) < 4) || (context.getItemInHand().is(Items.SNOW) && state.getValue(SnowyHelper.SNOW_LAYERS) < 8)) || super.canBeReplaced(state, context);
    }

    @Override
    public BlockState gbw$getOverlayState(BlockState state, BlockPos pos, RandomSource random) {
        if (SnowyHelper.isSnowy(state)) {
            return Blocks.SNOW.defaultBlockState().setValue(SnowLayerBlock.LAYERS, state.getValue(SnowyHelper.SNOW_LAYERS));
        }
        return Blocks.AIR.defaultBlockState();
    }
}
