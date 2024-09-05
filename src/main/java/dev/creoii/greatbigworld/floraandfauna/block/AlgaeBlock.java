package dev.creoii.greatbigworld.floraandfauna.block;

import com.mojang.serialization.MapCodec;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaTags;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class AlgaeBlock extends PlantBlock {
    public static final IntProperty DENSITY = IntProperty.of("density", 1, 3);
    public static final IntProperty MAX_DENSITY = IntProperty.of("max_density", 1, 3);
    public static final BooleanProperty COLLIDED = BooleanProperty.of("collided");
    public static final MapCodec<AlgaeBlock> CODEC = AlgaeBlock.createCodec(AlgaeBlock::new);
    public static final VoxelShape SHAPE = Block.createCuboidShape(0d, 0d, 0d, 16d, 1.5d, 16d);

    public AlgaeBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(DENSITY, 3).with(MAX_DENSITY, 3).with(COLLIDED, false));
    }

    @Override
    protected MapCodec<? extends AlgaeBlock> getCodec() {
        return CODEC;
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        int maxDensity = state.get(MAX_DENSITY);
        int density = state.get(DENSITY);
        if (!entity.getType().isIn(FloraAndFaunaTags.IGNORES_ALGAE)) {
            if (density == 2) {
                entity.slowMovement(state, new Vec3d(.75d, .95d, .75d));
            } else if (density == 3) {
                entity.slowMovement(state, new Vec3d(.5d, .75d, .5d));
            }
        }

        if (density > 1) {
            world.setBlockState(pos, state.with(DENSITY, Math.max(1, density - 1)), 3);
            if (!state.get(COLLIDED)) {
                world.scheduleBlockTick(pos, state.getBlock(), 80);
            }
        }
    }

    @Override
    protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!state.get(COLLIDED)) {
            int density = state.get(DENSITY);
            world.setBlockState(pos, state.with(DENSITY, Math.min(3, density + 1)), 3);
            if (state.get(DENSITY) < 3) {
                world.scheduleBlockTick(pos, state.getBlock(), 80);
            }
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(DENSITY, MAX_DENSITY, COLLIDED);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.empty();
    }

    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return (world.getFluidState(pos).getFluid() == Fluids.WATER || floor.getBlock() instanceof IceBlock) && world.getFluidState(pos.up()).getFluid() == Fluids.EMPTY;
    }
}
