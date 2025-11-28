package dev.creoii.greatbigworld.floraandfauna.mixin.block;

import dev.creoii.greatbigworld.block.OverlayState;
import dev.creoii.greatbigworld.floraandfauna.season.Season;
import dev.creoii.greatbigworld.floraandfauna.season.SeasonManager;
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
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FireflyBushBlock.class)
public abstract class FireflyBushBlockMixin extends PlantBlock implements OverlayState {
    protected FireflyBushBlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void gbw$setSnowyDefaultState(Settings settings, CallbackInfo ci) {
        setDefaultState(getStateManager().getDefaultState().with(SnowyHelper.SNOW_LAYERS, 0));
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (SnowyHelper.isSnowy(state)) {
            return VoxelShapes.union(super.getOutlineShape(state, world, pos, context), SnowyHelper.getSnowShape(state));
        }
        return super.getOutlineShape(state, world, pos, context);
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (SnowyHelper.isSnowy(state)) {
            return SnowyHelper.LAYERS_TO_SHAPE[state.get(SnowyHelper.SNOW_LAYERS) - 1];
        }
        return super.getCollisionShape(state, world, pos, context);
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState blockState = super.getPlacementState(ctx);
        BlockState state = ctx.getWorld().getBlockState(ctx.getBlockPos());

        if (state.isOf(Blocks.SNOW)) {
            return blockState.with(SnowyHelper.SNOW_LAYERS, state.get(SnowBlock.LAYERS));
        } else if (state.contains(SnowyHelper.SNOW_LAYERS) && SnowyHelper.isSnowy(state)) {
            return state.with(SnowyHelper.SNOW_LAYERS, state.get(SnowyHelper.SNOW_LAYERS));
        }

        return blockState;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(SnowyHelper.SNOW_LAYERS);
    }

    @Override
    protected boolean hasRandomTicks(BlockState state) {
        return SnowyHelper.isSnowy(state);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        RegistryEntry<Biome> biomeEntry = world.getBiome(pos);
        SeasonManager seasonManager = SeasonManager.getInstance(world.getServer());
        if (seasonManager != null && world.getDimensionEntry().isIn(FloraAndFaunaTags.AFFECTED_BY_SEASONS) && world.getLightLevel(LightType.BLOCK, pos) > 11 || (seasonManager.getCurrentSeason() != Season.WINTER && !biomeEntry.isIn(FloraAndFaunaTags.NOT_AFFECTED_BY_WINTER) && biomeEntry.value().doesNotSnow(pos, world.getSeaLevel()))) {
            if (state.get(SnowyHelper.SNOW_LAYERS) > 0)
                dropStacks(Blocks.SNOW.getDefaultState().with(SnowBlock.LAYERS, state.get(SnowyHelper.SNOW_LAYERS)), world, pos);
            world.setBlockState(pos, state.with(SnowyHelper.SNOW_LAYERS, 0));
        }
    }

    @Override
    public BlockState gbw$getOverlayState(BlockState state, BlockPos pos, Random random) {
        if (SnowyHelper.isSnowy(state)) {
            return Blocks.SNOW.getDefaultState().with(SnowBlock.LAYERS, state.get(SnowyHelper.SNOW_LAYERS));
        }
        return Blocks.AIR.getDefaultState();
    }
}
