package dev.creoii.greatbigworld.floraandfauna.block;

import com.mojang.serialization.MapCodec;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaTags;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
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
    public static final MapCodec<AlgaeBlock> CODEC = AlgaeBlock.createCodec(AlgaeBlock::new);
    public static final VoxelShape SHAPE = Block.createCuboidShape(0d, 0d, 0d, 16d, 1.5d, 16d);

    public AlgaeBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(DENSITY, 3));
    }

    @Override
    protected MapCodec<? extends PlantBlock> getCodec() {
        return CODEC;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (entity.getType().isIn(FloraAndFaunaTags.IGNORES_ALGAE))
            return;
        int density = state.get(DENSITY);
        if (density == 2) {
            entity.slowMovement(state, new Vec3d(.75d, .95d, .75d));
            world.setBlockState(pos, state.with(DENSITY, 1), 3);
        } else if (density == 3) {
            entity.slowMovement(state, new Vec3d(.5d, .75d, .5d));
            world.setBlockState(pos, state.with(DENSITY, 2), 3);
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(DENSITY);
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.empty();
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return state.get(DENSITY) < 3;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        int density = state.get(DENSITY);
        if (density == 2) {
            world.setBlockState(pos, state.with(DENSITY, 3));
        } else if (density == 1) {
            world.setBlockState(pos, state.with(DENSITY, 2));
        }
    }

    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        FluidState fluidState = world.getFluidState(pos);
        FluidState fluidState2 = world.getFluidState(pos.up());
        return (fluidState.getFluid() == Fluids.WATER || floor.getBlock() instanceof IceBlock) && fluidState2.getFluid() == Fluids.EMPTY;
    }
}
