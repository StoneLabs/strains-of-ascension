package net.stone_labs.strainsofascension;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.gamerule.v1.rule.DoubleRule;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.world.GameRules;
import net.stone_labs.strainsofascension.artifacts.ArtifactManager;
import net.stone_labs.strainsofascension.artifacts.ArtifactState;
import net.stone_labs.strainsofascension.commands.ArtifactsCommand;
import net.stone_labs.strainsofascension.effects.strains.BlindnessStrain;
import net.stone_labs.strainsofascension.effects.strains.NightVisionStrain;
import net.stone_labs.strainsofascension.effects.strains.PoisonNauseaStrain;
import net.stone_labs.strainsofascension.effects.strains.WitherStrain;
import net.stone_labs.strainsofascension.entities.VexBossEntity;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;

import static net.minecraft.command.argument.EntityArgumentType.getPlayers;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class StrainsOfAscension implements DedicatedServerModInitializer
{
    public static final Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "strainsofascension";
    public static final String MOD_NAME = "Strains of Ascension";
    public static final String VERSION = "2.8.0";

    public static boolean queueArtifactDebug = false;
    public static Collection<ServerPlayerEntity> queueArtifactDebugFor;

    public static long tickTime = 0;
    public static double tickAvrg = 0;
    public static int tickNumber = 0;
    public static boolean profiler = false;

    public static class ServerTickEvent implements ServerTickEvents.EndTick
    {
        @Override
        public void onEndTick(MinecraftServer server)
        {
            long startTime = System.nanoTime();

            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList())
            {
                ArtifactState artifactState = ArtifactManager.GetPlayerArtifactState(player.getInventory());

                if (queueArtifactDebug)
                    if (queueArtifactDebugFor != null && queueArtifactDebugFor.contains(player))
                        artifactState.Debug(player, true);

                StrainManager.applyEffects(player, artifactState);
            }

            queueArtifactDebug = false;

            tickNumber++;
            tickTime += System.nanoTime() - startTime;
            if (tickNumber % 100 == 0)
            {
                tickAvrg = tickTime / 100.0 / 1000.0;
                tickTime = 0;
                if (profiler)
                    printProfilerOutput(server);
            }
        }
    }

    public static void printProfilerOutput(MinecraftServer server)
    {
        for (ServerPlayerEntity serverPlayer : server.getPlayerManager().getPlayerList())
            serverPlayer.sendMessage(new LiteralText(String.format("§2Profiler: Average %.2fμs per Server Tick over %d ticks.", tickAvrg, 100)), false);
    }

    @Override
    public void onInitializeServer()
    {
        ServerTickEvents.END_SERVER_TICK.register(new ServerTickEvent());

        LOGGER.log(Level.INFO, "Initialized {} version {}", MOD_NAME, VERSION);

        // Add lootTable loaded callback
        ArtifactManager.Init();

        // Add command to display artifacts for debugging
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> ArtifactsCommand.register(dispatcher));

        // Set values from gamerules on server start
        ServerLifecycleEvents.SERVER_STARTED.register(server ->
        {
            BlindnessStrain.doBlindness = server.getGameRules().get(DO_BLINDNESS_STRAIN).get();
            NightVisionStrain.allowNVCancelNether = server.getGameRules().get(ALLOW_NV_CANCEL_NETHER).get();
            PoisonNauseaStrain.doPoisonNausea = server.getGameRules().get(DO_POISON_STRAIN).get();
            PoisonNauseaStrain.doNausea = server.getGameRules().get(DO_NAUSEA_WITH_POISON_STRAIN).get();
            WitherStrain.doWither = server.getGameRules().get(DO_WITHER_STRAIN).get();
            StrainManager.doNether = server.getGameRules().get(DO_NETHER_STRAINS).get();
            StrainManager.showIcon = server.getGameRules().get(SHOW_STRAIN_ICONS).get();
            StrainManager.doCreative = server.getGameRules().get(DO_CREATIVE_STRAINS).get();
            StrainManager.doSpectator = server.getGameRules().get(DO_SPECTATOR).get();
            StrainManager.localDifficultyEffectMultiplier = server.getGameRules().get(LOCAL_DIFFICULTY_LAYER_IMPACT).get();
            StrainManager.lunarDifficultyEffectMultiplier = server.getGameRules().get(LUNAR_DIFFICULTY_LAYER_IMPACT).get();
            StrainManager.strainMode = server.getGameRules().get(DO_STRAINS_ON_ASCENSION).get() ? StrainManager.STRAINMODE.ASCENSION : StrainManager.STRAINMODE.DEPTH;
            StrainManager.debugHeight = server.getGameRules().get(DO_DEBUG_STRAIN_HEIGHT).get();
        });
    }

    public static final GameRules.Key<GameRules.BooleanRule> DO_BLINDNESS_STRAIN = register("doBlindnessStrain", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(true, (server, rule) ->
    {
        BlindnessStrain.doBlindness = rule.get();
    }));
    public static final GameRules.Key<GameRules.BooleanRule> ALLOW_NV_CANCEL_NETHER = register("allowNVCancelNether", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(true, (server, rule) ->
    {
        NightVisionStrain.allowNVCancelNether = rule.get();
    }));
    public static final GameRules.Key<GameRules.BooleanRule> DO_POISON_STRAIN = register("doPoisonStrain", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(true, (server, rule) ->
    {
        PoisonNauseaStrain.doPoisonNausea = rule.get();
    }));
    public static final GameRules.Key<GameRules.BooleanRule> DO_NAUSEA_WITH_POISON_STRAIN = register("doNauseaWithPoisonStrain", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(true, (server, rule) ->
    {
        PoisonNauseaStrain.doNausea = rule.get();
    }));
    public static final GameRules.Key<GameRules.BooleanRule> DO_WITHER_STRAIN = register("doWitherStrain", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(true, (server, rule) ->
    {
        WitherStrain.doWither = rule.get();
    }));
    public static final GameRules.Key<GameRules.BooleanRule> DO_NETHER_STRAINS = register("doNetherStrains", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(true, (server, rule) ->
    {
        StrainManager.doNether = rule.get();
    }));
    public static final GameRules.Key<GameRules.BooleanRule> SHOW_STRAIN_ICONS = register("showStrainIcons", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(false, (server, rule) ->
    {
        StrainManager.showIcon = rule.get();
    }));
    public static final GameRules.Key<GameRules.BooleanRule> DO_CREATIVE_STRAINS = register("doCreativeStrains", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(false, (server, rule) ->
    {
        StrainManager.doCreative = rule.get();
    }));
    public static final GameRules.Key<GameRules.BooleanRule> DO_SPECTATOR = register("doSpectatorStrains", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(false, (server, rule) ->
    {
        StrainManager.doSpectator = rule.get();
    }));
    public static final GameRules.Key<DoubleRule> LOCAL_DIFFICULTY_LAYER_IMPACT = register("localDifficultyLayerImpact", GameRules.Category.PLAYER, GameRuleFactory.createDoubleRule(3.0, 0, (server, rule) ->
    {
        StrainManager.localDifficultyEffectMultiplier = rule.get();
    }));
    public static final GameRules.Key<DoubleRule> LUNAR_DIFFICULTY_LAYER_IMPACT = register("lunarDifficultyLayerImpact", GameRules.Category.PLAYER, GameRuleFactory.createDoubleRule(3.0, 0, (server, rule) ->
    {
        StrainManager.lunarDifficultyEffectMultiplier = rule.get();
    }));
    public static final GameRules.Key<GameRules.BooleanRule> DO_DEBUG_STRAIN_HEIGHT = register("doDebugStrainHeight", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(false, (server, rule) ->
    {
        StrainManager.debugHeight = rule.get();
    }));
    public static final GameRules.Key<GameRules.BooleanRule> DO_STRAINS_ON_ASCENSION = register("doStrainsOnAscension", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(false, (server, rule) ->
    {
        StrainManager.strainMode = rule.get() ? StrainManager.STRAINMODE.ASCENSION : StrainManager.STRAINMODE.DEPTH;
    }));

    private static <T extends GameRules.Rule<T>> GameRules.Key<T> register(String name, GameRules.Category category, GameRules.Type<T> type)
    {
        return GameRuleRegistry.register(name, category, type);
    }
}
