package dev.creoii.greatbigworld.floraandfauna.world.decorator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import dev.creoii.greatbigworld.floraandfauna.registry.FloraAndFaunaBlocks;
import dev.creoii.greatbigworld.floraandfauna.registry.FloraAndFaunaTreeDecoratorTypes;
import net.minecraft.block.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;
import org.apache.commons.lang3.mutable.MutableInt;

public class MossTreeDecorator extends TreeDecorator {
    public static final MapCodec<MossTreeDecorator> CODEC = Codec.floatRange(0f, 1f).fieldOf("probability").xmap(MossTreeDecorator::new, decorator -> {
        return decorator.probability;
    });
    private final float probability;

    public MossTreeDecorator(float probability) {
        this.probability = probability;
    }

    @Override
    protected TreeDecoratorType<?> getType() {
        return FloraAndFaunaTreeDecoratorTypes.MOSS;
    }

    @Override
    public void generate(TreeDecorator.Generator generator) {
        Random random = generator.getRandom();
        MutableInt noise = new MutableInt(random.nextInt(5) - 2);
        MutableInt noiseProbability = new MutableInt();
        TestableWorld world = generator.getWorld();
        for (BlockPos pos : generator.getLogPositions()) {
            if (random.nextFloat() > probability)
                continue;

            BlockPos blockPos;
            noiseProbability.setValue(random.nextInt(3) - 1);
            if (noise.getValue() > 0) {
                blockPos = pos.west();
                if (generator.isAir(blockPos) || world.testBlockState(blockPos, state -> state.isOf(Blocks.VINE))) {
                    generator.replace(blockPos, FloraAndFaunaBlocks.MOSS_CARPET.getDefaultState().with(Properties.FACING, Direction.EAST));
                }
            }

            if (noise.getValue() > 0) {
                blockPos = pos.east();
                if (generator.isAir(blockPos) || world.testBlockState(blockPos, state -> state.isOf(Blocks.VINE))) {
                    generator.replace(blockPos, FloraAndFaunaBlocks.MOSS_CARPET.getDefaultState().with(Properties.FACING, Direction.WEST));
                }
            }

            if (noise.getValue() > 0) {
                blockPos = pos.north();
                if (generator.isAir(blockPos) || world.testBlockState(blockPos, state -> state.isOf(Blocks.VINE))) {
                    generator.replace(blockPos, FloraAndFaunaBlocks.MOSS_CARPET.getDefaultState().with(Properties.FACING, Direction.SOUTH));
                }
            }

            if (noise.getValue() > 0) {
                blockPos = pos.south();
                if (generator.isAir(blockPos) || world.testBlockState(blockPos, state -> state.isOf(Blocks.VINE))) {
                    generator.replace(blockPos, FloraAndFaunaBlocks.MOSS_CARPET.getDefaultState().with(Properties.FACING, Direction.NORTH));
                }
            }

            if (noise.getValue() > 0) {
                blockPos = pos.up();
                if (generator.isAir(blockPos) || world.testBlockState(blockPos, state -> state.isOf(Blocks.VINE))) {
                    generator.replace(blockPos, FloraAndFaunaBlocks.MOSS_CARPET.getDefaultState().with(Properties.FACING, Direction.DOWN));
                }
            }

            if (noise.getValue() > 0) {
                blockPos = pos.down();
                if (generator.isAir(blockPos) || world.testBlockState(blockPos, state -> state.isOf(Blocks.VINE))) {
                    generator.replace(blockPos, FloraAndFaunaBlocks.MOSS_CARPET.getDefaultState().with(Properties.FACING, Direction.UP));
                }
            }

            if (noiseProbability.getValue() < 0) {
                noise.setValue(Math.max(noise.getValue() - 1, -2));
            } else if (noiseProbability.getValue() > 0) {
                noise.setValue(Math.min(noise.getValue() + 1, 2));
            }
        }
    }
}
