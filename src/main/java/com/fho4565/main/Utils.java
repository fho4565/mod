package com.fho4565.main;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.NbtPathArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Score;
import net.minecraft.world.scores.Scoreboard;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.CompletableFuture;


public class Utils {
    public static final String MODID = "more_cd";

    public static SuggestionProvider<CommandSourceStack> createCommandSuggestionC(String[] strings) {
        return SuggestionProviders.register(new ResourceLocation(""),
                (context, builder) ->
                        SharedSuggestionProvider.suggest(strings,
                                builder));
    }

    public static CompletableFuture<Suggestions> createCommandSuggestion(String[] strings, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggest(strings, builder);
    }

    public static String getWorldPath(CommandContext<CommandSourceStack> context) {
        return context.getSource().getServer().getWorldPath(new LevelResource("")).toFile().getAbsolutePath();
    }

    /**
     * @param playerName 玩家名字
     * @param objective  要获取的计分板名字
     */
    public static int getScore(MinecraftServer server, String playerName, Objective objective) {
        Scoreboard scoreboard = server.getScoreboard();
        if (!scoreboard.hasPlayerScore(playerName, objective)) {
            return 0;
        } else {
            Score score = scoreboard.getOrCreatePlayerScore(playerName, objective);
            return score.getScore();
        }
    }

    public static Tag getData(CommandContext<CommandSourceStack> commandContext, String resourceLocation, String path) throws CommandSyntaxException {
        Collection<Tag> tags = NbtPathArgument.getPath(commandContext, path).get(commandContext.getSource().getServer().getCommandStorage().get(ResourceLocationArgument.getId(commandContext, resourceLocation)));
        Iterator<Tag> iterator = tags.iterator();
        return iterator.next();
    }

    public static void setData(CommandContext<CommandSourceStack> commandContext, String sourceResourceLocation, String sourcePath, Tag tag) throws CommandSyntaxException {
        NbtPathArgument.getPath(commandContext, sourcePath).set(commandContext.getSource().getServer().getCommandStorage().get(ResourceLocationArgument.getId(commandContext, sourceResourceLocation)), () -> tag);
    }

    public static void sendTCdFeedback(CommandContext<net.minecraft.commands.CommandSourceStack> commandContext, String key, String... strings) {
        commandContext.getSource().sendSuccess(new TranslatableComponent(key, (Object[]) strings), false);
    }

    public static void sendTCdFeedback(CommandContext<net.minecraft.commands.CommandSourceStack> commandContext, String key, Boolean aborted, String... strings) {
        if (!aborted) {
            commandContext.getSource().sendSuccess(new TranslatableComponent(key, (Object[]) strings), false);
        } else {
            commandContext.getSource().sendFailure(new TranslatableComponent(key, (Object[]) strings));
        }
    }

    public static void sendCdFeedback(CommandContext<net.minecraft.commands.CommandSourceStack> commandContext, String content) {
        commandContext.getSource().sendSuccess(new TextComponent(content), false);
    }

    public static void sendCdFeedback(CommandContext<net.minecraft.commands.CommandSourceStack> commandContext, String content, Boolean aborted) {
        if (!aborted) {
            commandContext.getSource().sendSuccess(new TextComponent(content), false);
        } else {
            commandContext.getSource().sendFailure(new TextComponent(content));
        }
    }
}
