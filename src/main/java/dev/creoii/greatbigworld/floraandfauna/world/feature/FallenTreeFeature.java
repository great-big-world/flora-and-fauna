package dev.creoii.greatbigworld.floraandfauna.world.feature;

import com.mojang.serialization.Codec;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaProperties;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaTags;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class FallenTreeFeature extends Feature<FallenTreeFeatureConfig> {
    public FallenTreeFeature(Codec<FallenTreeFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<FallenTreeFeatureConfig> context) {
        FallenTreeFeatureConfig config = context.config();
        WorldGenLevel world = context.level();
        BlockPos origin = context.origin();
        BlockState state = config.state().getState(context.random(), origin);
        @Nullable BlockState leafState = config.leafState().isPresent() ? config.leafState().get().getState(context.random(), origin) : null;
        int length = config.length().sample(context.random());
        float leafChance = config.leafChance().isPresent() ? config.leafChance().get().sample(context.random()) / 100f : -1f;

        Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(context.random());
        if (state.hasProperty(BlockStateProperties.AXIS)) {
            state = state.setValue(BlockStateProperties.AXIS, direction.getAxis());
        } else if (state.hasProperty(BlockStateProperties.HORIZONTAL_AXIS) && direction.getAxis().isHorizontal()) {
            state = state.setValue(BlockStateProperties.HORIZONTAL_AXIS, direction.getAxis());
        } else if (state.hasProperty(BlockStateProperties.FACING)) {
            state = state.setValue(BlockStateProperties.FACING, direction);
        } else if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING) && direction.getAxis().isHorizontal()) {
            state = state.setValue(BlockStateProperties.HORIZONTAL_FACING, direction);
        }

        List<BlockPos> placementPositions = new ArrayList<>();
        boolean unstable = false;
        for (int i = 0, j = length / 2; i < length; ++i) {
            BlockPos pos = origin.relative(direction, i);
            BlockState downState = world.getBlockState(pos.below());
            if (downState.is(FloraAndFaunaTags.DOES_NOT_SUPPORT_FALLEN_TREES)) {
                --j;
            } else if (j <= 0) {
                j = length / 2;
                unstable = false;
            }

            placementPositions.add(pos);

            BlockState placeState = world.getBlockState(pos);
            if ((!placeState.isAir() && !placeState.canBeReplaced()) || placeState.is(BlockTags.FEATURES_CANNOT_REPLACE)) {
                placementPositions.clear();
                break;
            }
            if (j <= 0) {
                unstable = true;
            }
        }

        if (unstable) {
            return false;
        }

        boolean placeLeaves = context.random().nextFloat() < leafChance && leafState != null;
        for (int i = 0; i < placementPositions.size(); ++i) {
            BlockPos pos = placementPositions.get(i);

            if (state.hasProperty(BlockStateProperties.WATERLOGGED)) {
                state = state.setValue(BlockStateProperties.WATERLOGGED, world.isWaterAt(pos));
            }
            world.setBlock(pos, state, Block.UPDATE_ALL);

            if (context.random().nextFloat() > .075f) {
                BlockState mushroomState = context.random().nextBoolean() ? Blocks.RED_MUSHROOM.defaultBlockState() : Blocks.BROWN_MUSHROOM.defaultBlockState();

                int count = 1;
                for (int j = 0; j < 3; ++j) {
                    if (i < 2 ? context.random().nextBoolean() : context.random().nextInt(3) == 0)
                        ++count;
                }

                world.setBlock(pos.above(), mushroomState.setValue(FloraAndFaunaProperties.MUSHROOMS, count), Block.UPDATE_ALL);
            }

            if (placeLeaves) {
                for (Direction direction1 : Direction.values()) {
                    if (direction1.getAxis() == direction.getAxis())
                        continue;

                    BlockPos offset = pos.relative(direction1);
                    BlockState offsetState = world.getBlockState(offset);
                    if ((offsetState.isAir() || offsetState.canBeReplaced()) && !world.isWaterAt(offset)) {
                        if (i > placementPositions.size() * .66669f && context.random().nextFloat() < leafChance) {
                            world.setBlock(offset, leafState.setValue(LeavesBlock.DISTANCE, 1), 19);
                        }
                    }
                }
            }
        }

        return !placementPositions.isEmpty();
    }
}
