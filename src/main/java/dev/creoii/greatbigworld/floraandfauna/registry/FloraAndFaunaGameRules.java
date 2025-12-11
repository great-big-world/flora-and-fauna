package dev.creoii.greatbigworld.floraandfauna.registry;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.serialization.Codec;
import dev.creoii.greatbigworld.GreatBigWorld;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import net.minecraft.world.level.gamerules.GameRuleType;
import net.minecraft.world.level.gamerules.GameRuleTypeVisitor;

public final class FloraAndFaunaGameRules {
    public static GameRule<Boolean> ADVANCE_SEASONS;
    public static GameRule<Integer> SEASON_LENGTH;

    public static void register() {
        ADVANCE_SEASONS = Registry.register(BuiltInRegistries.GAME_RULE, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "advance_seasons"), new GameRule<>(GameRuleCategory.UPDATES, GameRuleType.BOOL, BoolArgumentType.bool(), GameRuleTypeVisitor::visitBoolean, Codec.BOOL, value -> value ? 1 : 0, true, FeatureFlagSet.of()));
        SEASON_LENGTH = Registry.register(BuiltInRegistries.GAME_RULE, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "season_length"), new GameRule<>(GameRuleCategory.UPDATES, GameRuleType.INT, IntegerArgumentType.integer(100, Integer.MAX_VALUE), GameRuleTypeVisitor::visitInteger, Codec.intRange(100, Integer.MAX_VALUE), value -> value, 86400, FeatureFlagSet.of()));
    }
}
