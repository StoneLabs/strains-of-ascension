package net.stone_labs.strainsofascension.commands;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.GameProfileArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.DataCommand;
import net.minecraft.server.command.GameModeCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.dedicated.command.OpCommand;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.stone_labs.strainsofascension.StrainsOfAscension;
import net.stone_labs.strainsofascension.artifacts.ArtifactManager;
import net.stone_labs.strainsofascension.entities.VexBossEntity;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Objects;

import static net.minecraft.command.argument.EntityArgumentType.getPlayers;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class ArtifactsCommand
{
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher)
    {
        dispatcher.register(literal("artifacts")
                .requires((source) -> source.hasPermissionLevel(2))
                .then(literal("get")
                        .then(argument("targets", EntityArgumentType.players())
                                .executes((context) -> get(context.getSource(), getPlayers(context, "targets")))))
                .then(literal("give")
                        .then(argument("targets", EntityArgumentType.players())
                                .executes((context) -> give(context.getSource(), getPlayers(context, "targets")))))
                .then(literal("spawn")
                        .then(literal("vexboss")
                                .executes((context) -> spawnVexBoss(context.getSource()))))
                .then(literal("profiler")
                        .then(literal("on")
                                .executes((context) -> profiler(context.getSource(), true)))
                        .then(literal("off")
                                .executes((context) -> profiler(context.getSource(), false)))
                        .executes((context) -> profiler(context.getSource(), null)))

        );
    }

    private static int get(ServerCommandSource source, Collection<ServerPlayerEntity> targets) throws CommandSyntaxException
    {
        StrainsOfAscension.queueArtifactDebug = true;
        StrainsOfAscension.queueArtifactDebugFor = targets;

        return 0;
    }

    private static int give(ServerCommandSource source, Collection<ServerPlayerEntity> targets) throws CommandSyntaxException
    {
        source.sendFeedback(Text.literal("Generating Artifacts, this may take a while..."), true);
        for (ServerPlayerEntity player : targets)
        {
            ArtifactManager.DropPlayerFullPool(player);
            source.sendFeedback(Text.literal(String.format("Artifacts generated for %s.", player.getEntityName())), true);
        }

        source.sendFeedback(Text.literal("Artifacts generated."), true);

        return 0;
    }

    private static int spawnVexBoss(ServerCommandSource source) throws CommandSyntaxException
    {
        if (source.getEntity() == null)
            return 1;

        new VexBossEntity(source.getWorld(), source.getEntity().getBlockPos());
        return 0;
    }

    private static int profiler(ServerCommandSource source, @Nullable Boolean state) throws CommandSyntaxException
    {
        StrainsOfAscension.profiler = Objects.requireNonNullElseGet(state, () -> !StrainsOfAscension.profiler);
        source.sendFeedback(
                Text.literal(String.format(
                        "%s profiler %s",
                        state == null ? "Toggled" : "Turned",
                        StrainsOfAscension.profiler ? "on" : "off")), true);

        return 0;
    }
}
