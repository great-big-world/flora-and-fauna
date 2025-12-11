package dev.creoii.greatbigworld.floraandfauna.season;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.greatbigworld.util.ColorHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.Nullable;

public class TransitionContext {
    public static final Codec<TransitionContext> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(Codec.INT.optionalFieldOf("current_season", 3).forGetter(manager -> {
            return manager.current.ordinal();
        }), Codec.INT.optionalFieldOf("next_season", 0).forGetter(manager -> {
            return manager.next == null ? manager.current.ordinal() : manager.next.ordinal();
        }), Codec.FLOAT.optionalFieldOf("percentage", 0f).forGetter(manager -> {
            return manager.percentage;
        })).apply(instance, TransitionContext::new);
    });

    /**
     * The current season is different than the {@link SeasonManager#currentSeason}.
     * <p>This is the transitional current season, which stays the same for the entire transition. The {@link SeasonManager#currentSeason} changes halfway through the transition.</p>
     */
    private Season current;
    private @Nullable Season next;
    private float percentage;

    public TransitionContext(int currentSeason, int nextSeason, float percentage) {
        this(Season.values()[currentSeason], Season.values()[nextSeason], percentage);
    }

    public TransitionContext(Season currentSeason, @Nullable Season nextSeason, float percentage) {
        current = currentSeason;
        next = nextSeason;
        this.percentage = percentage;
    }

    public Season getCurrent() {
        return current;
    }

    public @Nullable Season getNext() {
        return next;
    }

    public float getPercentage() {
        return percentage;
    }

    public void setSeason(Season current, @Nullable Season next) {
        this.current = current;
        this.next = next;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    public int getSeasonGrassColor(BlockAndTintGetter world, BlockPos pos, int color) {
        if (world == null)
            return color;

        Holder<Biome> biomeEntry = world.getBiomeFabric(pos);
        if (biomeEntry == null || !biomeEntry.isBound()) {
            return color;
        }

        Season.Context context = new Season.Context(world, pos, color);
        return ColorHelper.interpolate(getPercentage(), getCurrent().getGrassColorChange().apply(context), getNext() == null ? getCurrent().getGrassColorChange().apply(context) : getNext().getGrassColorChange().apply(context));
    }

    public int getSeasonFoliageColor(BlockAndTintGetter world, BlockPos pos, int color) {
        if (world == null)
            return color;

        Holder<Biome> biomeEntry = world.getBiomeFabric(pos);
        if (biomeEntry == null || !biomeEntry.isBound()) {
            return color;
        }

        Season.Context context = new Season.Context(world, pos, color);
        return ColorHelper.interpolate(getPercentage(), getCurrent().getFoliageColorChange().apply(context), getNext() == null ? getCurrent().getFoliageColorChange().apply(context) : getNext().getFoliageColorChange().apply(context));
    }
}
