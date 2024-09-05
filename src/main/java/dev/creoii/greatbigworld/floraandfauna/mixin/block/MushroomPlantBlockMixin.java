package dev.creoii.greatbigworld.floraandfauna.mixin.block;

import dev.creoii.creoapi.api.block.CreoBlock;
import dev.creoii.greatbigworld.floraandfauna.season.Season;
import dev.creoii.greatbigworld.floraandfauna.season.SeasonManager;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaTags;
import dev.creoii.greatbigworld.floraandfauna.util.SnowyHelper;
import net.minecraft.block.*;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.LightType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MushroomPlantBlock.class)
public abstract class MushroomPlantBlockMixin extends PlantBlock implements Fertilizable, CreoBlock {
    @Unique
    private static final IntProperty MUSHROOMS = IntProperty.of("mushrooms", 1, 4);
    @Unique
    private static final VoxelShape LARGE_SHAPE = Block.createCuboidShape(3.5d, 0d, 3.5d, 12.5d, 10d, 12.5d);

    protected MushroomPlantBlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void gbw$setSnowyDefaultState(RegistryKey<ConfiguredFeature<?, ?>> featureKey, Settings settings, CallbackInfo ci) {
        setDefaultState(getStateManager().getDefaultState().with(MUSHROOMS, 1).with(SnowyHelper.SNOW_LAYERS, 0));
    }

    @Inject(method = "getOutlineShape", at = @At("RETURN"), cancellable = true)
    private void gbw$mergeSnowShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
        VoxelShape snowShape = VoxelShapes.empty();
        if (SnowyHelper.isSnowy(state)) {
            snowShape = SnowyHelper.getSnowShape(state);
        }
        cir.setReturnValue(VoxelShapes.union(state.get(MUSHROOMS) > 2 ? LARGE_SHAPE : cir.getReturnValue(), snowShape));
    }

    @Override
    protected boolean canPathfindThrough(BlockState state, NavigationType type) {
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
        if (world.getLightLevel(LightType.BLOCK, pos) > 11 || (seasonManager.getCurrentSeason() != Season.WINTER && !biomeEntry.isIn(FloraAndFaunaTags.NOT_AFFECTED_BY_WINTER) && biomeEntry.value().doesNotSnow(pos))) {
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
        builder.add(MUSHROOMS, SnowyHelper.SNOW_LAYERS);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState state = ctx.getWorld().getBlockState(ctx.getBlockPos());
        if (state.isOf(Blocks.SNOW)) {
            return getDefaultState().with(SnowyHelper.SNOW_LAYERS, state.get(SnowBlock.LAYERS));
        } else if (state.isOf(this)) {
            return state.with(MUSHROOMS, Math.min(4, state.get(MUSHROOMS) + 1));
        }
        return super.getPlacementState(ctx);
    }

    public boolean canReplace(BlockState state, ItemPlacementContext context) {
        return !context.shouldCancelInteraction() && ((context.getStack().isOf(asItem()) && state.get(MUSHROOMS) < 4) || (context.getStack().isOf(Items.SNOW) && state.get(SnowyHelper.SNOW_LAYERS) < 8)) || super.canReplace(state, context);
    }

    @Override
    public BlockState getOverlayState(BlockState state, BlockPos pos, Random random) {
        if (SnowyHelper.isSnowy(state)) {
            return Blocks.SNOW.getDefaultState().with(SnowBlock.LAYERS, state.get(SnowyHelper.SNOW_LAYERS));
        }
        return Blocks.AIR.getDefaultState();
    }
}
