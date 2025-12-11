package dev.creoii.greatbigworld.floraandfauna.block;

import com.mojang.serialization.MapCodec;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.IceBlock;
import net.minecraft.world.level.block.VegetationBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class AlgaeBlock extends VegetationBlock {
    public static final IntegerProperty DENSITY = IntegerProperty.create("density", 1, 3);
    public static final IntegerProperty MAX_DENSITY = IntegerProperty.create("max_density", 1, 3);
    public static final BooleanProperty COLLIDED = BooleanProperty.create("collided");
    public static final MapCodec<AlgaeBlock> CODEC = AlgaeBlock.simpleCodec(AlgaeBlock::new);
    public static final VoxelShape SHAPE = Block.box(0d, 0d, 0d, 16d, 1.5d, 16d);

    public AlgaeBlock(Properties settings) {
        super(settings);
        registerDefaultState(getStateDefinition().any().setValue(DENSITY, 3).setValue(MAX_DENSITY, 3).setValue(COLLIDED, false));
    }

    @Override
    protected MapCodec<? extends AlgaeBlock> codec() {
        return CODEC;
    }

    @Override
    protected void entityInside(BlockState state, Level world, BlockPos pos, Entity entity, InsideBlockEffectApplier handler, boolean bl) {
        int maxDensity = state.getValue(MAX_DENSITY);
        int density = state.getValue(DENSITY);
        if (!entity.getType().is(FloraAndFaunaTags.IGNORES_ALGAE)) {
            if (density == 2) {
                entity.makeStuckInBlock(state, new Vec3(.75d, .95d, .75d));
            } else if (density == 3) {
                entity.makeStuckInBlock(state, new Vec3(.5d, .75d, .5d));
            }
        }

        if (density > 1) {
            world.setBlock(pos, state.setValue(DENSITY, Math.max(1, density - 1)), 3);
            if (!state.getValue(COLLIDED)) {
                world.scheduleTick(pos, state.getBlock(), 80);
            }
        }
    }

    @Override
    protected void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (!state.getValue(COLLIDED)) {
            int density = state.getValue(DENSITY);
            world.setBlock(pos, state.setValue(DENSITY, Math.min(3, density + 1)), 3);
            if (state.getValue(DENSITY) < 3) {
                world.scheduleTick(pos, state.getBlock(), 80);
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DENSITY, MAX_DENSITY, COLLIDED);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    protected boolean mayPlaceOn(BlockState floor, BlockGetter world, BlockPos pos) {
        return (world.getFluidState(pos).getType() == Fluids.WATER || floor.getBlock() instanceof IceBlock) && world.getFluidState(pos.above()).getType() == Fluids.EMPTY;
    }
}
