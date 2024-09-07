package dev.creoii.greatbigworld.floraandfauna.block;

import dev.creoii.creoapi.api.block.CreoBlock;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class HollowLogBlock extends PillarBlock implements CreoBlock, Waterloggable {
    private static final VoxelShape X_SHAPE = VoxelShapes.union(Block.createCuboidShape(0d, 0d, 0d, 16d, 16d, 3d), Block.createCuboidShape(0d, 13d, 0d, 16d, 16d, 16d), Block.createCuboidShape(0d, 0d, 13d, 16d, 16d, 16d), Block.createCuboidShape(0d, 0d, 0d, 16d, 3d, 16d));
    private static final VoxelShape Y_SHAPE = VoxelShapes.union(Block.createCuboidShape(0d, 0d, 0d, 16d, 16d, 3d), Block.createCuboidShape(0d, 0d, 0d, 3d, 16d, 16d), Block.createCuboidShape(0d, 0d, 13d, 16d, 16d, 16d), Block.createCuboidShape(13d, 0d, 0d, 16d, 16d, 16d));
    private static final VoxelShape Z_SHAPE = VoxelShapes.union(Block.createCuboidShape(13d, 0d, 0d, 16d, 16d, 16d), Block.createCuboidShape(0d, 0d, 0d, 3d, 16d, 16d), Block.createCuboidShape(0d, 13d, 0d, 16d, 16d, 16d), Block.createCuboidShape(0d, 0d, 0d, 16d, 3d, 16d));
    private static final VoxelShape X_COLLISION_SHAPE = VoxelShapes.union(Block.createCuboidShape(0d, 0d, 0d, 16d, 16d, 2d), Block.createCuboidShape(0d, 13.75d, 0d, 16d, 16d, 16d), Block.createCuboidShape(0d, 0d, 14d, 16d, 16d, 16d), Block.createCuboidShape(0d, 0d, 0d, 16d, 2.25d, 16d));
    private static final VoxelShape Y_COLLISION_SHAPE = VoxelShapes.union(Block.createCuboidShape(0d, 0d, 0d, 16d, 16d, 2.25d), Block.createCuboidShape(0d, 0d, 0d, 2.25d, 16d, 16d), Block.createCuboidShape(0d, 0d, 13.75d, 16d, 16d, 16d), Block.createCuboidShape(13.75d, 0d, 0d, 16d, 16d, 16d));
    private static final VoxelShape Z_COLLISION_SHAPE = VoxelShapes.union(Block.createCuboidShape(14d, 0d, 0d, 16d, 16d, 16d), Block.createCuboidShape(0d, 0d, 0d, 2d, 16d, 16d), Block.createCuboidShape(0d, 13.75d, 0d, 16d, 16d, 16d), Block.createCuboidShape(0d, 0d, 0d, 16d, 2.25d, 16d));
    private static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    public HollowLogBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(AXIS, Direction.Axis.Y).with(WATERLOGGED, false));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(AXIS)) {
            case X -> X_SHAPE;
            case Y -> Y_SHAPE;
            case Z -> Z_SHAPE;
        };
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(AXIS)) {
            case X -> X_COLLISION_SHAPE;
            case Y -> Y_COLLISION_SHAPE;
            case Z -> Z_COLLISION_SHAPE;
        };
    }

    @Override
    public VoxelShape getRaycastShape(BlockState state, BlockView world, BlockPos pos) {
        return VoxelShapes.fullCube();
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AXIS, WATERLOGGED);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState().with(AXIS, ctx.getSide().getAxis()).with(WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).isOf(Fluids.WATER));
    }

    @Override
    public boolean canEntityCollideAdjacent(Entity entity, BlockState state, BlockPos pos) {
        if (entity.getWorld().isClient || entity.isInPose(EntityPose.SWIMMING) || !entity.isSprinting() || entity.hasPassengers())
            return false;
        BlockPos difference = pos.subtract(entity.getBlockPos());
        if (difference.getY() > .5d || difference.getY() < -.5d || difference.equals(BlockPos.ORIGIN))
            return false;
        Vec3i facingVec = entity.getHorizontalFacing().getVector();
        return switch (state.get(AXIS)) {
            case X -> difference.getX() != 0d && difference.getZ() == 0d && difference.getX() == facingVec.getX();
            case Z -> difference.getZ() != 0d && difference.getX() == 0d && difference.getZ() == facingVec.getZ();
            case Y -> false;
        };
    }

    @Override
    public void onAdjacentEntityCollision(Entity entity, BlockState state, BlockPos pos) {
        entity.setSwimming(true);
        entity.setPose(EntityPose.SWIMMING);
    }
}
