package dev.creoii.greatbigworld.floraandfauna.registry;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.floraandfauna.block.AlgaeBlock;
import dev.creoii.greatbigworld.floraandfauna.block.HollowLogBlock;
import dev.creoii.greatbigworld.util.ColorHelper;
import dev.creoii.greatbigworld.util.RegistryHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.SnowyDirtBlock;
import net.minecraft.world.level.block.TallFlowerBlock;
import net.minecraft.world.level.block.TallGrassBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.material.MapColor;

public final class FloraAndFaunaBlocks {
    public static Block HUMUS;
    public static Block TROPICAL_FERN;
    public static Block LARGE_TROPICAL_FERN;
    public static Block POTTED_TROPICAL_FERN;
    public static Block RED_HIBISCUS;
    public static Block ORANGE_HIBISCUS;
    public static Block YELLOW_HIBISCUS;
    public static Block BLUE_HIBISCUS;
    public static Block PINK_HIBISCUS;
    public static Block PURPLE_HIBISCUS;
    public static Block BLACK_HIBISCUS;
    public static Block WHITE_HIBISCUS;
    public static Block POTTED_RED_HIBISCUS;
    public static Block POTTED_ORANGE_HIBISCUS;
    public static Block POTTED_YELLOW_HIBISCUS;
    public static Block POTTED_BLUE_HIBISCUS;
    public static Block POTTED_PINK_HIBISCUS;
    public static Block POTTED_PURPLE_HIBISCUS;
    public static Block POTTED_BLACK_HIBISCUS;
    public static Block POTTED_WHITE_HIBISCUS;
    public static Block HOLLOW_OAK_LOG;
    public static Block HOLLOW_SPRUCE_LOG;
    public static Block HOLLOW_BIRCH_LOG;
    public static Block HOLLOW_JUNGLE_LOG;
    public static Block HOLLOW_DARK_OAK_LOG;
    public static Block HOLLOW_ACACIA_LOG;
    public static Block HOLLOW_MANGROVE_LOG;
    public static Block HOLLOW_CHERRY_LOG;
    public static Block HOLLOW_PALE_OAK_LOG;
    public static Block HOLLOW_WARPED_STEM;
    public static Block HOLLOW_CRIMSON_STEM;
    public static Block STRIPPED_HOLLOW_OAK_LOG;
    public static Block STRIPPED_HOLLOW_SPRUCE_LOG;
    public static Block STRIPPED_HOLLOW_BIRCH_LOG;
    public static Block STRIPPED_HOLLOW_JUNGLE_LOG;
    public static Block STRIPPED_HOLLOW_DARK_OAK_LOG;
    public static Block STRIPPED_HOLLOW_ACACIA_LOG;
    public static Block STRIPPED_HOLLOW_MANGROVE_LOG;
    public static Block STRIPPED_HOLLOW_CHERRY_LOG;
    public static Block STRIPPED_HOLLOW_PALE_OAK_LOG;
    public static Block STRIPPED_HOLLOW_WARPED_STEM;
    public static Block STRIPPED_HOLLOW_CRIMSON_STEM;
    public static Block ALGAE;

