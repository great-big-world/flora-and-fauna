package dev.creoii.greatbigworld.floraandfauna.season;

import dev.creoii.greatbigworld.util.ColorHelper;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaTags;
import dev.creoii.greatbigworld.world.fastnoise.FastNoiseLite;
import java.util.function.Function;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.biome.Biome;

public enum Season {
    AUTUMN("season.autumn", Season::applyAutumnColorChangeFoliage, Season::applyAutumnColorChangeGrass, Season::applyAutumnColorChangeParticle, FloraAndFaunaTags.NOT_AFFECTED_BY_AUTUMN),
    WINTER("season.winter", Season::applyWinterColorChange, Season::applyWinterColorChangeParticle, FloraAndFaunaTags.NOT_AFFECTED_BY_WINTER),
    SPRING("season.spring", Season::applySpringColorChange, Season::applySpringColorChangeParticle, FloraAndFaunaTags.NOT_AFFECTED_BY_SPRING),
    SUMMER("season.summer", Season::applySummerColorChange, Season::applySummerColorChange, FloraAndFaunaTags.NOT_AFFECTED_BY_SUMMER);

    private static final FastNoiseLite AUTUMN_COLOR_NOISE = new FastNoiseLite();
    private final String translationKey;
    private final Function<Context, Integer> foliageColorChange;
    private final Function<Context, Integer> grassColorChange;
    private final Function<Context, Integer> particleColorChange;
    private final TagKey<Biome> unaffectedBiomes;

    Season(String translationKey, Function<Context, Integer> foliageColorChange, Function<Context, Integer> grassColorChange, Function<Context, Integer> particleColorChange, TagKey<Biome> unaffectedBiomes) {
        this.translationKey = translationKey;
        this.foliageColorChange = foliageColorChange;
        this.grassColorChange = grassColorChange;
        this.particleColorChange = particleColorChange;
        this.unaffectedBiomes = unaffectedBiomes;
    }

    Season(String translationKey, Function<Context, Integer> colorChange, Function<Context, Integer> particleColorChange, TagKey<Biome> unaffectedBiomes) {
        this(translationKey, colorChange, colorChange, particleColorChange, unaffectedBiomes);
    }

    public static Season getNextSeason(Season season) {
        return switch (season) {
            case AUTUMN -> Season.WINTER;
            case WINTER -> Season.SPRING;
            case SPRING -> Season.SUMMER;
            case SUMMER -> Season.AUTUMN;
        };
    }

    public String getTranslationKey() {
        return translationKey;
    }

    public Function<Context, Integer> getGrassColorChange() {
        return grassColorChange;
    }

    public Function<Context, Integer> getFoliageColorChange() {
        return foliageColorChange;
    }

    public Function<Context, Integer> getParticleColorChange() {
        return particleColorChange;
    }

    public TagKey<Biome> getUnaffectedBiomes() {
        return unaffectedBiomes;
    }

    private static int applyAutumnColorChangeGrass(Context context) {
        Holder<Biome> biomeEntry = context.world.getBiomeFabric(context.pos);

        if (biomeEntry == null || !biomeEntry.isBound() || biomeEntry.is(FloraAndFaunaTags.NOT_AFFECTED_BY_AUTUMN) || (Minecraft.getInstance().level != null && !Minecraft.getInstance().level.dimensionTypeRegistration().is(FloraAndFaunaTags.AFFECTED_BY_SEASONS))) {
            return context.defaultColor;
        }

        return ColorHelper.add(context.defaultColor, 100, 0, 0);
    }

    private static int applyAutumnColorChangeFoliage(Context context) {
        Holder<Biome> biomeEntry = context.world.getBiomeFabric(context.pos);

        if (biomeEntry == null || !biomeEntry.isBound() || biomeEntry.is(FloraAndFaunaTags.NOT_AFFECTED_BY_AUTUMN) || (Minecraft.getInstance().level != null && !Minecraft.getInstance().level.dimensionTypeRegistration().is(FloraAndFaunaTags.AFFECTED_BY_SEASONS))) {
            return context.defaultColor;
        }

        double blend = (AUTUMN_COLOR_NOISE.getNoise(context.pos.getX() * 6f, context.pos.getZ() * 6f) + 1d) / 2d;
        if (blend < .334d) {
            return ColorHelper.interpolate(blend * 2d, ColorHelper.add(context.defaultColor, 255, 0, 0), ColorHelper.add(context.defaultColor, 255, 165, 0));
        } else if (blend < .667d) {
            return ColorHelper.interpolate((blend - .334d) * 2d, ColorHelper.add(context.defaultColor, 255, 165, 0), ColorHelper.add(context.defaultColor, 255, 255, 0));
        } else return ColorHelper.interpolate((blend - .667d) * 2d, ColorHelper.add(context.defaultColor, 255, 255, 0), ColorHelper.add(context.defaultColor, 255, 0, 0));
    }

    private static int applyAutumnColorChangeParticle(Context context) {
        return ColorHelper.add(context.defaultColor, 100, 0, 0);
    }

    private static int applyWinterColorChange(Context context) {
        Holder<Biome> biomeEntry = context.world.getBiomeFabric(context.pos);

        if (biomeEntry == null || !biomeEntry.isBound() || biomeEntry.is(FloraAndFaunaTags.NOT_AFFECTED_BY_WINTER) || (Minecraft.getInstance().level != null && !Minecraft.getInstance().level.dimensionTypeRegistration().is(FloraAndFaunaTags.AFFECTED_BY_SEASONS))) {
            return context.defaultColor;
        }

        return ColorHelper.add(context.defaultColor, 90, 100, 100);
    }

    private static int applyWinterColorChangeParticle(Context context) {
        return ColorHelper.add(context.defaultColor, 90, 100, 100);
    }

    private static int applySpringColorChange(Context context) {
        Holder<Biome> biomeEntry = context.world.getBiomeFabric(context.pos);

        if (biomeEntry == null || !biomeEntry.isBound() || biomeEntry.is(FloraAndFaunaTags.NOT_AFFECTED_BY_SPRING) || (Minecraft.getInstance().level != null && !Minecraft.getInstance().level.dimensionTypeRegistration().is(FloraAndFaunaTags.AFFECTED_BY_SEASONS))) {
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

    public static int applyOverworldColorChange(int color) {
        return ColorHelper.add(color, 5, 75, 0);
    }

    public record Context(BlockAndTintGetter world, BlockPos pos, int defaultColor) {}
}
