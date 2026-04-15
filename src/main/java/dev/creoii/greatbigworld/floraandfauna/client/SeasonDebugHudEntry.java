package dev.creoii.greatbigworld.floraandfauna.client;

import dev.creoii.greatbigworld.GreatBigWorld;
import net.minecraft.client.gui.components.debug.DebugEntryCategory;
import net.minecraft.client.gui.components.debug.DebugScreenDisplayer;
import net.minecraft.client.gui.components.debug.DebugScreenEntry;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jspecify.annotations.Nullable;

public class SeasonDebugHudEntry implements DebugScreenEntry {
    public static Identifier CURRENT_SEASON = Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "current_season");

    @Override
    public void display(DebugScreenDisplayer lines, @Nullable Level world, @Nullable LevelChunk clientChunk, @Nullable LevelChunk chunk) {
        if (FloraAndFaunaClient.getCurrentSeason() != null) {
            lines.addLine("Season: " + FloraAndFaunaClient.currentSeason.name() + " T: " + (FloraAndFaunaClient.getTransitionContext() == null ? "0%" : FloraAndFaunaClient.getTransitionContext().getPercentage()));
        }
    }

    @Override
    public boolean isAllowed(boolean reducedDebugInfo) {
        return true;
    }

    @Override
    public DebugEntryCategory category() {
        return DebugEntryCategory.SCREEN_TEXT;
    }
}
