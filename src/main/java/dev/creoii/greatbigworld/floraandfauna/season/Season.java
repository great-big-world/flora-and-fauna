package dev.creoii.greatbigworld.floraandfauna.season;

import dev.creoii.greatbigworld.floraandfauna.util.ColorHelper;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaTags;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.biome.Biome;

import java.util.function.Function;

public enum Season {
    AUTUMN("season.autumn", Season::applyAutumnColorChange, Season::applyAutumnColorChangeParticle),
    WINTER("season.winter", Season::applyWinterColorChange, Season::applyWinterColorChangeParticle),
    SPRING("season.spring", Season::applySpringColorChange, Season::applySpringColorChangeParticle),
    SUMMER("season.summer", Season::applySummerColorChange, Season::applySummerColorChange);

    private final String translationKey;
    private final Function<Context, Integer> colorChange;
    private final Function<Context, Integer> colorChangeParticle;

    Season(String translationKey, Function<Context, Integer> colorChange, Function<Context, Integer> colorChangeParticle) {
        this.translationKey = translationKey;
        this.colorChange = colorChange;
        this.colorChangeParticle = colorChangeParticle;
    }

    /**
     * Unused for now. Will be used in Adventures' world start options.
     */
    public String getTranslationKey() {
        return translationKey;
    }

    public Function<Context, Integer> getColorChange() {
        return colorChange;
    }

    public Function<Context, Integer> getColorChangeParticle() {
        return colorChangeParticle;
    }

    private static int applyAutumnColorChange(Context context) {
        RegistryEntry<Biome> biomeEntry = context.world.getBiomeFabric(context.pos);

        if (biomeEntry == null || !biomeEntry.hasKeyAndValue() || biomeEntry.isIn(FloraAndFaunaTags.NOT_AFFECTED_BY_AUTUMN)) {
            return context.defaultColor;
        }

        return ColorHelper.add(context.defaultColor, 100, 0, 0);
    }

    private static int applyAutumnColorChangeParticle(Context context) {
        return ColorHelper.add(context.defaultColor, 100, 0, 0);
    }

    private static int applyWinterColorChange(Context context) {
        RegistryEntry<Biome> biomeEntry = context.world.getBiomeFabric(context.pos);

        if (biomeEntry == null || !biomeEntry.hasKeyAndValue() || biomeEntry.isIn(FloraAndFaunaTags.NOT_AFFECTED_BY_WINTER)) {
            return context.defaultColor;
        }

        return ColorHelper.add(context.defaultColor, 90, 100, 100);
    }

    private static int applyWinterColorChangeParticle(Context context) {
        return ColorHelper.add(context.defaultColor, 90, 100, 100);
    }

    private static int applySpringColorChange(Context context) {
        RegistryEntry<Biome> biomeEntry = context.world.getBiomeFabric(context.pos);

        if (biomeEntry == null || !biomeEntry.hasKeyAndValue() || biomeEntry.isIn(FloraAndFaunaTags.NOT_AFFECTED_BY_SPRING)) {
            return context.defaultColor;
        }

        return ColorHelper.add(context.defaultColor, 10, 100, 0);
    }

    private static int applySpringColorChangeParticle(Context context) {
        return ColorHelper.add(context.defaultColor, 10, 100, 0);
    }

    private static int applySummerColorChange(Context context) {
        return context.defaultColor;
    }

    public record Context(BlockRenderView world, BlockPos pos, int defaultColor) {}
}
