package dev.creoii.greatbigworld.floraandfauna.registry;

import dev.creoii.greatbigworld.floraandfauna.FloraAndFauna;
import dev.creoii.greatbigworld.floraandfauna.block.AlgaeBlock;
import dev.creoii.greatbigworld.floraandfauna.block.HollowLogBlock;
import dev.creoii.greatbigworld.floraandfauna.block.MossCarpetBlock;
import dev.creoii.greatbigworld.floraandfauna.util.ColorHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.minecraft.block.*;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.color.world.GrassColors;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.*;
import net.minecraft.util.Identifier;

public final class FloraAndFaunaBlocks {
    public static final Block HUMUS = new SnowyBlock(AbstractBlock.Settings.copy(Blocks.GRASS_BLOCK).mapColor(MapColor.TERRACOTTA_BROWN));
    public static final Block MOSS_CARPET = new MossCarpetBlock(AbstractBlock.Settings.copy(Blocks.MOSS_CARPET));
    public static final Block TROPICAL_FERN = new ShortPlantBlock(AbstractBlock.Settings.copy(Blocks.FERN));
    public static final Block LARGE_TROPICAL_FERN = new TallPlantBlock(AbstractBlock.Settings.copy(Blocks.LARGE_FERN));
    public static final Block POTTED_TROPICAL_FERN = new FlowerPotBlock(TROPICAL_FERN, AbstractBlock.Settings.copy(Blocks.FLOWER_POT));
    public static final Block RED_HIBISCUS = new FlowerBlock(StatusEffects.NAUSEA, 0, AbstractBlock.Settings.copy(Blocks.DANDELION));
    public static final Block ORANGE_HIBISCUS = new FlowerBlock(StatusEffects.NAUSEA, 0, AbstractBlock.Settings.copy(Blocks.DANDELION));
    public static final Block YELLOW_HIBISCUS = new FlowerBlock(StatusEffects.NAUSEA, 0, AbstractBlock.Settings.copy(Blocks.DANDELION));
    public static final Block BLUE_HIBISCUS = new FlowerBlock(StatusEffects.NAUSEA, 0, AbstractBlock.Settings.copy(Blocks.DANDELION));
    public static final Block PINK_HIBISCUS = new FlowerBlock(StatusEffects.NAUSEA, 0, AbstractBlock.Settings.copy(Blocks.DANDELION));
    public static final Block PURPLE_HIBISCUS = new FlowerBlock(StatusEffects.NAUSEA, 0, AbstractBlock.Settings.copy(Blocks.DANDELION));
    public static final Block BLACK_HIBISCUS = new FlowerBlock(StatusEffects.NAUSEA, 0, AbstractBlock.Settings.copy(Blocks.DANDELION));
    public static final Block WHITE_HIBISCUS = new FlowerBlock(StatusEffects.NAUSEA, 0, AbstractBlock.Settings.copy(Blocks.DANDELION));
    public static final Block POTTED_RED_HIBISCUS = new FlowerPotBlock(RED_HIBISCUS, AbstractBlock.Settings.copy(Blocks.FLOWER_POT));
    public static final Block POTTED_ORANGE_HIBISCUS = new FlowerPotBlock(ORANGE_HIBISCUS, AbstractBlock.Settings.copy(Blocks.FLOWER_POT));
    public static final Block POTTED_YELLOW_HIBISCUS = new FlowerPotBlock(YELLOW_HIBISCUS, AbstractBlock.Settings.copy(Blocks.FLOWER_POT));
    public static final Block POTTED_BLUE_HIBISCUS = new FlowerPotBlock(BLUE_HIBISCUS, AbstractBlock.Settings.copy(Blocks.FLOWER_POT));
    public static final Block POTTED_PINK_HIBISCUS = new FlowerPotBlock(PINK_HIBISCUS, AbstractBlock.Settings.copy(Blocks.FLOWER_POT));
    public static final Block POTTED_PURPLE_HIBISCUS = new FlowerPotBlock(PURPLE_HIBISCUS, AbstractBlock.Settings.copy(Blocks.FLOWER_POT));
    public static final Block POTTED_BLACK_HIBISCUS = new FlowerPotBlock(BLACK_HIBISCUS, AbstractBlock.Settings.copy(Blocks.FLOWER_POT));
    public static final Block POTTED_WHITE_HIBISCUS = new FlowerPotBlock(WHITE_HIBISCUS, AbstractBlock.Settings.copy(Blocks.FLOWER_POT));
    public static final Block HOLLOW_OAK_LOG = new HollowLogBlock(AbstractBlock.Settings.copy(Blocks.OAK_LOG));
    public static final Block HOLLOW_SPRUCE_LOG = new HollowLogBlock(AbstractBlock.Settings.copy(Blocks.SPRUCE_LOG));
    public static final Block HOLLOW_BIRCH_LOG = new HollowLogBlock(AbstractBlock.Settings.copy(Blocks.BIRCH_LOG));
    public static final Block HOLLOW_JUNGLE_LOG = new HollowLogBlock(AbstractBlock.Settings.copy(Blocks.JUNGLE_LOG));
    public static final Block HOLLOW_DARK_OAK_LOG = new HollowLogBlock(AbstractBlock.Settings.copy(Blocks.DARK_OAK_LOG));
    public static final Block HOLLOW_ACACIA_LOG = new HollowLogBlock(AbstractBlock.Settings.copy(Blocks.ACACIA_LOG));
    public static final Block HOLLOW_MANGROVE_LOG = new HollowLogBlock(AbstractBlock.Settings.copy(Blocks.MANGROVE_LOG));
    public static final Block HOLLOW_CHERRY_LOG = new HollowLogBlock(AbstractBlock.Settings.copy(Blocks.CHERRY_LOG));
    public static final Block HOLLOW_WARPED_STEM = new HollowLogBlock(AbstractBlock.Settings.copy(Blocks.WARPED_STEM));
    public static final Block HOLLOW_CRIMSON_STEM = new HollowLogBlock(AbstractBlock.Settings.copy(Blocks.CRIMSON_STEM));
    public static final Block STRIPPED_HOLLOW_OAK_LOG = new HollowLogBlock(AbstractBlock.Settings.copy(Blocks.STRIPPED_OAK_LOG));
    public static final Block STRIPPED_HOLLOW_SPRUCE_LOG = new HollowLogBlock(AbstractBlock.Settings.copy(Blocks.STRIPPED_SPRUCE_LOG));
    public static final Block STRIPPED_HOLLOW_BIRCH_LOG = new HollowLogBlock(AbstractBlock.Settings.copy(Blocks.STRIPPED_BIRCH_LOG));
    public static final Block STRIPPED_HOLLOW_JUNGLE_LOG = new HollowLogBlock(AbstractBlock.Settings.copy(Blocks.STRIPPED_JUNGLE_LOG));
    public static final Block STRIPPED_HOLLOW_DARK_OAK_LOG = new HollowLogBlock(AbstractBlock.Settings.copy(Blocks.STRIPPED_DARK_OAK_LOG));
    public static final Block STRIPPED_HOLLOW_ACACIA_LOG = new HollowLogBlock(AbstractBlock.Settings.copy(Blocks.STRIPPED_ACACIA_LOG));
    public static final Block STRIPPED_HOLLOW_MANGROVE_LOG = new HollowLogBlock(AbstractBlock.Settings.copy(Blocks.STRIPPED_MANGROVE_LOG));
    public static final Block STRIPPED_HOLLOW_CHERRY_LOG = new HollowLogBlock(AbstractBlock.Settings.copy(Blocks.STRIPPED_CHERRY_LOG));
    public static final Block STRIPPED_HOLLOW_WARPED_STEM = new HollowLogBlock(AbstractBlock.Settings.copy(Blocks.STRIPPED_WARPED_STEM));
    public static final Block STRIPPED_HOLLOW_CRIMSON_STEM = new HollowLogBlock(AbstractBlock.Settings.copy(Blocks.STRIPPED_CRIMSON_STEM));
    public static final Block ALGAE = new AlgaeBlock(AbstractBlock.Settings.copy(Blocks.LILY_PAD));

