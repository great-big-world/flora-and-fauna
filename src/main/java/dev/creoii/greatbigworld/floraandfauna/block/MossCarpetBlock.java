package dev.creoii.greatbigworld.floraandfauna.block;

import com.mojang.serialization.MapCodec;
import dev.creoii.greatbigworld.floraandfauna.client.FloraAndFaunaClient;
import dev.creoii.greatbigworld.floraandfauna.season.Season;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaTags;
import dev.creoii.greatbigworld.floraandfauna.util.SnowyHelper;
import net.minecraft.block.*;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.LightType;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Nullable;

public class MossCarpetBlock extends MultifaceGrowthBlock {
    public static final MapCodec<MossCarpetBlock> CODEC = createCodec(MossCarpetBlock::new);
    private final LichenGrower grower = new LichenGrower(this);

    public MossCarpetBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(Properties.NORTH, false).with(Properties.SOUTH, false).with(Properties.EAST, false).with(Properties.WEST, false).with(Properties.UP, false).with(Properties.DOWN, false).with(SnowyHelper.SNOW_LAYERS, 0));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        VoxelShape shape = super.getOutlineShape(state, world, pos, context);
        if (SnowyHelper.isSnowy(state)) {
            return VoxelShapes.union(shape, SnowyHelper.LAYERS_TO_SHAPE[state.get(SnowyHelper.SNOW_LAYERS) - 1]);
        }
        return shape;
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return SnowyHelper.isSnowy(state);
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

    @Override
    public boolean canReplace(BlockState state, ItemPlacementContext context) {
        if (super.canReplace(state, context))
            return true;
        return context.getStack().isOf(Items.SNOW);
    }

    @SuppressWarnings("deprecation")
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        if (type == NavigationType.LAND)
            return state.get(SnowyHelper.SNOW_LAYERS) < 5;
        return false;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(SnowyHelper.SNOW_LAYERS);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState placementState = super.getPlacementState(ctx);
        BlockState state = ctx.getWorld().getBlockState(ctx.getBlockPos());
        if (placementState != null && state.getBlock() instanceof SnowBlock) {
            return placementState.with(SnowyHelper.SNOW_LAYERS, state.get(Properties.LAYERS));
        }
        return placementState;
    }

    @Override
    protected MapCodec<? extends MultifaceGrowthBlock> getCodec() {
        return CODEC;
    }

    @Override
    public LichenGrower getGrower() {
        return grower;
    }
}
