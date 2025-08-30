package dev.creoii.greatbigworld.floraandfauna.world.feature;

import com.mojang.serialization.Codec;
import dev.creoii.greatbigworld.floraandfauna.util.SnowyHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SnowyBlock;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.LightType;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class FreezeTopLayerFeature extends Feature<DefaultFeatureConfig> {
    public FreezeTopLayerFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        BlockPos pos = context.getOrigin();
        BlockPos.Mutable top = new BlockPos.Mutable();
        BlockPos.Mutable bottom = new BlockPos.Mutable();
        for (int i = 0; i < 16; ++i) {
            int k = pos.getX() + i;
            for (int j = 0; j < 16; ++j) {
                int l = pos.getZ() + j;
                int m = world.getTopY(Heightmap.Type.MOTION_BLOCKING, k, l);
                int m1 = world.getTopY(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, k, l);

                for (int y = m; y >= m1; --y) {
                    top.set(k, y, l);
                    bottom.set(k, y - 1, l);
                    Biome biome = world.getBiome(top).value();

                    if (biome.canSetIce(world, bottom, false)) {
                        world.setBlockState(bottom, Blocks.ICE.getDefaultState(), 2);
                    } else {
                        if (biome.canSetSnow(world, top)) {
                            world.setBlockState(top, Blocks.SNOW.getDefaultState(), 2);

                            BlockState bottomState = world.getBlockState(bottom);
                            if (bottomState.contains(SnowyBlock.SNOWY))
                                world.setBlockState(bottom, bottomState.with(SnowyBlock.SNOWY, true), 2);
                        } else if (canSetSnow(biome, world, top)) {
                            BlockState topState = world.getBlockState(top);

                            if (topState.contains(SnowyHelper.SNOW_LAYERS)) {
                                if (!(topState.contains(Properties.DOUBLE_BLOCK_HALF) && topState.get(Properties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER)) {
                                    world.setBlockState(top, topState.with(SnowyHelper.SNOW_LAYERS, 1), 2);

                                    BlockState bottomState = world.getBlockState(bottom);
                                    if (bottomState.contains(SnowyBlock.SNOWY))
                                        world.setBlockState(bottom, bottomState.with(SnowyBlock.SNOWY, true), 2);
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    public boolean canSetSnow(Biome biome, WorldView world, BlockPos pos) {
        if (biome.doesNotSnow(pos, world.getSeaLevel())) {
            return false;
        } else return world.isInHeightLimit(pos.getY()) && world.getLightLevel(LightType.BLOCK, pos) < 10;
    }
}
