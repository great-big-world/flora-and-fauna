package dev.creoii.greatbigworld.floraandfauna.world.feature;

import com.mojang.serialization.Codec;
import dev.creoii.greatbigworld.floraandfauna.util.SnowyHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowyDirtBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class FreezeTopLayerFeature extends Feature<NoneFeatureConfiguration> {
    public FreezeTopLayerFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel world = context.level();
        BlockPos pos = context.origin();
        BlockPos.MutableBlockPos top = new BlockPos.MutableBlockPos();
        BlockPos.MutableBlockPos bottom = new BlockPos.MutableBlockPos();
        for (int i = 0; i < 16; ++i) {
            int k = pos.getX() + i;
            for (int j = 0; j < 16; ++j) {
                int l = pos.getZ() + j;
                int m = world.getHeight(Heightmap.Types.MOTION_BLOCKING, k, l);
                int m1 = world.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, k, l);

                for (int y = m; y >= m1; --y) {
                    top.set(k, y, l);
                    bottom.set(k, y - 1, l);
                    Biome biome = world.getBiome(top).value();

                    if (biome.shouldFreeze(world, bottom, false)) {
                        world.setBlock(bottom, Blocks.ICE.defaultBlockState(), 2);
                    } else {
                        if (biome.shouldSnow(world, top)) {
                            world.setBlock(top, Blocks.SNOW.defaultBlockState(), 2);

                            BlockState bottomState = world.getBlockState(bottom);
                            if (bottomState.hasProperty(SnowyDirtBlock.SNOWY))
                                world.setBlock(bottom, bottomState.setValue(SnowyDirtBlock.SNOWY, true), 2);
                        } else if (canSetSnow(biome, world, top)) {
                            BlockState topState = world.getBlockState(top);

                            if (topState.hasProperty(SnowyHelper.SNOW_LAYERS)) {
                                if (!(topState.hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF) && topState.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER)) {
                                    world.setBlock(top, topState.setValue(SnowyHelper.SNOW_LAYERS, 1), 2);

                                    BlockState bottomState = world.getBlockState(bottom);
                                    if (bottomState.hasProperty(SnowyDirtBlock.SNOWY))
                                        world.setBlock(bottom, bottomState.setValue(SnowyDirtBlock.SNOWY, true), 2);
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    public boolean canSetSnow(Biome biome, LevelReader world, BlockPos pos) {
        if (biome.warmEnoughToRain(pos, world.getSeaLevel())) {
            return false;
        } else return world.isInsideBuildHeight(pos.getY()) && world.getBrightness(LightLayer.BLOCK, pos) < 10;
    }
}
