package dev.creoii.greatbigworld.floraandfauna.util;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class SnowyHelper {
    public static final IntegerProperty SNOW_LAYERS = IntegerProperty.create("snow_layers", 0, 8);
    public static final VoxelShape[] LAYERS_TO_SHAPE = new VoxelShape[]{Shapes.empty(), Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 4.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 6.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 10.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 12.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 14.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 16.0)};

    public static boolean isSnowy(BlockState state) {
        return state.getValueOrElse(SNOW_LAYERS, 0) > 0;
    }

    public static VoxelShape getSnowShape(BlockState state) {
        return LAYERS_TO_SHAPE[state.getValue(SNOW_LAYERS)];
    }
}