    public static void register() {
        HUMUS = RegistryHelper.registerBlock(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "humus"), SnowyDirtBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.GRASS_BLOCK).mapColor(MapColor.TERRACOTTA_BROWN));

        TROPICAL_FERN = RegistryHelper.registerBlock(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "tropical_fern"), TallGrassBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.FERN));
        LARGE_TROPICAL_FERN = RegistryHelper.registerBlock(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "large_tropical_fern"), TallFlowerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.LARGE_FERN));
        POTTED_TROPICAL_FERN = RegistryHelper.registerBlock(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "potted_tropical_fern"), settings -> new FlowerPotBlock(TROPICAL_FERN, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.FLOWER_POT));

        RED_HIBISCUS = RegistryHelper.registerBlock(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "red_hibiscus"), settings -> new FlowerBlock(MobEffects.NAUSEA, 0, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.DANDELION));
        ORANGE_HIBISCUS = RegistryHelper.registerBlock(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "orange_hibiscus"), settings -> new FlowerBlock(MobEffects.NAUSEA, 0, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.DANDELION));
        YELLOW_HIBISCUS = RegistryHelper.registerBlock(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "yellow_hibiscus"), settings -> new FlowerBlock(MobEffects.NAUSEA, 0, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.DANDELION));
        BLUE_HIBISCUS = RegistryHelper.registerBlock(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "blue_hibiscus"), settings -> new FlowerBlock(MobEffects.NAUSEA, 0, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.DANDELION));
        PINK_HIBISCUS = RegistryHelper.registerBlock(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "pink_hibiscus"), settings -> new FlowerBlock(MobEffects.NAUSEA, 0, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.DANDELION));
        PURPLE_HIBISCUS = RegistryHelper.registerBlock(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "purple_hibiscus"), settings -> new FlowerBlock(MobEffects.NAUSEA, 0, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.DANDELION));
        BLACK_HIBISCUS = RegistryHelper.registerBlock(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "black_hibiscus"), settings -> new FlowerBlock(MobEffects.NAUSEA, 0, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.DANDELION));
        WHITE_HIBISCUS = RegistryHelper.registerBlock(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "white_hibiscus"), settings -> new FlowerBlock(MobEffects.NAUSEA, 0, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.DANDELION));
        POTTED_RED_HIBISCUS = RegistryHelper.registerBlock(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "potted_red_hibiscus"), settings -> new FlowerPotBlock(RED_HIBISCUS, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.FLOWER_POT));
        POTTED_ORANGE_HIBISCUS = RegistryHelper.registerBlock(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "potted_orange_hibiscus"), settings -> new FlowerPotBlock(ORANGE_HIBISCUS, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.FLOWER_POT));
        POTTED_YELLOW_HIBISCUS = RegistryHelper.registerBlock(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "potted_yellow_hibiscus"), settings -> new FlowerPotBlock(YELLOW_HIBISCUS, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.FLOWER_POT));
        POTTED_BLUE_HIBISCUS = RegistryHelper.registerBlock(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "potted_blue_hibiscus"), settings -> new FlowerPotBlock(BLUE_HIBISCUS, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.FLOWER_POT));
        POTTED_PINK_HIBISCUS = RegistryHelper.registerBlock(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "potted_pink_hibiscus"), settings -> new FlowerPotBlock(PINK_HIBISCUS, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.FLOWER_POT));
        POTTED_PURPLE_HIBISCUS = RegistryHelper.registerBlock(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "potted_purple_hibiscus"), settings -> new FlowerPotBlock(PURPLE_HIBISCUS, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.FLOWER_POT));
        POTTED_BLACK_HIBISCUS = RegistryHelper.registerBlock(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "potted_black_hibiscus"), settings -> new FlowerPotBlock(BLACK_HIBISCUS, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.FLOWER_POT));
        POTTED_WHITE_HIBISCUS = RegistryHelper.registerBlock(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "potted_white_hibiscus"), settings -> new FlowerPotBlock(WHITE_HIBISCUS, settings), BlockBehaviour.Properties.ofFullCopy(Blocks.FLOWER_POT));

        HOLLOW_OAK_LOG = RegistryHelper.registerBlock(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "hollow_oak_log"), HollowLogBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG));
        HOLLOW_SPRUCE_LOG = RegistryHelper.registerBlock(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "hollow_spruce_log"), HollowLogBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_LOG));
        HOLLOW_BIRCH_LOG = RegistryHelper.registerBlock(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "hollow_birch_log"), HollowLogBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.BIRCH_LOG));
        HOLLOW_JUNGLE_LOG = RegistryHelper.registerBlock(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "hollow_jungle_log"), HollowLogBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.JUNGLE_LOG));
        HOLLOW_DARK_OAK_LOG = RegistryHelper.registerBlock(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "hollow_dark_oak_log"), HollowLogBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.DARK_OAK_LOG));
        HOLLOW_ACACIA_LOG = RegistryHelper.registerBlock(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "hollow_acacia_log"), HollowLogBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_LOG));
        HOLLOW_MANGROVE_LOG = RegistryHelper.registerBlock(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "hollow_mangrove_log"), HollowLogBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.MANGROVE_LOG));
        HOLLOW_CHERRY_LOG = RegistryHelper.registerBlock(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "hollow_cherry_log"), HollowLogBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_LOG));
        HOLLOW_PALE_OAK_LOG = RegistryHelper.registerBlock(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "hollow_pale_oak_log"), HollowLogBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.PALE_OAK_LOG));
        HOLLOW_WARPED_STEM = RegistryHelper.registerBlock(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "hollow_warped_stem"), HollowLogBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_STEM));
        HOLLOW_CRIMSON_STEM = RegistryHelper.registerBlock(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "hollow_crimson_stem"), HollowLogBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.CRIMSON_STEM));
        STRIPPED_HOLLOW_OAK_LOG = RegistryHelper.registerBlock(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "stripped_hollow_oak_log"), HollowLogBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_LOG));
        STRIPPED_HOLLOW_SPRUCE_LOG = RegistryHelper.registerBlock(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "stripped_hollow_spruce_log"), HollowLogBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_SPRUCE_LOG));
        STRIPPED_HOLLOW_BIRCH_LOG = RegistryHelper.registerBlock(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "stripped_hollow_birch_log"), HollowLogBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_BIRCH_LOG));
        STRIPPED_HOLLOW_JUNGLE_LOG = RegistryHelper.registerBlock(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "stripped_hollow_jungle_log"), HollowLogBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_JUNGLE_LOG));
        STRIPPED_HOLLOW_DARK_OAK_LOG = RegistryHelper.registerBlock(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "stripped_hollow_dark_oak_log"), HollowLogBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_DARK_OAK_LOG));
        STRIPPED_HOLLOW_ACACIA_LOG = RegistryHelper.registerBlock(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "stripped_hollow_acacia_log"), HollowLogBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_ACACIA_LOG));
        STRIPPED_HOLLOW_MANGROVE_LOG = RegistryHelper.registerBlock(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "stripped_hollow_mangrove_log"), HollowLogBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_MANGROVE_LOG));
        STRIPPED_HOLLOW_CHERRY_LOG = RegistryHelper.registerBlock(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "stripped_hollow_cherry_log"), HollowLogBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_CHERRY_LOG));
        STRIPPED_HOLLOW_PALE_OAK_LOG = RegistryHelper.registerBlock(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "stripped_hollow_pale_oak_log"), HollowLogBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_PALE_OAK_LOG));
        STRIPPED_HOLLOW_WARPED_STEM = RegistryHelper.registerBlock(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "stripped_hollow_warped_stem"), HollowLogBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_WARPED_STEM));
        STRIPPED_HOLLOW_CRIMSON_STEM = RegistryHelper.registerBlock(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "stripped_hollow_crimson_stem"), HollowLogBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_CRIMSON_STEM));

        ALGAE = RegistryHelper.registerBlock(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "algae"), AlgaeBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.LILY_PAD));

        FlammableBlockRegistry.getDefaultInstance().add(TROPICAL_FERN, 60, 100);
        FlammableBlockRegistry.getDefaultInstance().add(LARGE_TROPICAL_FERN, 60, 100);

        FlammableBlockRegistry.getDefaultInstance().add(RED_HIBISCUS, 60, 100);
        FlammableBlockRegistry.getDefaultInstance().add(ORANGE_HIBISCUS, 60, 100);
        FlammableBlockRegistry.getDefaultInstance().add(YELLOW_HIBISCUS, 60, 100);
        FlammableBlockRegistry.getDefaultInstance().add(BLUE_HIBISCUS, 60, 100);
        FlammableBlockRegistry.getDefaultInstance().add(PINK_HIBISCUS, 60, 100);
        FlammableBlockRegistry.getDefaultInstance().add(PURPLE_HIBISCUS, 60, 100);
        FlammableBlockRegistry.getDefaultInstance().add(BLACK_HIBISCUS, 60, 100);
        FlammableBlockRegistry.getDefaultInstance().add(WHITE_HIBISCUS, 60, 100);

        FlammableBlockRegistry.getDefaultInstance().add(HOLLOW_OAK_LOG, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(HOLLOW_SPRUCE_LOG, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(HOLLOW_BIRCH_LOG, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(HOLLOW_JUNGLE_LOG, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(HOLLOW_DARK_OAK_LOG, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(HOLLOW_ACACIA_LOG, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(HOLLOW_MANGROVE_LOG, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(HOLLOW_CHERRY_LOG, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(HOLLOW_PALE_OAK_LOG, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(STRIPPED_HOLLOW_OAK_LOG, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(STRIPPED_HOLLOW_SPRUCE_LOG, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(STRIPPED_HOLLOW_BIRCH_LOG, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(STRIPPED_HOLLOW_JUNGLE_LOG, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(STRIPPED_HOLLOW_DARK_OAK_LOG, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(STRIPPED_HOLLOW_ACACIA_LOG, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(STRIPPED_HOLLOW_MANGROVE_LOG, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(STRIPPED_HOLLOW_CHERRY_LOG, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(STRIPPED_HOLLOW_PALE_OAK_LOG, 5, 5);

        StrippableBlockRegistry.register(HOLLOW_OAK_LOG, STRIPPED_HOLLOW_OAK_LOG);
        StrippableBlockRegistry.register(HOLLOW_SPRUCE_LOG, STRIPPED_HOLLOW_SPRUCE_LOG);
        StrippableBlockRegistry.register(HOLLOW_BIRCH_LOG, STRIPPED_HOLLOW_BIRCH_LOG);
        StrippableBlockRegistry.register(HOLLOW_JUNGLE_LOG, STRIPPED_HOLLOW_JUNGLE_LOG);
        StrippableBlockRegistry.register(HOLLOW_ACACIA_LOG, STRIPPED_HOLLOW_ACACIA_LOG);
        StrippableBlockRegistry.register(HOLLOW_DARK_OAK_LOG, STRIPPED_HOLLOW_DARK_OAK_LOG);
        StrippableBlockRegistry.register(HOLLOW_MANGROVE_LOG, STRIPPED_HOLLOW_MANGROVE_LOG);
        StrippableBlockRegistry.register(HOLLOW_CHERRY_LOG, STRIPPED_HOLLOW_CHERRY_LOG);
        StrippableBlockRegistry.register(HOLLOW_PALE_OAK_LOG, STRIPPED_HOLLOW_PALE_OAK_LOG);
        StrippableBlockRegistry.register(HOLLOW_WARPED_STEM, STRIPPED_HOLLOW_WARPED_STEM);
        StrippableBlockRegistry.register(HOLLOW_CRIMSON_STEM, STRIPPED_HOLLOW_CRIMSON_STEM);
    }

    @Environment(EnvType.CLIENT)
    public static void registerClient() {
        BlockRenderLayerMap.putBlocks(ChunkSectionLayer.CUTOUT,
                TROPICAL_FERN, LARGE_TROPICAL_FERN, POTTED_TROPICAL_FERN,
                RED_HIBISCUS, ORANGE_HIBISCUS, YELLOW_HIBISCUS, BLUE_HIBISCUS, PINK_HIBISCUS, PURPLE_HIBISCUS, BLACK_HIBISCUS, WHITE_HIBISCUS,
                POTTED_RED_HIBISCUS, POTTED_ORANGE_HIBISCUS, POTTED_YELLOW_HIBISCUS, POTTED_BLUE_HIBISCUS, POTTED_PINK_HIBISCUS, POTTED_PURPLE_HIBISCUS, POTTED_BLACK_HIBISCUS, POTTED_WHITE_HIBISCUS,
                ALGAE
        );

        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
            return ColorHelper.add(world != null && pos != null ? BiomeColors.getAverageGrassColor(world, state.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.UPPER ? pos.below() : pos) : GrassColor.getDefaultColor(), 75, 75, 0);
        }, LARGE_TROPICAL_FERN);
        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
            return ColorHelper.add(world != null && pos != null ? BiomeColors.getAverageGrassColor(world, pos) : GrassColor.getDefaultColor(), 75, 75, 0);
        }, TROPICAL_FERN, POTTED_TROPICAL_FERN);
    }
}
