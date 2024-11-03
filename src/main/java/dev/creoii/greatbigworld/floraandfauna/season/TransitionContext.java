package dev.creoii.greatbigworld.floraandfauna.season;

import dev.creoii.greatbigworld.util.ColorHelper;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Nullable;

public class TransitionContext {
    /**
     * The current season is different than the {@link SeasonManager#currentSeason}.
     * <p>This is the transitional current season, which stays the same for the entire transition. The {@link SeasonManager#currentSeason} changes halfway through the transition.</p>
     */
    private Season current;
    private @Nullable Season next;
    private float percentage;

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

    /**
     * @return an NbtCompound storing three integers, representing a TransitionContext that can be sent via packets.
     */
    public NbtCompound writeNbt() {
        NbtCompound nbt = new NbtCompound();
        nbt.putInt("current_season", current.ordinal());
        nbt.putInt("next_season", next == null ? current.ordinal() : next.ordinal());
        nbt.putFloat("percentage", percentage);
        return nbt;
    }

    protected static TransitionContext readNbt(NbtCompound nbt) {
        return new TransitionContext(Season.values()[nbt.getInt("current_season")], Season.values()[nbt.getInt("next_season")], nbt.getFloat("percentage"));
    }

    public int getSeasonGrassColor(BlockRenderView world, BlockPos pos, int color) {
        if (world == null)
            return color;

        RegistryEntry<Biome> biomeEntry = world.getBiomeFabric(pos);
        if (biomeEntry == null || !biomeEntry.hasKeyAndValue()) {
            return color;
        }

        Season.Context context = new Season.Context(world, pos, color);
        return ColorHelper.interpolate(getPercentage(), getCurrent().getGrassColorChange().apply(context), getNext() == null ? getCurrent().getGrassColorChange().apply(context) : getNext().getGrassColorChange().apply(context));
    }

    public int getSeasonFoliageColor(BlockRenderView world, BlockPos pos, int color) {
        if (world == null)
            return color;

        RegistryEntry<Biome> biomeEntry = world.getBiomeFabric(pos);
        if (biomeEntry == null || !biomeEntry.hasKeyAndValue()) {
            return color;
        }

        Season.Context context = new Season.Context(world, pos, color);
        return ColorHelper.interpolate(getPercentage(), getCurrent().getFoliageColorChange().apply(context), getNext() == null ? getCurrent().getFoliageColorChange().apply(context) : getNext().getFoliageColorChange().apply(context));
    }
}
