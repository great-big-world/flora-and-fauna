package dev.creoii.greatbigworld.floraandfauna.block;

import dev.creoii.greatbigworld.block.AdjacentCollision;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class HollowLogBlock extends PillarBlock implements Waterloggable, AdjacentCollision {
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
    public void onAdjacentEntityCollision(Entity entity, BlockState state, BlockPos pos) {
        if (!entity.isSprinting() || entity.hasPassengers() || entity.hasVehicle() || entity.isCrawling())
            return;
        BlockPos difference = pos.subtract(entity.getBlockPos());
        if (Math.abs(difference.getY()) > .15d || difference.equals(BlockPos.ORIGIN))
            return;
        Vec3d vecDifference = pos.toBottomCenterPos().subtract(entity.getEntityPos());
        boolean canEnter = switch (state.get(AXIS)) {
            case Y -> false;
            case X -> Math.abs(vecDifference.getZ()) < .1d && difference.getX() == entity.getHorizontalFacing().getOffsetX();
            case Z -> Math.abs(vecDifference.getX()) < .1d && difference.getZ() == entity.getHorizontalFacing().getOffsetZ();
        };

        if (canEnter) {
            BlockPos entrancePos = BlockPos.ofFloored(entity.getEntityPos()).offset(state.get(AXIS) == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X, 0);
            VoxelShape shape = entity.getEntityWorld().getBlockState(entrancePos).getCollisionShape(entity.getEntityWorld(), entrancePos);
            final double y = shape.isEmpty() ? 0d : shape.getMax(Direction.Axis.Y);
            if (y >= .375d)
                return;

            Vec3d target = pos.toBottomCenterPos();
            Vec3d direction = entity.getEntityPos().subtract(target).normalize().multiply(.5d);
            Vec3d destination = target.add(direction.x, Math.min(.2d, y), direction.z);

            entity.setPose(EntityPose.SWIMMING);
            entity.setSwimming(true);
            entity.setPos(destination.x, destination.y, destination.z);
        }
    }
}
