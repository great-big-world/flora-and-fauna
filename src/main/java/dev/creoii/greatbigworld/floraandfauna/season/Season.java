package dev.creoii.greatbigworld.floraandfauna.season;

import dev.creoii.greatbigworld.floraandfauna.util.ColorHelper;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaTags;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.biome.Biome;

import java.util.function.Function;

public enum Season {
    AUTUMN(Season::applyAutumnColorChange),
    WINTER(Season::applyWinterColorChange),
    SPRING(Season::applySpringColorChange),
    SUMMER(Season::applySummerColorChange);

    private final Function<Context, Integer> colorChange;

    Season(Function<Context, Integer> colorChange) {
        this.colorChange = colorChange;
    }

    public Function<Context, Integer> getColorChange() {
        return colorChange;
    }

    private static int applyAutumnColorChange(Context context) {
        RegistryEntry<Biome> biomeEntry = context.world.getBiomeFabric(context.pos);

        if (biomeEntry == null || !biomeEntry.hasKeyAndValue() || biomeEntry.isIn(FloraAndFaunaTags.NOT_AFFECTED_BY_AUTUMN)) {
            return context.defaultColor;
        }

        return ColorHelper.add(context.defaultColor, 100, 0, 0);
    }

    private static int applyWinterColorChange(Context context) {
        RegistryEntry<Biome> biomeEntry = context.world.getBiomeFabric(context.pos);

        if (biomeEntry == null || !biomeEntry.hasKeyAndValue() || biomeEntry.isIn(FloraAndFaunaTags.NOT_AFFECTED_BY_WINTER)) {
            return context.defaultColor;
        }

        return ColorHelper.add(context.defaultColor, 100, 100, 100);
    }

    private static int applySpringColorChange(Context context) {
        RegistryEntry<Biome> biomeEntry = context.world.getBiomeFabric(context.pos);

        if (biomeEntry == null || !biomeEntry.hasKeyAndValue() || biomeEntry.isIn(FloraAndFaunaTags.NOT_AFFECTED_BY_SPRING)) {
            return context.defaultColor;
        }

        return ColorHelper.add(context.defaultColor, 10, 100, 0);
    }

    private static int applySummerColorChange(Context context) {
        return context.defaultColor;
    }

    public record Context(BlockRenderView world, BlockPos pos, int defaultColor) {}
}
