package dev.creoii.greatbigworld.floraandfauna.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

public final class SnowyHelper {
    public static final IntProperty SNOW_LAYERS = IntProperty.of("snow_layers", 0, 8);
    public static final VoxelShape[] LAYERS_TO_SHAPE = new VoxelShape[]{VoxelShapes.empty(), Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 4.0, 16.0), Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 6.0, 16.0), Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 8.0, 16.0), Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 10.0, 16.0), Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 12.0, 16.0), Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 14.0, 16.0), Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 16.0)};

    public static boolean isSnowy(BlockState state) {
        return state.contains(SNOW_LAYERS) && state.get(SNOW_LAYERS) > 0;
    }

    public static VoxelShape getSnowShape(BlockState state) {
        return LAYERS_TO_SHAPE[state.get(SNOW_LAYERS)];
    }

    public static void tickSnowUnderLeaves(World world, BlockState state, BlockPos pos) {
        if (state.get(SNOW_LAYERS) == 0) {
            int i = state.get(SNOW_LAYERS);
            if (i < Math.min(world.getGameRules().getInt(GameRules.SNOW_ACCUMULATION_HEIGHT), 8)) {
                BlockState blockState2 = state.with(SNOW_LAYERS, i + 1);
                Block.pushEntitiesUpBeforeBlockChange(state, blockState2, world, pos);
                world.setBlockState(pos, blockState2);
            }
        }
    }
}
