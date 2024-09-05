package dev.creoii.greatbigworld.floraandfauna.season;

import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.Nullable;

public class TransitionContext {
    private Season current;
    private @Nullable Season next;
    private float percentage;

    public TransitionContext(Season currentSeason, Season nextSeason, float percentage) {
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

    public void setCurrent(Season current) {
        this.current = current;
    }

    public void setNext(@Nullable Season next) {
        this.next = next;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

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
}
