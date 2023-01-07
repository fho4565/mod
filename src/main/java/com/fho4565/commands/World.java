package com.fho4565.commands;

import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;

import java.util.Objects;


public class World {
    public static void register() {
        CommandRegister.dispatcher.register(
                Commands.literal("world")
                        .then(Commands.literal("spawnX").executes(context -> {
                            context.getSource().sendSuccess(new TextComponent("当前世界出生点X坐标为："+context.getSource().getLevel().getLevelData().getXSpawn()),false);
                            return context.getSource().getLevel().getLevelData().getXSpawn();
                        }))
                        .then(Commands.literal("spawnY").executes(context -> {
                            context.getSource().sendSuccess(new TextComponent("当前世界出生点Y坐标为："+context.getSource().getLevel().getLevelData().getYSpawn()),false);
                            return context.getSource().getLevel().getLevelData().getYSpawn();
                        }))
                        .then(Commands.literal("spawnZ").executes(context -> {
                            context.getSource().sendSuccess(new TextComponent("当前世界出生点Z坐标为："+context.getSource().getLevel().getLevelData().getZSpawn()),false);
                            return context.getSource().getLevel().getLevelData().getZSpawn();
                        }))
                        .then(Commands.literal("version").executes(context -> {
                            String ver = context.getSource().getServer().getServerVersion();
                            String[] split = ver.split("\\.");
                            int version = 0;
                            for (int i = 0; i < split.length; i++) {
                                version = (int) ((version + Integer.parseInt(split[i])) * 1000/java.lang.Math.pow(10, i+1));
                            }
                            context.getSource().sendSuccess(new TextComponent("当前游戏版本为"+ver),false);
                            return version;
                        }))
                        .then(Commands.literal("tickTime").executes(context -> {
                            double tickTime = (mean(Objects.requireNonNull(context.getSource().getServer().getTickTime(context.getSource().getLevel().dimension()))) * 1.0E-6D);
                            context.getSource().sendSuccess(new TextComponent("当前世界的tick time为"+tickTime),false);
                            return (int) tickTime;
                        }))
                        .then(Commands.literal("tps").executes(context -> {
                            double tps = (java.lang.Math.min(1000.0 / (mean(Objects.requireNonNull(context.getSource().getServer().getTickTime(context.getSource().getLevel().dimension()))) * 1.0E-6D), 20));
                            context.getSource().sendSuccess(new TextComponent("当前世界的tps为"+tps),false);
                            return (int) tps;
                        }))
        );
    }

    private static long mean(long[] values)
    {
        long sum = 0L;
        for (long v : values)
            sum += v;
        return sum / values.length;
    }

}
