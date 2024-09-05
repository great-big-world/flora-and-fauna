package dev.creoii.greatbigworld.floraandfauna.registry;

import dev.creoii.creoapi.api.item.CreoItemSettings;
import dev.creoii.greatbigworld.floraandfauna.FloraAndFauna;
import dev.creoii.greatbigworld.floraandfauna.util.ColorHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.client.color.world.GrassColors;
import net.minecraft.item.*;
import net.minecraft.registry.*;
import net.minecraft.util.Identifier;

import java.util.List;

public final class FloraAndFaunaItems {
    public static final Item HUMUS = new BlockItem(FloraAndFaunaBlocks.HUMUS, new CreoItemSettings());
    public static final Item MOSS_CARPET = new BlockItem(FloraAndFaunaBlocks.MOSS_CARPET, new CreoItemSettings());
    public static final Item TROPICAL_FERN = new BlockItem(FloraAndFaunaBlocks.TROPICAL_FERN, new CreoItemSettings());
    public static final Item LARGE_TROPICAL_FERN = new BlockItem(FloraAndFaunaBlocks.LARGE_TROPICAL_FERN, new CreoItemSettings());
    public static final Item RED_HIBISCUS = new BlockItem(FloraAndFaunaBlocks.RED_HIBISCUS, new CreoItemSettings());
    public static final Item ORANGE_HIBISCUS = new BlockItem(FloraAndFaunaBlocks.ORANGE_HIBISCUS, new CreoItemSettings());
    public static final Item YELLOW_HIBISCUS = new BlockItem(FloraAndFaunaBlocks.YELLOW_HIBISCUS, new CreoItemSettings());
    public static final Item BLUE_HIBISCUS = new BlockItem(FloraAndFaunaBlocks.BLUE_HIBISCUS, new CreoItemSettings());
    public static final Item PINK_HIBISCUS = new BlockItem(FloraAndFaunaBlocks.PINK_HIBISCUS, new CreoItemSettings());
    public static final Item PURPLE_HIBISCUS = new BlockItem(FloraAndFaunaBlocks.PURPLE_HIBISCUS, new CreoItemSettings());
    public static final Item BLACK_HIBISCUS = new BlockItem(FloraAndFaunaBlocks.BLACK_HIBISCUS, new CreoItemSettings());
    public static final Item WHITE_HIBISCUS = new BlockItem(FloraAndFaunaBlocks.WHITE_HIBISCUS, new CreoItemSettings());
    public static final Item HOLLOW_OAK_LOG = new BlockItem(FloraAndFaunaBlocks.HOLLOW_OAK_LOG, new CreoItemSettings());
    public static final Item HOLLOW_SPRUCE_LOG = new BlockItem(FloraAndFaunaBlocks.HOLLOW_SPRUCE_LOG, new CreoItemSettings());
    public static final Item HOLLOW_BIRCH_LOG = new BlockItem(FloraAndFaunaBlocks.HOLLOW_BIRCH_LOG, new CreoItemSettings());
    public static final Item HOLLOW_JUNGLE_LOG = new BlockItem(FloraAndFaunaBlocks.HOLLOW_JUNGLE_LOG, new CreoItemSettings());
    public static final Item HOLLOW_DARK_OAK_LOG = new BlockItem(FloraAndFaunaBlocks.HOLLOW_DARK_OAK_LOG, new CreoItemSettings());
    public static final Item HOLLOW_ACACIA_LOG = new BlockItem(FloraAndFaunaBlocks.HOLLOW_ACACIA_LOG, new CreoItemSettings());
    public static final Item HOLLOW_MANGROVE_LOG = new BlockItem(FloraAndFaunaBlocks.HOLLOW_MANGROVE_LOG, new CreoItemSettings());
    public static final Item HOLLOW_CHERRY_LOG = new BlockItem(FloraAndFaunaBlocks.HOLLOW_CHERRY_LOG, new CreoItemSettings());
    public static final Item HOLLOW_WARPED_STEM = new BlockItem(FloraAndFaunaBlocks.HOLLOW_WARPED_STEM, new CreoItemSettings());
    public static final Item HOLLOW_CRIMSON_STEM = new BlockItem(FloraAndFaunaBlocks.HOLLOW_CRIMSON_STEM, new CreoItemSettings());
    public static final Item STRIPPED_HOLLOW_OAK_LOG = new BlockItem(FloraAndFaunaBlocks.STRIPPED_HOLLOW_OAK_LOG, new CreoItemSettings());
    public static final Item STRIPPED_HOLLOW_SPRUCE_LOG = new BlockItem(FloraAndFaunaBlocks.STRIPPED_HOLLOW_SPRUCE_LOG, new CreoItemSettings());
    public static final Item STRIPPED_HOLLOW_BIRCH_LOG = new BlockItem(FloraAndFaunaBlocks.STRIPPED_HOLLOW_BIRCH_LOG, new CreoItemSettings());
    public static final Item STRIPPED_HOLLOW_JUNGLE_LOG = new BlockItem(FloraAndFaunaBlocks.STRIPPED_HOLLOW_JUNGLE_LOG, new CreoItemSettings());
    public static final Item STRIPPED_HOLLOW_DARK_OAK_LOG = new BlockItem(FloraAndFaunaBlocks.STRIPPED_HOLLOW_DARK_OAK_LOG, new CreoItemSettings());
    public static final Item STRIPPED_HOLLOW_ACACIA_LOG = new BlockItem(FloraAndFaunaBlocks.STRIPPED_HOLLOW_ACACIA_LOG, new CreoItemSettings());
    public static final Item STRIPPED_HOLLOW_MANGROVE_LOG = new BlockItem(FloraAndFaunaBlocks.STRIPPED_HOLLOW_MANGROVE_LOG, new CreoItemSettings());
    public static final Item STRIPPED_HOLLOW_CHERRY_LOG = new BlockItem(FloraAndFaunaBlocks.STRIPPED_HOLLOW_CHERRY_LOG, new CreoItemSettings());
    public static final Item STRIPPED_HOLLOW_WARPED_STEM = new BlockItem(FloraAndFaunaBlocks.STRIPPED_HOLLOW_WARPED_STEM, new CreoItemSettings());
    public static final Item STRIPPED_HOLLOW_CRIMSON_STEM = new BlockItem(FloraAndFaunaBlocks.STRIPPED_HOLLOW_CRIMSON_STEM, new CreoItemSettings());
    public static final Item ALGAE = new PlaceableOnWaterItem(FloraAndFaunaBlocks.ALGAE, new CreoItemSettings());

