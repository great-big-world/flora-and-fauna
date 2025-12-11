package dev.creoii.greatbigworld.floraandfauna.registry;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.creoii.greatbigworld.floraandfauna.season.Season;
import dev.creoii.greatbigworld.floraandfauna.season.SeasonManager;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.StringUtils;

public final class FloraAndFaunaCommands {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(LiteralArgumentBuilder.<CommandSourceStack>literal("season")
                    .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                    .executes(context -> {
                        SeasonManager seasonManager = SeasonManager.getInstance(context.getSource().getServer());
                        if (seasonManager == null) {
                            context.getSource().sendFailure(Component.literal("An unexpected error occurred"));
                            return -1;
                        }
                        context.getSource().sendSuccess(() -> Component.literal("The current season is " + StringUtils.capitalize(seasonManager.getCurrentSeason().name().toLowerCase())), false);
                        return 1;
                    })
                    .then(Commands.argument("season", StringArgumentType.string())
                            .suggests((context1, builder) -> {
                                for (Season season : Season.values()) {
                                    builder.suggest(season.name().toLowerCase());
                                }
                                return builder.buildFuture();
                            })
                            .executes(context -> {
                                return executeSetSeason(context.getSource(), StringArgumentType.getString(context, "season"), false);
                            })
                            .then(Commands.argument("preserveTime", BoolArgumentType.bool())
                                    .executes(context -> {
                                        return executeSetSeason(context.getSource(), StringArgumentType.getString(context, "season"), BoolArgumentType.getBool(context, "preserveTime"));
                                    }))
                    )
            );
        });
    }

    private static int executeSetSeason(CommandSourceStack source, String season, boolean preserveTime) {
        SeasonManager manager = SeasonManager.getInstance(source.getServer());
        if (manager != null) {
            manager.setCurrentSeason(source.getLevel(), Season.valueOf(season.toUpperCase()), preserveTime);
        } else {
            source.sendFailure(Component.literal("An unexpected error occurred"));
            return -1;
        }
        source.sendSuccess(() -> Component.literal("Set season to " + StringUtils.capitalize(season)), true);
        return 1;
    }
}
