package dev.creoii.greatbigworld.floraandfauna.registry;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;

public final class FloraAndFaunaGameRules {
    public static GameRules.Key<GameRules.BooleanRule> DO_SEASON_CYCLE;
    public static GameRules.Key<GameRules.IntRule> SEASON_LENGTH;

    public static void register() {
        DO_SEASON_CYCLE = GameRuleRegistry.register("doSeasonCycle", GameRules.Category.UPDATES, GameRuleFactory.createBooleanRule(true));
        SEASON_LENGTH = GameRuleRegistry.register("seasonLength", GameRules.Category.UPDATES, GameRuleFactory.createIntRule(864000 /* 12 hours */, 100));
    }
}
