package dev.creoii.greatbigworld.floraandfauna.registry;

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
                        context.getSource().sendFeedback(() -> Text.literal("The current season is " + StringUtils.capitalize(SeasonManager.getInstance(context.getSource().getServer()).getCurrentSeason().name().toLowerCase())), false);
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
                                String season = context.getArgument("season", String.class);
                                SeasonManager manager = SeasonManager.getInstance(context.getSource().getServer());
                                if (manager != null) {
                                    manager.setCurrentSeason(context.getSource().getWorld(), Season.valueOf(season.toUpperCase()), false);
                                } else {
                                    context.getSource().sendError(Text.literal("An unexpected error occurred."));
                                    return -1;
                                }
                                context.getSource().sendFeedback(() -> Text.literal("Set season to " + StringUtils.capitalize(season)), true);
                                return 1;
                            })
                    )
            );
        });
    }
}