    public static void register() {
        Registry.register(Registries.ITEM, new Identifier(FloraAndFauna.NAMESPACE, "humus"), HUMUS);
        Registry.register(Registries.ITEM, new Identifier(FloraAndFauna.NAMESPACE, "moss_carpet"), MOSS_CARPET);

        Registry.register(Registries.ITEM, new Identifier(FloraAndFauna.NAMESPACE, "tropical_fern"), TROPICAL_FERN);
        Registry.register(Registries.ITEM, new Identifier(FloraAndFauna.NAMESPACE, "large_tropical_fern"), LARGE_TROPICAL_FERN);
        Registry.register(Registries.ITEM, new Identifier(FloraAndFauna.NAMESPACE, "red_hibiscus"), RED_HIBISCUS);
        Registry.register(Registries.ITEM, new Identifier(FloraAndFauna.NAMESPACE, "orange_hibiscus"), ORANGE_HIBISCUS);
        Registry.register(Registries.ITEM, new Identifier(FloraAndFauna.NAMESPACE, "yellow_hibiscus"), YELLOW_HIBISCUS);
        Registry.register(Registries.ITEM, new Identifier(FloraAndFauna.NAMESPACE, "blue_hibiscus"), BLUE_HIBISCUS);
        Registry.register(Registries.ITEM, new Identifier(FloraAndFauna.NAMESPACE, "pink_hibiscus"), PINK_HIBISCUS);
        Registry.register(Registries.ITEM, new Identifier(FloraAndFauna.NAMESPACE, "purple_hibiscus"), PURPLE_HIBISCUS);
        Registry.register(Registries.ITEM, new Identifier(FloraAndFauna.NAMESPACE, "black_hibiscus"), BLACK_HIBISCUS);
        Registry.register(Registries.ITEM, new Identifier(FloraAndFauna.NAMESPACE, "white_hibiscus"), WHITE_HIBISCUS);
        Registry.register(Registries.ITEM, new Identifier(FloraAndFauna.NAMESPACE, "hollow_oak_log"), HOLLOW_OAK_LOG);
        Registry.register(Registries.ITEM, new Identifier(FloraAndFauna.NAMESPACE, "hollow_spruce_log"), HOLLOW_SPRUCE_LOG);
        Registry.register(Registries.ITEM, new Identifier(FloraAndFauna.NAMESPACE, "hollow_birch_log"), HOLLOW_BIRCH_LOG);
        Registry.register(Registries.ITEM, new Identifier(FloraAndFauna.NAMESPACE, "hollow_jungle_log"), HOLLOW_JUNGLE_LOG);
        Registry.register(Registries.ITEM, new Identifier(FloraAndFauna.NAMESPACE, "hollow_dark_oak_log"), HOLLOW_DARK_OAK_LOG);
        Registry.register(Registries.ITEM, new Identifier(FloraAndFauna.NAMESPACE, "hollow_acacia_log"), HOLLOW_ACACIA_LOG);
        Registry.register(Registries.ITEM, new Identifier(FloraAndFauna.NAMESPACE, "hollow_mangrove_log"), HOLLOW_MANGROVE_LOG);
        Registry.register(Registries.ITEM, new Identifier(FloraAndFauna.NAMESPACE, "hollow_cherry_log"), HOLLOW_CHERRY_LOG);
        Registry.register(Registries.ITEM, new Identifier(FloraAndFauna.NAMESPACE, "hollow_warped_stem"), HOLLOW_WARPED_STEM);
        Registry.register(Registries.ITEM, new Identifier(FloraAndFauna.NAMESPACE, "hollow_crimson_stem"), HOLLOW_CRIMSON_STEM);
        Registry.register(Registries.ITEM, new Identifier(FloraAndFauna.NAMESPACE, "stripped_hollow_oak_log"), STRIPPED_HOLLOW_OAK_LOG);
        Registry.register(Registries.ITEM, new Identifier(FloraAndFauna.NAMESPACE, "stripped_hollow_spruce_log"), STRIPPED_HOLLOW_SPRUCE_LOG);
        Registry.register(Registries.ITEM, new Identifier(FloraAndFauna.NAMESPACE, "stripped_hollow_birch_log"), STRIPPED_HOLLOW_BIRCH_LOG);
        Registry.register(Registries.ITEM, new Identifier(FloraAndFauna.NAMESPACE, "stripped_hollow_jungle_log"), STRIPPED_HOLLOW_JUNGLE_LOG);
        Registry.register(Registries.ITEM, new Identifier(FloraAndFauna.NAMESPACE, "stripped_hollow_dark_oak_log"), STRIPPED_HOLLOW_DARK_OAK_LOG);
        Registry.register(Registries.ITEM, new Identifier(FloraAndFauna.NAMESPACE, "stripped_hollow_acacia_log"), STRIPPED_HOLLOW_ACACIA_LOG);
        Registry.register(Registries.ITEM, new Identifier(FloraAndFauna.NAMESPACE, "stripped_hollow_mangrove_log"), STRIPPED_HOLLOW_MANGROVE_LOG);
        Registry.register(Registries.ITEM, new Identifier(FloraAndFauna.NAMESPACE, "stripped_hollow_cherry_log"), STRIPPED_HOLLOW_CHERRY_LOG);
        Registry.register(Registries.ITEM, new Identifier(FloraAndFauna.NAMESPACE, "stripped_hollow_warped_stem"), STRIPPED_HOLLOW_WARPED_STEM);
        Registry.register(Registries.ITEM, new Identifier(FloraAndFauna.NAMESPACE, "stripped_hollow_crimson_stem"), STRIPPED_HOLLOW_CRIMSON_STEM);
        Registry.register(Registries.ITEM, new Identifier(FloraAndFauna.NAMESPACE, "algae"), ALGAE);


        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(entries -> {
            entries.addAfter(Items.PODZOL, HUMUS);
            entries.addAfter(Items.FERN, TROPICAL_FERN);
            entries.addAfter(Items.LARGE_FERN, LARGE_TROPICAL_FERN);
            entries.addAfter(Items.LILY_OF_THE_VALLEY, RED_HIBISCUS, ORANGE_HIBISCUS, YELLOW_HIBISCUS, BLUE_HIBISCUS, PINK_HIBISCUS, PURPLE_HIBISCUS, BLACK_HIBISCUS, WHITE_HIBISCUS);
            entries.addAfter(Items.OAK_LOG, HOLLOW_OAK_LOG, STRIPPED_HOLLOW_OAK_LOG);
            entries.addAfter(Items.SPRUCE_LOG, HOLLOW_SPRUCE_LOG, STRIPPED_HOLLOW_SPRUCE_LOG);
            entries.addAfter(Items.BIRCH_LOG, HOLLOW_BIRCH_LOG, STRIPPED_HOLLOW_BIRCH_LOG);
            entries.addAfter(Items.JUNGLE_LOG, HOLLOW_JUNGLE_LOG, STRIPPED_HOLLOW_JUNGLE_LOG);
            entries.addAfter(Items.DARK_OAK_LOG, HOLLOW_DARK_OAK_LOG, STRIPPED_HOLLOW_DARK_OAK_LOG);
            entries.addAfter(Items.ACACIA_LOG, HOLLOW_ACACIA_LOG, STRIPPED_HOLLOW_ACACIA_LOG);
            entries.addAfter(Items.MANGROVE_LOG, HOLLOW_MANGROVE_LOG, STRIPPED_HOLLOW_MANGROVE_LOG);
            entries.addAfter(Items.CHERRY_LOG, HOLLOW_CHERRY_LOG, STRIPPED_HOLLOW_CHERRY_LOG);
            entries.addAfter(Items.WARPED_STEM, HOLLOW_WARPED_STEM, STRIPPED_HOLLOW_WARPED_STEM);
            entries.addAfter(Items.CRIMSON_STEM, HOLLOW_CRIMSON_STEM, STRIPPED_HOLLOW_CRIMSON_STEM);
            entries.addAfter(Items.LILY_PAD, ALGAE);
            replaceMossCarpet(entries.getSearchTabStacks());
            replaceMossCarpet(entries.getDisplayStacks());
        });
    }

    private static void replaceMossCarpet(List<ItemStack> stacks) {
        stacks.replaceAll(stack -> {
            if (stack.isOf(Items.MOSS_CARPET))
                return MOSS_CARPET.getDefaultStack();
            return stack;
        });
    }

    @Environment(EnvType.CLIENT)
    public static void registerClient() {
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
            return ColorHelper.add(GrassColors.getColor(.5d, 1d), 50, 50, 0);
        }, TROPICAL_FERN, LARGE_TROPICAL_FERN);
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
            return ColorHelper.add(ColorProviderRegistry.ITEM.get(Items.SHORT_GRASS).getColor(stack, tintIndex), 50, 50, 0);
        }, TROPICAL_FERN, LARGE_TROPICAL_FERN);
    }
}
