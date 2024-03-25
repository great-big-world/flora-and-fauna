package dev.creoii.greatbigworld.floraandfauna.world.decorator;

import com.mojang.serialization.Codec;
import dev.creoii.greatbigworld.floraandfauna.registry.FloraAndFaunaBlocks;
import dev.creoii.greatbigworld.floraandfauna.registry.FloraAndFaunaTreeDecoratorTypes;
import dev.creoii.greatbigworld.floraandfauna.util.SnowyHelper;
import net.minecraft.block.*;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;
import org.apache.commons.lang3.mutable.MutableInt;

public class MossTreeDecorator extends TreeDecorator {
    public static final Codec<MossTreeDecorator> CODEC = Codec.floatRange(0f, 1f).fieldOf("probability").xmap(MossTreeDecorator::new, decorator -> decorator.probability).codec();
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
                    replaceWithMoss(generator, blockPos, ConnectingBlock.EAST);
                }
            }

            if (noise.getValue() > 0) {
                blockPos = pos.east();
                if (generator.isAir(blockPos) || world.testBlockState(blockPos, state -> state.isOf(Blocks.VINE))) {
                    replaceWithMoss(generator, blockPos, ConnectingBlock.WEST);
                }
            }

            if (noise.getValue() > 0) {
                blockPos = pos.north();
                if (generator.isAir(blockPos) || world.testBlockState(blockPos, state -> state.isOf(Blocks.VINE))) {
                    replaceWithMoss(generator, blockPos, ConnectingBlock.SOUTH);
                }
            }

            if (noise.getValue() > 0) {
                blockPos = pos.south();
                if (generator.isAir(blockPos) || world.testBlockState(blockPos, state -> state.isOf(Blocks.VINE))) {
                    replaceWithMoss(generator, blockPos, ConnectingBlock.NORTH);
                }
            }

            if (noise.getValue() > 0) {
                blockPos = pos.up();
                if (generator.isAir(blockPos) || world.testBlockState(blockPos, state -> state.isOf(Blocks.VINE))) {
                    replaceWithMoss(generator, blockPos, ConnectingBlock.DOWN);
                }
            }

            if (noise.getValue() > 0) {
                blockPos = pos.down();
                if (generator.isAir(blockPos) || world.testBlockState(blockPos, state -> state.isOf(Blocks.VINE))) {
                    replaceWithMoss(generator, blockPos, ConnectingBlock.UP);
                }
            }

            if (noiseProbability.getValue() < 0) {
                noise.setValue(Math.max(noise.getValue() - 1, -2));
            } else if (noiseProbability.getValue() > 0) {
                noise.setValue(Math.min(noise.getValue() + 1, 2));
            }
        }
    }

    public void replaceWithMoss(TreeDecorator.Generator generator, BlockPos pos, BooleanProperty faceProperty) {
        BlockState mossState = FloraAndFaunaBlocks.MOSS_CARPET.getDefaultState();

        TestableWorld world = generator.getWorld();
        if (world.testBlockState(pos, state -> state == FloraAndFaunaBlocks.MOSS_CARPET.getDefaultState().with(ConnectingBlock.WEST, true)))
            mossState = mossState.with(ConnectingBlock.WEST, true);
        if (world.testBlockState(pos, state -> state == FloraAndFaunaBlocks.MOSS_CARPET.getDefaultState().with(ConnectingBlock.EAST, true)))
            mossState = mossState.with(ConnectingBlock.EAST, true);
        if (world.testBlockState(pos, state -> state == FloraAndFaunaBlocks.MOSS_CARPET.getDefaultState().with(ConnectingBlock.SOUTH, true)))
            mossState = mossState.with(ConnectingBlock.SOUTH, true);
        if (world.testBlockState(pos, state -> state == FloraAndFaunaBlocks.MOSS_CARPET.getDefaultState().with(ConnectingBlock.NORTH, true)))
            mossState = mossState.with(ConnectingBlock.NORTH, true);
        if (world.testBlockState(pos, state -> state == FloraAndFaunaBlocks.MOSS_CARPET.getDefaultState().with(ConnectingBlock.UP, true)))
            mossState = mossState.with(ConnectingBlock.UP, true);
        if (world.testBlockState(pos, state -> state == FloraAndFaunaBlocks.MOSS_CARPET.getDefaultState().with(ConnectingBlock.DOWN, true)))
            mossState = mossState.with(ConnectingBlock.DOWN, true);

        if (world.testBlockState(pos, state -> state.isOf(Blocks.SNOW)))
            mossState = mossState.with(SnowyHelper.SNOW_LAYERS, 1);

        generator.replace(pos, mossState.with(faceProperty, true));
    }
}
