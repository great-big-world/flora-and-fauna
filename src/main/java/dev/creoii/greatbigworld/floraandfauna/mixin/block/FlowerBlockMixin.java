package dev.creoii.greatbigworld.floraandfauna.mixin.block;

import dev.creoii.greatbigworld.block.OverlayState;
import dev.creoii.greatbigworld.floraandfauna.season.Season;
import dev.creoii.greatbigworld.floraandfauna.season.SeasonManager;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaProperties;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaTags;
import dev.creoii.greatbigworld.floraandfauna.util.SnowyHelper;
import net.minecraft.block.*;
import net.minecraft.component.type.SuspiciousStewEffectsComponent;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.Items;
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
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FlowerBlock.class)
public abstract class FlowerBlockMixin extends PlantBlock implements SuspiciousStewIngredient, OverlayState {
    @Unique private static final VoxelShape LARGE_SHAPE = Block.createCuboidShape(3.5d, 0d, 3.5d, 12.5d, 10d, 12.5d);

    protected FlowerBlockMixin(Settings settings) {
        super(settings);
    }

    @Override
    public boolean hasDynamicBounds() {
        return true;
    }

    @Inject(method = "<init>(Lnet/minecraft/component/type/SuspiciousStewEffectsComponent;Lnet/minecraft/block/AbstractBlock$Settings;)V", at = @At("TAIL"))
    private void gbw$setSnowyDefaultState(SuspiciousStewEffectsComponent stewEffects, Settings settings, CallbackInfo ci) {
        BlockState defaultState = getStateManager().getDefaultState();
        if (defaultState.contains(SnowyHelper.SNOW_LAYERS))
            defaultState = defaultState.with(SnowyHelper.SNOW_LAYERS, 0);
        if (defaultState.contains(FloraAndFaunaProperties.FLOWERS))
            defaultState = defaultState.with(FloraAndFaunaProperties.FLOWERS, 1);
        setDefaultState(defaultState);
    }

    @Inject(method = "getOutlineShape", at = @At("RETURN"), cancellable = true)
    private void gbw$mergeSnowShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
        VoxelShape snowShape = VoxelShapes.empty();
        if (SnowyHelper.isSnowy(state)) {
            snowShape = SnowyHelper.getSnowShape(state);
        }
        cir.setReturnValue(VoxelShapes.union(state.get(FloraAndFaunaProperties.FLOWERS) > 2 ? LARGE_SHAPE.offset(state.getModelOffset(pos)) : cir.getReturnValue(), snowShape));
    }

    public boolean canPathfindThrough(BlockState state, NavigationType type) {
        if (type == NavigationType.LAND)
            return state.get(SnowyHelper.SNOW_LAYERS) < 5;
        return false;
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return SnowyHelper.isSnowy(state);
    }

    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        RegistryEntry<Biome> biomeEntry = world.getBiome(pos);
        SeasonManager seasonManager = SeasonManager.getInstance(world.getServer());
        if (world.getLightLevel(LightType.BLOCK, pos) > 11 || (seasonManager.getCurrentSeason() != Season.WINTER && !biomeEntry.isIn(FloraAndFaunaTags.NOT_AFFECTED_BY_WINTER) && biomeEntry.value().doesNotSnow(pos, world.getSeaLevel()))) {
            if (state.get(SnowyHelper.SNOW_LAYERS) > 0)
                dropStacks(Blocks.SNOW.getDefaultState().with(SnowBlock.LAYERS, state.get(SnowyHelper.SNOW_LAYERS)), world, pos);
            world.setBlockState(pos, state.with(SnowyHelper.SNOW_LAYERS, 0));
        }
    }

    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (SnowyHelper.isSnowy(state)) {
            return SnowyHelper.LAYERS_TO_SHAPE[state.get(SnowyHelper.SNOW_LAYERS) - 1];
        }
        return VoxelShapes.empty();
    }

    public VoxelShape getSidesShape(BlockState state, BlockView world, BlockPos pos) {
        if (SnowyHelper.isSnowy(state)) {
            return SnowyHelper.getSnowShape(state);
        }
        return VoxelShapes.empty();
    }

    public VoxelShape getCameraCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (SnowyHelper.isSnowy(state)) {
            return SnowyHelper.getSnowShape(state);
        }
        return VoxelShapes.empty();
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FloraAndFaunaProperties.FLOWERS, SnowyHelper.SNOW_LAYERS);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState state = ctx.getWorld().getBlockState(ctx.getBlockPos());
        if (state.isOf(Blocks.SNOW)) {
            return getDefaultState().with(SnowyHelper.SNOW_LAYERS, state.get(SnowBlock.LAYERS));
        } else if (state.isOf(this)) {
            return state.with(FloraAndFaunaProperties.FLOWERS, Math.min(4, state.get(FloraAndFaunaProperties.FLOWERS) + 1));
        }
        return super.getPlacementState(ctx);
    }

    public boolean canReplace(BlockState state, ItemPlacementContext context) {
        return !context.shouldCancelInteraction() && ((context.getStack().isOf(asItem()) && state.get(FloraAndFaunaProperties.FLOWERS) < 4) || (context.getStack().isOf(Items.SNOW) && state.get(SnowyHelper.SNOW_LAYERS) < 8)) || super.canReplace(state, context);
    }

    @Override
    public BlockState getOverlayState(BlockState state, BlockPos pos, Random random) {
        if (SnowyHelper.isSnowy(state)) {
            return Blocks.SNOW.getDefaultState().with(SnowBlock.LAYERS, state.get(SnowyHelper.SNOW_LAYERS));
        }
        return Blocks.AIR.getDefaultState();
    }
}
