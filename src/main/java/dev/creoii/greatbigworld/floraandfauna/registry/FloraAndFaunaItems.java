package dev.creoii.greatbigworld.floraandfauna.registry;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.util.RegistryHelper;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public final class FloraAndFaunaItems {
    public static Item HUMUS;
    public static Item TROPICAL_FERN;
    public static Item LARGE_TROPICAL_FERN;
    public static Item RED_HIBISCUS;
    public static Item ORANGE_HIBISCUS;
    public static Item YELLOW_HIBISCUS;
    public static Item BLUE_HIBISCUS;
    public static Item PINK_HIBISCUS;
    public static Item PURPLE_HIBISCUS;
    public static Item BLACK_HIBISCUS;
    public static Item WHITE_HIBISCUS;
    public static Item HOLLOW_OAK_LOG;
    public static Item HOLLOW_SPRUCE_LOG;
    public static Item HOLLOW_BIRCH_LOG;
    public static Item HOLLOW_JUNGLE_LOG;
    public static Item HOLLOW_DARK_OAK_LOG;
    public static Item HOLLOW_ACACIA_LOG;
    public static Item HOLLOW_MANGROVE_LOG;
    public static Item HOLLOW_CHERRY_LOG;
    public static Item HOLLOW_PALE_OAK_LOG;
    public static Item HOLLOW_WARPED_STEM;
    public static Item HOLLOW_CRIMSON_STEM;
    public static Item STRIPPED_HOLLOW_OAK_LOG;
    public static Item STRIPPED_HOLLOW_SPRUCE_LOG;
    public static Item STRIPPED_HOLLOW_BIRCH_LOG;
    public static Item STRIPPED_HOLLOW_JUNGLE_LOG;
    public static Item STRIPPED_HOLLOW_DARK_OAK_LOG;
    public static Item STRIPPED_HOLLOW_ACACIA_LOG;
    public static Item STRIPPED_HOLLOW_MANGROVE_LOG;
    public static Item STRIPPED_HOLLOW_CHERRY_LOG;
    public static Item STRIPPED_HOLLOW_PALE_OAK_LOG;
    public static Item STRIPPED_HOLLOW_WARPED_STEM;
    public static Item STRIPPED_HOLLOW_CRIMSON_STEM;
    public static Item ALGAE;

    public static void register() {
        HUMUS = RegistryHelper.registerBlockItem(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "humus"), FloraAndFaunaBlocks.HUMUS);
        TROPICAL_FERN = RegistryHelper.registerBlockItem(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "tropical_fern"), FloraAndFaunaBlocks.TROPICAL_FERN);
        LARGE_TROPICAL_FERN = RegistryHelper.registerBlockItem(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "large_tropical_fern"), FloraAndFaunaBlocks.LARGE_TROPICAL_FERN);
        RED_HIBISCUS = RegistryHelper.registerBlockItem(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "red_hibiscus"), FloraAndFaunaBlocks.RED_HIBISCUS);
        ORANGE_HIBISCUS = RegistryHelper.registerBlockItem(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "orange_hibiscus"), FloraAndFaunaBlocks.ORANGE_HIBISCUS);
        YELLOW_HIBISCUS = RegistryHelper.registerBlockItem(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "yellow_hibiscus"), FloraAndFaunaBlocks.YELLOW_HIBISCUS);
        BLUE_HIBISCUS = RegistryHelper.registerBlockItem(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "blue_hibiscus"), FloraAndFaunaBlocks.BLUE_HIBISCUS);
        PINK_HIBISCUS = RegistryHelper.registerBlockItem(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "pink_hibiscus"), FloraAndFaunaBlocks.PINK_HIBISCUS);
        PURPLE_HIBISCUS = RegistryHelper.registerBlockItem(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "purple_hibiscus"), FloraAndFaunaBlocks.PURPLE_HIBISCUS);
        BLACK_HIBISCUS = RegistryHelper.registerBlockItem(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "black_hibiscus"), FloraAndFaunaBlocks.BLACK_HIBISCUS);
        WHITE_HIBISCUS = RegistryHelper.registerBlockItem(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "white_hibiscus"), FloraAndFaunaBlocks.WHITE_HIBISCUS);
        HOLLOW_OAK_LOG = RegistryHelper.registerBlockItem(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "hollow_oak_log"), FloraAndFaunaBlocks.HOLLOW_OAK_LOG);
        HOLLOW_SPRUCE_LOG = RegistryHelper.registerBlockItem(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "hollow_spruce_log"), FloraAndFaunaBlocks.HOLLOW_SPRUCE_LOG);
        HOLLOW_BIRCH_LOG = RegistryHelper.registerBlockItem(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "hollow_birch_log"), FloraAndFaunaBlocks.HOLLOW_BIRCH_LOG);
        HOLLOW_JUNGLE_LOG = RegistryHelper.registerBlockItem(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "hollow_jungle_log"), FloraAndFaunaBlocks.HOLLOW_JUNGLE_LOG);
        HOLLOW_DARK_OAK_LOG = RegistryHelper.registerBlockItem(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "hollow_dark_oak_log"), FloraAndFaunaBlocks.HOLLOW_DARK_OAK_LOG);
        HOLLOW_ACACIA_LOG = RegistryHelper.registerBlockItem(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "hollow_acacia_log"), FloraAndFaunaBlocks.HOLLOW_ACACIA_LOG);
        HOLLOW_MANGROVE_LOG = RegistryHelper.registerBlockItem(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "hollow_mangrove_log"), FloraAndFaunaBlocks.HOLLOW_MANGROVE_LOG);
        HOLLOW_CHERRY_LOG = RegistryHelper.registerBlockItem(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "hollow_cherry_log"), FloraAndFaunaBlocks.HOLLOW_CHERRY_LOG);
        HOLLOW_PALE_OAK_LOG = RegistryHelper.registerBlockItem(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "hollow_pale_oak_log"), FloraAndFaunaBlocks.HOLLOW_PALE_OAK_LOG);
        HOLLOW_WARPED_STEM = RegistryHelper.registerBlockItem(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "hollow_warped_stem"), FloraAndFaunaBlocks.HOLLOW_WARPED_STEM);
        HOLLOW_CRIMSON_STEM = RegistryHelper.registerBlockItem(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "hollow_crimson_stem"), FloraAndFaunaBlocks.HOLLOW_CRIMSON_STEM);
        STRIPPED_HOLLOW_OAK_LOG = RegistryHelper.registerBlockItem(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "stripped_hollow_oak_log"), FloraAndFaunaBlocks.STRIPPED_HOLLOW_OAK_LOG);
        STRIPPED_HOLLOW_SPRUCE_LOG = RegistryHelper.registerBlockItem(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "stripped_hollow_spruce_log"), FloraAndFaunaBlocks.STRIPPED_HOLLOW_SPRUCE_LOG);
        STRIPPED_HOLLOW_BIRCH_LOG = RegistryHelper.registerBlockItem(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "stripped_hollow_birch_log"), FloraAndFaunaBlocks.STRIPPED_HOLLOW_BIRCH_LOG);
        STRIPPED_HOLLOW_JUNGLE_LOG = RegistryHelper.registerBlockItem(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "stripped_hollow_jungle_log"), FloraAndFaunaBlocks.STRIPPED_HOLLOW_JUNGLE_LOG);
        STRIPPED_HOLLOW_DARK_OAK_LOG = RegistryHelper.registerBlockItem(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "stripped_hollow_dark_oak_log"), FloraAndFaunaBlocks.STRIPPED_HOLLOW_DARK_OAK_LOG);
        STRIPPED_HOLLOW_ACACIA_LOG = RegistryHelper.registerBlockItem(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "stripped_hollow_acacia_log"), FloraAndFaunaBlocks.STRIPPED_HOLLOW_ACACIA_LOG);
        STRIPPED_HOLLOW_MANGROVE_LOG = RegistryHelper.registerBlockItem(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "stripped_hollow_mangrove_log"), FloraAndFaunaBlocks.STRIPPED_HOLLOW_MANGROVE_LOG);
        STRIPPED_HOLLOW_CHERRY_LOG = RegistryHelper.registerBlockItem(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "stripped_hollow_cherry_log"), FloraAndFaunaBlocks.STRIPPED_HOLLOW_CHERRY_LOG);
        STRIPPED_HOLLOW_PALE_OAK_LOG = RegistryHelper.registerBlockItem(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "stripped_hollow_pale_oak_log"), FloraAndFaunaBlocks.STRIPPED_HOLLOW_PALE_OAK_LOG);
        STRIPPED_HOLLOW_WARPED_STEM = RegistryHelper.registerBlockItem(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "stripped_hollow_warped_stem"), FloraAndFaunaBlocks.STRIPPED_HOLLOW_WARPED_STEM);
        STRIPPED_HOLLOW_CRIMSON_STEM = RegistryHelper.registerBlockItem(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "stripped_hollow_crimson_stem"), FloraAndFaunaBlocks.STRIPPED_HOLLOW_CRIMSON_STEM);
        ALGAE = RegistryHelper.registerBlockItem(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "algae"), FloraAndFaunaBlocks.ALGAE);

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.NATURAL_BLOCKS).register(entries -> {
            entries.addAfter(Items.PODZOL, HUMUS);
            entries.addAfter(Items.FERN, TROPICAL_FERN);
            entries.addAfter(Items.LARGE_FERN, LARGE_TROPICAL_FERN);
            entries.addAfter(Items.LILY_OF_THE_VALLEY, RED_HIBISCUS, ORANGE_HIBISCUS, YELLOW_HIBISCUS, BLUE_HIBISCUS, PINK_HIBISCUS, PURPLE_HIBISCUS, BLACK_HIBISCUS, WHITE_HIBISCUS);
            entries.addAfter(Items.OAK_LOG, HOLLOW_OAK_LOG);
            entries.addAfter(Items.SPRUCE_LOG, HOLLOW_SPRUCE_LOG);
            entries.addAfter(Items.BIRCH_LOG, HOLLOW_BIRCH_LOG);
            entries.addAfter(Items.JUNGLE_LOG, HOLLOW_JUNGLE_LOG);
            entries.addAfter(Items.DARK_OAK_LOG, HOLLOW_DARK_OAK_LOG);
            entries.addAfter(Items.ACACIA_LOG, HOLLOW_ACACIA_LOG);
            entries.addAfter(Items.MANGROVE_LOG, HOLLOW_MANGROVE_LOG);
            entries.addAfter(Items.CHERRY_LOG, HOLLOW_CHERRY_LOG);
            entries.addAfter(Items.PALE_OAK_LOG, HOLLOW_PALE_OAK_LOG);
            entries.addAfter(Items.WARPED_STEM, HOLLOW_WARPED_STEM);
            entries.addAfter(Items.CRIMSON_STEM, HOLLOW_CRIMSON_STEM);
            entries.addAfter(Items.LILY_PAD, ALGAE);
        });

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.BUILDING_BLOCKS).register(entries -> {
            entries.addAfter(Items.OAK_WOOD, HOLLOW_OAK_LOG);
            entries.addAfter(Items.SPRUCE_WOOD, HOLLOW_SPRUCE_LOG);
            entries.addAfter(Items.BIRCH_WOOD, HOLLOW_BIRCH_LOG);
            entries.addAfter(Items.JUNGLE_WOOD, HOLLOW_JUNGLE_LOG);
            entries.addAfter(Items.ACACIA_WOOD, HOLLOW_ACACIA_LOG);
            entries.addAfter(Items.DARK_OAK_WOOD, HOLLOW_DARK_OAK_LOG);
            entries.addAfter(Items.MANGROVE_WOOD, HOLLOW_MANGROVE_LOG);
            entries.addAfter(Items.CHERRY_WOOD, HOLLOW_CHERRY_LOG);
            entries.addAfter(Items.PALE_OAK_WOOD, HOLLOW_PALE_OAK_LOG);
            entries.addAfter(Items.WARPED_HYPHAE, HOLLOW_WARPED_STEM);
            entries.addAfter(Items.CRIMSON_HYPHAE, HOLLOW_CRIMSON_STEM);

            entries.addAfter(Items.STRIPPED_OAK_WOOD, STRIPPED_HOLLOW_OAK_LOG);
            entries.addAfter(Items.STRIPPED_SPRUCE_WOOD, STRIPPED_HOLLOW_SPRUCE_LOG);
            entries.addAfter(Items.STRIPPED_BIRCH_WOOD, STRIPPED_HOLLOW_BIRCH_LOG);
            entries.addAfter(Items.STRIPPED_JUNGLE_WOOD, STRIPPED_HOLLOW_JUNGLE_LOG);
            entries.addAfter(Items.STRIPPED_ACACIA_WOOD, STRIPPED_HOLLOW_ACACIA_LOG);
            entries.addAfter(Items.STRIPPED_DARK_OAK_WOOD, STRIPPED_HOLLOW_DARK_OAK_LOG);
            entries.addAfter(Items.STRIPPED_MANGROVE_WOOD, STRIPPED_HOLLOW_MANGROVE_LOG);
            entries.addAfter(Items.STRIPPED_CHERRY_WOOD, STRIPPED_HOLLOW_CHERRY_LOG);
            entries.addAfter(Items.STRIPPED_PALE_OAK_WOOD, STRIPPED_HOLLOW_PALE_OAK_LOG);
            entries.addAfter(Items.STRIPPED_WARPED_HYPHAE, STRIPPED_HOLLOW_WARPED_STEM);
            entries.addAfter(Items.STRIPPED_CRIMSON_HYPHAE, STRIPPED_HOLLOW_CRIMSON_STEM);
        });
    }
}
