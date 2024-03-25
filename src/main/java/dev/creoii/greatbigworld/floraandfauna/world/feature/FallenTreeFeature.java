package dev.creoii.greatbigworld.floraandfauna.world.feature;

import com.mojang.serialization.Codec;
import dev.creoii.greatbigworld.floraandfauna.block.MossCarpetBlock;
import dev.creoii.greatbigworld.floraandfauna.registry.FloraAndFaunaBlocks;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaTags;
import dev.creoii.greatbigworld.floraandfauna.util.SnowyHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.ArrayList;
import java.util.List;

public class FallenTreeFeature extends Feature<FallenTreeFeatureConfig> {
    public FallenTreeFeature(Codec<FallenTreeFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<FallenTreeFeatureConfig> context) {
        FallenTreeFeatureConfig config = context.getConfig();
        StructureWorldAccess world = context.getWorld();
        BlockPos origin = context.getOrigin();
        BlockState state = config.state().get(context.getRandom(), origin);
        int length = config.length().get(context.getRandom());
        float mossChance = config.mossChance().get(context.getRandom()) / 100f;

        Direction direction = Direction.Type.HORIZONTAL.random(context.getRandom());
        if (state.contains(Properties.AXIS)) {
            state = state.with(Properties.AXIS, direction.getAxis());
        } else if (state.contains(Properties.HORIZONTAL_AXIS) && direction.getAxis().isHorizontal()) {
            state = state.with(Properties.HORIZONTAL_AXIS, direction.getAxis());
        } else if (state.contains(Properties.FACING)) {
            state = state.with(Properties.FACING, direction);
        } else if (state.contains(Properties.HORIZONTAL_FACING) && direction.getAxis().isHorizontal()) {
            state = state.with(Properties.HORIZONTAL_FACING, direction);
        }

        List<BlockPos> placementPositions = new ArrayList<>();
        boolean unstable = false;
        for (int i = 0, j = length / 2; i < length; ++i) {
            BlockPos pos = origin.offset(direction, i);
            BlockState downState = world.getBlockState(pos.down());
            if (downState.isIn(FloraAndFaunaTags.DOES_NOT_SUPPORT_FALLEN_TREES)) {
                --j;
            } else if (j <= 0) {
                j = length / 2;
                unstable = false;
            }

            placementPositions.add(pos);

            BlockState placeState = world.getBlockState(pos);
            if ((!placeState.isAir() && !placeState.isReplaceable()) || placeState.isIn(BlockTags.FEATURES_CANNOT_REPLACE)) {
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

        for (BlockPos pos : placementPositions) {
            world.setBlockState(pos, state, Block.NOTIFY_ALL);

            if (context.getRandom().nextFloat() > mossChance)
                continue;

            for (Direction direction1 : Direction.values()) {
                if (direction1.getAxis() == direction.getAxis())
                    continue;

                BlockPos offset = pos.offset(direction1);
                BlockState offsetState = world.getBlockState(offset);
                if ((offsetState.isAir() || offsetState.isReplaceable()) && !world.isWater(offset)) {
                    world.setBlockState(offset, FloraAndFaunaBlocks.MOSS_CARPET.getDefaultState().with(MossCarpetBlock.getProperty(direction1.getOpposite()), true).with(SnowyHelper.SNOW_LAYERS, world.getBlockState(offset).isOf(Blocks.SNOW) ? 1 : 0), 19);
                }
            }
        }

        return !placementPositions.isEmpty();
    }
}