    public static void register() {
        Registry.register(Registries.BLOCK, new Identifier(FloraAndFauna.NAMESPACE, "humus"), HUMUS);
        Registry.register(Registries.BLOCK, new Identifier(FloraAndFauna.NAMESPACE, "moss_carpet"), MOSS_CARPET);

        Registry.register(Registries.BLOCK, new Identifier(FloraAndFauna.NAMESPACE, "tropical_fern"), TROPICAL_FERN);
        Registry.register(Registries.BLOCK, new Identifier(FloraAndFauna.NAMESPACE, "large_tropical_fern"), LARGE_TROPICAL_FERN);
        Registry.register(Registries.BLOCK, new Identifier(FloraAndFauna.NAMESPACE, "potted_tropical_fern"), POTTED_TROPICAL_FERN);

        Registry.register(Registries.BLOCK, new Identifier(FloraAndFauna.NAMESPACE, "red_hibiscus"), RED_HIBISCUS);
        Registry.register(Registries.BLOCK, new Identifier(FloraAndFauna.NAMESPACE, "orange_hibiscus"), ORANGE_HIBISCUS);
        Registry.register(Registries.BLOCK, new Identifier(FloraAndFauna.NAMESPACE, "yellow_hibiscus"), YELLOW_HIBISCUS);
        Registry.register(Registries.BLOCK, new Identifier(FloraAndFauna.NAMESPACE, "blue_hibiscus"), BLUE_HIBISCUS);
        Registry.register(Registries.BLOCK, new Identifier(FloraAndFauna.NAMESPACE, "pink_hibiscus"), PINK_HIBISCUS);
        Registry.register(Registries.BLOCK, new Identifier(FloraAndFauna.NAMESPACE, "purple_hibiscus"), PURPLE_HIBISCUS);
        Registry.register(Registries.BLOCK, new Identifier(FloraAndFauna.NAMESPACE, "black_hibiscus"), BLACK_HIBISCUS);
        Registry.register(Registries.BLOCK, new Identifier(FloraAndFauna.NAMESPACE, "white_hibiscus"), WHITE_HIBISCUS);
        Registry.register(Registries.BLOCK, new Identifier(FloraAndFauna.NAMESPACE, "potted_red_hibiscus"), POTTED_RED_HIBISCUS);
        Registry.register(Registries.BLOCK, new Identifier(FloraAndFauna.NAMESPACE, "potted_orange_hibiscus"), POTTED_ORANGE_HIBISCUS);
        Registry.register(Registries.BLOCK, new Identifier(FloraAndFauna.NAMESPACE, "potted_yellow_hibiscus"), POTTED_YELLOW_HIBISCUS);
        Registry.register(Registries.BLOCK, new Identifier(FloraAndFauna.NAMESPACE, "potted_blue_hibiscus"), POTTED_BLUE_HIBISCUS);
        Registry.register(Registries.BLOCK, new Identifier(FloraAndFauna.NAMESPACE, "potted_pink_hibiscus"), POTTED_PINK_HIBISCUS);
        Registry.register(Registries.BLOCK, new Identifier(FloraAndFauna.NAMESPACE, "potted_purple_hibiscus"), POTTED_PURPLE_HIBISCUS);
        Registry.register(Registries.BLOCK, new Identifier(FloraAndFauna.NAMESPACE, "potted_black_hibiscus"), POTTED_BLACK_HIBISCUS);
        Registry.register(Registries.BLOCK, new Identifier(FloraAndFauna.NAMESPACE, "potted_white_hibiscus"), POTTED_WHITE_HIBISCUS);

        Registry.register(Registries.BLOCK, new Identifier(FloraAndFauna.NAMESPACE, "hollow_oak_log"), HOLLOW_OAK_LOG);
        Registry.register(Registries.BLOCK, new Identifier(FloraAndFauna.NAMESPACE, "hollow_spruce_log"), HOLLOW_SPRUCE_LOG);
        Registry.register(Registries.BLOCK, new Identifier(FloraAndFauna.NAMESPACE, "hollow_birch_log"), HOLLOW_BIRCH_LOG);
        Registry.register(Registries.BLOCK, new Identifier(FloraAndFauna.NAMESPACE, "hollow_jungle_log"), HOLLOW_JUNGLE_LOG);
        Registry.register(Registries.BLOCK, new Identifier(FloraAndFauna.NAMESPACE, "hollow_dark_oak_log"), HOLLOW_DARK_OAK_LOG);
        Registry.register(Registries.BLOCK, new Identifier(FloraAndFauna.NAMESPACE, "hollow_acacia_log"), HOLLOW_ACACIA_LOG);
        Registry.register(Registries.BLOCK, new Identifier(FloraAndFauna.NAMESPACE, "hollow_mangrove_log"), HOLLOW_MANGROVE_LOG);
        Registry.register(Registries.BLOCK, new Identifier(FloraAndFauna.NAMESPACE, "hollow_cherry_log"), HOLLOW_CHERRY_LOG);
        Registry.register(Registries.BLOCK, new Identifier(FloraAndFauna.NAMESPACE, "hollow_warped_stem"), HOLLOW_WARPED_STEM);
        Registry.register(Registries.BLOCK, new Identifier(FloraAndFauna.NAMESPACE, "hollow_crimson_stem"), HOLLOW_CRIMSON_STEM);
        Registry.register(Registries.BLOCK, new Identifier(FloraAndFauna.NAMESPACE, "stripped_hollow_oak_log"), STRIPPED_HOLLOW_OAK_LOG);
        Registry.register(Registries.BLOCK, new Identifier(FloraAndFauna.NAMESPACE, "stripped_hollow_spruce_log"), STRIPPED_HOLLOW_SPRUCE_LOG);
        Registry.register(Registries.BLOCK, new Identifier(FloraAndFauna.NAMESPACE, "stripped_hollow_birch_log"), STRIPPED_HOLLOW_BIRCH_LOG);
        Registry.register(Registries.BLOCK, new Identifier(FloraAndFauna.NAMESPACE, "stripped_hollow_jungle_log"), STRIPPED_HOLLOW_JUNGLE_LOG);
        Registry.register(Registries.BLOCK, new Identifier(FloraAndFauna.NAMESPACE, "stripped_hollow_dark_oak_log"), STRIPPED_HOLLOW_DARK_OAK_LOG);
        Registry.register(Registries.BLOCK, new Identifier(FloraAndFauna.NAMESPACE, "stripped_hollow_acacia_log"), STRIPPED_HOLLOW_ACACIA_LOG);
        Registry.register(Registries.BLOCK, new Identifier(FloraAndFauna.NAMESPACE, "stripped_hollow_mangrove_log"), STRIPPED_HOLLOW_MANGROVE_LOG);
        Registry.register(Registries.BLOCK, new Identifier(FloraAndFauna.NAMESPACE, "stripped_hollow_cherry_log"), STRIPPED_HOLLOW_CHERRY_LOG);
        Registry.register(Registries.BLOCK, new Identifier(FloraAndFauna.NAMESPACE, "stripped_hollow_warped_stem"), STRIPPED_HOLLOW_WARPED_STEM);
        Registry.register(Registries.BLOCK, new Identifier(FloraAndFauna.NAMESPACE, "stripped_hollow_crimson_stem"), STRIPPED_HOLLOW_CRIMSON_STEM);

        Registry.register(Registries.BLOCK, new Identifier(FloraAndFauna.NAMESPACE, "algae"), ALGAE);

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
        FlammableBlockRegistry.getDefaultInstance().add(STRIPPED_HOLLOW_OAK_LOG, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(STRIPPED_HOLLOW_SPRUCE_LOG, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(STRIPPED_HOLLOW_BIRCH_LOG, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(STRIPPED_HOLLOW_JUNGLE_LOG, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(STRIPPED_HOLLOW_DARK_OAK_LOG, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(STRIPPED_HOLLOW_ACACIA_LOG, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(STRIPPED_HOLLOW_MANGROVE_LOG, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(STRIPPED_HOLLOW_CHERRY_LOG, 5, 5);

        StrippableBlockRegistry.register(HOLLOW_OAK_LOG, STRIPPED_HOLLOW_OAK_LOG);
        StrippableBlockRegistry.register(HOLLOW_SPRUCE_LOG, STRIPPED_HOLLOW_SPRUCE_LOG);
        StrippableBlockRegistry.register(HOLLOW_BIRCH_LOG, STRIPPED_HOLLOW_BIRCH_LOG);
        StrippableBlockRegistry.register(HOLLOW_JUNGLE_LOG, STRIPPED_HOLLOW_JUNGLE_LOG);
        StrippableBlockRegistry.register(HOLLOW_ACACIA_LOG, STRIPPED_HOLLOW_ACACIA_LOG);
        StrippableBlockRegistry.register(HOLLOW_DARK_OAK_LOG, STRIPPED_HOLLOW_DARK_OAK_LOG);
        StrippableBlockRegistry.register(HOLLOW_MANGROVE_LOG, STRIPPED_HOLLOW_MANGROVE_LOG);
        StrippableBlockRegistry.register(HOLLOW_CHERRY_LOG, STRIPPED_HOLLOW_CHERRY_LOG);
        StrippableBlockRegistry.register(HOLLOW_WARPED_STEM, STRIPPED_HOLLOW_WARPED_STEM);
        StrippableBlockRegistry.register(HOLLOW_CRIMSON_STEM, STRIPPED_HOLLOW_CRIMSON_STEM);
    }

    @Environment(EnvType.CLIENT)
    public static void registerClient() {
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(),
                TROPICAL_FERN, LARGE_TROPICAL_FERN, POTTED_TROPICAL_FERN,
                RED_HIBISCUS, ORANGE_HIBISCUS, YELLOW_HIBISCUS, BLUE_HIBISCUS, PINK_HIBISCUS, PURPLE_HIBISCUS, BLACK_HIBISCUS, WHITE_HIBISCUS,
                POTTED_RED_HIBISCUS, POTTED_ORANGE_HIBISCUS, POTTED_YELLOW_HIBISCUS, POTTED_BLUE_HIBISCUS, POTTED_PINK_HIBISCUS, POTTED_PURPLE_HIBISCUS, POTTED_BLACK_HIBISCUS, POTTED_WHITE_HIBISCUS,
                ALGAE
        );

        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
            return ColorHelper.add(world != null && pos != null ? BiomeColors.getGrassColor(world, state.get(TallPlantBlock.HALF) == DoubleBlockHalf.UPPER ? pos.down() : pos) : GrassColors.getDefaultColor(), 75, 75, 0);
        }, LARGE_TROPICAL_FERN);
        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
            return ColorHelper.add(world != null && pos != null ? BiomeColors.getGrassColor(world, pos) : GrassColors.getDefaultColor(), 75, 75, 0);
        }, TROPICAL_FERN, POTTED_TROPICAL_FERN);
    }
}
