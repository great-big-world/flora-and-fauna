package dev.creoii.greatbigworld.floraandfauna.registry;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.creoii.greatbigworld.floraandfauna.season.Season;
import dev.creoii.greatbigworld.floraandfauna.season.SeasonManager;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.apache.commons.lang3.StringUtils;

public final class FloraAndFaunaCommands {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(LiteralArgumentBuilder.<ServerCommandSource>literal("season")
                    .requires(source -> source.hasPermissionLevel(2))
                    .executes(context -> {
                        SeasonManager seasonManager = SeasonManager.getInstance(context.getSource().getServer());
                        if (seasonManager == null) {
                            context.getSource().sendError(Text.literal("An unexpected error occurred"));
                            return -1;
                        }
                        context.getSource().sendFeedback(() -> Text.literal("The current season is " + StringUtils.capitalize(seasonManager.getCurrentSeason().name().toLowerCase())), false);
                        return 1;
                    })
                    .then(CommandManager.argument("season", StringArgumentType.string())
                            .suggests((context1, builder) -> {
                                for (Season season : Season.values()) {
                                    builder.suggest(season.name().toLowerCase());
                                }
                                return builder.buildFuture();
                            })
                            .executes(context -> {
                                return executeSetSeason(context.getSource(), StringArgumentType.getString(context, "season"), false);
                            })
                            .then(CommandManager.argument("preserveTime", BoolArgumentType.bool())
                                    .executes(context -> {
                                        return executeSetSeason(context.getSource(), StringArgumentType.getString(context, "season"), BoolArgumentType.getBool(context, "preserveTime"));
                                    }))
                    )
            );
        });
    }

    private static int executeSetSeason(ServerCommandSource source, String season, boolean preserveTime) {
        SeasonManager manager = SeasonManager.getInstance(source.getServer());
        if (manager != null) {
            manager.setCurrentSeason(source.getWorld(), Season.valueOf(season.toUpperCase()), preserveTime);
        } else {
            source.sendError(Text.literal("An unexpected error occurred"));
            return -1;
        }
        source.sendFeedback(() -> Text.literal("Set season to " + StringUtils.capitalize(season)), true);
        return 1;
    }
}
