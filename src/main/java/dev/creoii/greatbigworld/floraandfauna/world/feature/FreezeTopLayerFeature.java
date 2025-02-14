package dev.creoii.greatbigworld.floraandfauna.world.feature;

import com.mojang.serialization.Codec;
import dev.creoii.greatbigworld.floraandfauna.util.SnowyHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SnowyBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
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
        BlockPos.Mutable topDown = new BlockPos.Mutable();
        BlockPos.Mutable bottom = new BlockPos.Mutable();
        BlockPos.Mutable bottomDown = new BlockPos.Mutable();
        for (int i = 0; i < 16; ++i) {
            int k = pos.getX() + i;
            for (int j = 0; j < 16; ++j) {
                int l = pos.getZ() + j;
                int m = world.getTopY(Heightmap.Type.MOTION_BLOCKING, k, l);
                int m1 = world.getTopY(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, k, l);
                top.set(k, m, l);
                topDown.set(top).move(Direction.DOWN);
                bottom.set(k, m1, l);
                bottomDown.set(bottom).move(Direction.DOWN);
                Biome topBiome = world.getBiome(top).value();
                Biome bottomBiome = world.getBiome(bottom).value();

                if (topBiome.canSetIce(world, topDown, false)) {
                    world.setBlockState(topDown, Blocks.ICE.getDefaultState(), Block.NOTIFY_LISTENERS);
                }

                if (topBiome.canSetSnow(world, top)) {
                    BlockState blockState = world.getBlockState(topDown);
                    if (world.setBlockState(top, Blocks.SNOW.getDefaultState(), Block.NOTIFY_LISTENERS) && blockState.contains(SnowyBlock.SNOWY))
                        world.setBlockState(topDown, blockState.with(SnowyBlock.SNOWY, true), Block.NOTIFY_LISTENERS);
                }

                if (canSetSnow(topBiome, world, top)) {
                    BlockState blockState = world.getBlockState(top);
                    if (blockState.contains(SnowyHelper.SNOW_LAYERS)) {
                        BlockState blockState1 = world.getBlockState(topDown);
                        if (world.setBlockState(top, blockState.with(SnowyHelper.SNOW_LAYERS, 1), Block.NOTIFY_LISTENERS) && blockState1.contains(SnowyBlock.SNOWY))
                            world.setBlockState(topDown, blockState1.with(SnowyBlock.SNOWY, true), Block.NOTIFY_LISTENERS);

                        if (world.getBlockState(topDown).isOf(Blocks.GRASS_BLOCK) && world.getBlockState(topDown).get(SnowyBlock.SNOWY) && (world.getBlockState(top).isOf(Blocks.AIR) || world.getBlockState(top).isOf(Blocks.TALL_GRASS) || world.getBlockState(top).isOf(Blocks.SHORT_GRASS) && (!SnowyHelper.isSnowy(world.getBlockState(top)) || world.getBlockState(top).isOf(Blocks.SNOW)))) {
                            //System.out.println("bad snowy at: " + topDown.toShortString());
                        }
                    }
                }

                if (m == m1)
                    continue;

                if (bottomBiome.canSetIce(world, bottomDown, false)) {
                    world.setBlockState(bottomDown, Blocks.ICE.getDefaultState(), Block.NOTIFY_LISTENERS);
                }

                if (bottomBiome.canSetSnow(world, bottom)) {
                    BlockState blockState = world.getBlockState(bottomDown);
                    if (world.setBlockState(bottom, Blocks.SNOW.getDefaultState(), Block.NOTIFY_LISTENERS) && blockState.contains(SnowyBlock.SNOWY))
                        world.setBlockState(bottomDown, blockState.with(SnowyBlock.SNOWY, true), Block.NOTIFY_LISTENERS);
                }

                if (canSetSnow(bottomBiome, world, bottom)) {
                    BlockState blockState = world.getBlockState(bottom);
                    if (blockState.contains(SnowyHelper.SNOW_LAYERS)) {
                        BlockState blockState1 = world.getBlockState(bottomDown);
                        if (world.setBlockState(bottom, blockState.with(SnowyHelper.SNOW_LAYERS, 1), Block.NOTIFY_ALL) && blockState1.contains(SnowyBlock.SNOWY))
                            world.setBlockState(bottomDown, blockState1.with(SnowyBlock.SNOWY, true), Block.NOTIFY_LISTENERS);

                        if (world.getBlockState(bottomDown).isOf(Blocks.GRASS_BLOCK) && world.getBlockState(bottomDown).get(SnowyBlock.SNOWY) && (world.getBlockState(bottom).isOf(Blocks.AIR) || world.getBlockState(bottom).isOf(Blocks.TALL_GRASS) || world.getBlockState(bottom).isOf(Blocks.SHORT_GRASS) && SnowyHelper.isSnowy(world.getBlockState(bottom)))) {
                            //System.out.println("bad snowy at: " + bottomDown.toShortString());
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
