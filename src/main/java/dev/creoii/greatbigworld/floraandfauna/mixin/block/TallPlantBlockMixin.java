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
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.VegetationBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DoublePlantBlock.class)
public abstract class TallPlantBlockMixin extends VegetationBlock implements OverlayState {
    @Shadow @Final public static EnumProperty<DoubleBlockHalf> HALF;

    protected TallPlantBlockMixin(Properties settings) {
        super(settings);
    }

    @Override
    public boolean hasDynamicShape() {
        return true;
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void gbw$setSnowyDefaultState(Properties settings, CallbackInfo ci) {
        BlockState defaultState = getStateDefinition().any().setValue(HALF, DoubleBlockHalf.LOWER);
        if (defaultState.hasProperty(SnowyHelper.SNOW_LAYERS))
            defaultState = defaultState.setValue(SnowyHelper.SNOW_LAYERS, 0);
        registerDefaultState(defaultState);
    }

    @Inject(method = "getStateForPlacement", at = @At("RETURN"), cancellable = true)
    private void gbw$applySnowyPlacementState(BlockPlaceContext ctx, CallbackInfoReturnable<BlockState> cir) {
        BlockState state = ctx.getLevel().getBlockState(ctx.getClickedPos());
        if (cir.getReturnValue().getValue(HALF) == DoubleBlockHalf.LOWER) {
            if (state.is(Blocks.SNOW)) {
                cir.setReturnValue(cir.getReturnValue().setValue(SnowyHelper.SNOW_LAYERS, state.getValue(SnowLayerBlock.LAYERS)));
            } else if (state.hasProperty(SnowyHelper.SNOW_LAYERS) && SnowyHelper.isSnowy(state)) {
                cir.setReturnValue(defaultBlockState().setValue(SnowyHelper.SNOW_LAYERS, state.getValue(SnowyHelper.SNOW_LAYERS)));
            }
        }
    }

    @Inject(method = "createBlockStateDefinition", at = @At("TAIL"))
    private void gbw$addSnowyProperty(StateDefinition.Builder<Block, BlockState> builder, CallbackInfo ci) {
        builder.add(SnowyHelper.SNOW_LAYERS);
    }

    @Inject(method = "setPlacedBy", at = @At("HEAD"), cancellable = true)
    private void gbw$fixSnowPlacementBug(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack, CallbackInfo ci) {
        if (SnowyHelper.isSnowy(state) && state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            ci.cancel();
        }
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

            BlockState up = world.getBlockState(pos.above());
            if (state.getValue(HALF) == DoubleBlockHalf.LOWER && SnowyHelper.isSnowy(up)) {
                world.setBlockAndUpdate(pos.above(), up.setValue(SnowyHelper.SNOW_LAYERS, 0));
            }
        }
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        int i = state.getValue(SnowyHelper.SNOW_LAYERS);
        BlockState down = context.getLevel().getBlockState(context.getClickedPos().below());
        if (state.getValue(HALF) == DoubleBlockHalf.LOWER) {
            return context.getItemInHand().is(Items.SNOW) && i < 8;
        } else if (down.is(this) && down.getValue(SnowyHelper.SNOW_LAYERS) == 8) {
            return context.getItemInHand().is(Items.SNOW) && i < 8;
        } else return super.canBeReplaced(state, context);
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        if (type == PathComputationType.LAND)
            return state.getValue(SnowyHelper.SNOW_LAYERS) < 5;
        return false;
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
    public BlockState gbw$getOverlayState(BlockState state, BlockPos pos, RandomSource random) {
        if (SnowyHelper.isSnowy(state)) {
            return Blocks.SNOW.defaultBlockState().setValue(SnowLayerBlock.LAYERS, state.getValue(SnowyHelper.SNOW_LAYERS));
        }
        return Blocks.AIR.defaultBlockState();
    }
}
