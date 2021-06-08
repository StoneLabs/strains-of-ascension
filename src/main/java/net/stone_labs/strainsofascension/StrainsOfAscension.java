package net.stone_labs.strainsofascension;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.gamerule.v1.rule.DoubleRule;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameRules;
import net.stone_labs.strainsofascension.effects.BlindnessStrain;
import net.stone_labs.strainsofascension.effects.PoisonNauseaStrain;
import net.stone_labs.strainsofascension.effects.WitherStrain;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StrainsOfAscension implements DedicatedServerModInitializer
{
    public static final Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "strainsofascension";
    public static final String MOD_NAME = "Strains of Ascension";
    public static final String VERSION = "2.4.0";

    public static class ServerTickEvent implements ServerTickEvents.EndTick
    {
        @Override
        public void onEndTick(MinecraftServer server)
        {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList())
                StrainManager.applyEffects(player);
        }
    }

    @Override
    public void onInitializeServer()
    {
        ServerTickEvents.END_SERVER_TICK.register(new ServerTickEvent());
        LOGGER.log(Level.INFO, "Initialized {} version {}", MOD_NAME, VERSION);

        // Set values from gamerules on server start
        ServerLifecycleEvents.SERVER_STARTED.register(server ->
        {
            BlindnessStrain.doBlindness = server.getGameRules().get(DO_BLINDNESS_STRAIN).get();
            BlindnessStrain.allowNVCancelNether = server.getGameRules().get(ALLOW_NV_CANCEL_NETHER).get();
            BlindnessStrain.NVCancelMultiplier = server.getGameRules().get(NV_CANCEL_SPEED_MULTIPLIER).get();
            PoisonNauseaStrain.doPoisonNausea = server.getGameRules().get(DO_POISON_STRAIN).get();
            PoisonNauseaStrain.doNausea = server.getGameRules().get(DO_NAUSEA_WITH_POISON_STRAIN).get();
            WitherStrain.doWither = server.getGameRules().get(DO_WITHER_STRAIN).get();
            StrainManager.doNether = server.getGameRules().get(DO_NETHER_STRAINS).get();
            StrainManager.showIcon = server.getGameRules().get(SHOW_STRAIN_ICONS).get();
            StrainManager.doCreative = server.getGameRules().get(DO_CREATIVE_STRAINS).get();
            StrainManager.doSpectator = server.getGameRules().get(DO_SPECTATOR).get();
            StrainManager.localDifficultyEffectMultiplier = server.getGameRules().get(LOCAL_DIFFICULTY_LAYER_IMPACT).get();
            StrainManager.lunarDifficultyEffectMultiplier = server.getGameRules().get(LUNAR_DIFFICULTY_LAYER_IMPACT).get();
            StrainManager.debugHeight = server.getGameRules().get(DO_DEBUG_STRAIN_HEIGHT).get();
        });
    }

    public static final GameRules.Key<GameRules.BooleanRule> DO_BLINDNESS_STRAIN = register("doBlindnessStrain", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(true, (server, rule) ->
    {
        BlindnessStrain.doBlindness = rule.get();
    }));
    public static final GameRules.Key<GameRules.BooleanRule> ALLOW_NV_CANCEL_NETHER = register("allowNVCancelNether", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(true, (server, rule) ->
    {
        BlindnessStrain.allowNVCancelNether = rule.get();
    }));
    public static final GameRules.Key<GameRules.IntRule> NV_CANCEL_SPEED_MULTIPLIER = register("NVCancelSpeedMultiplier", GameRules.Category.PLAYER, GameRuleFactory.createIntRule(5, 0, (server, rule) ->
    {
        BlindnessStrain.NVCancelMultiplier = rule.get();
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
    public static final GameRules.Key<DoubleRule> LOCAL_DIFFICULTY_LAYER_IMPACT = register("localDifficultyLayerImpact", GameRules.Category.PLAYER, GameRuleFactory.createDoubleRule(1.0, 0, (server, rule) ->
    {
        StrainManager.localDifficultyEffectMultiplier = rule.get();
    }));
    public static final GameRules.Key<DoubleRule> LUNAR_DIFFICULTY_LAYER_IMPACT = register("lunarDifficultyLayerImpact", GameRules.Category.PLAYER, GameRuleFactory.createDoubleRule(1.0, 0, (server, rule) ->
    {
        StrainManager.lunarDifficultyEffectMultiplier = rule.get();
    }));
    public static final GameRules.Key<GameRules.BooleanRule> DO_DEBUG_STRAIN_HEIGHT = register("doDebugStrainHeight", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(false, (server, rule) ->
    {
        StrainManager.debugHeight = rule.get();
    }));

    private static <T extends GameRules.Rule<T>> GameRules.Key<T> register(String name, GameRules.Category category, GameRules.Type<T> type)
    {
        return GameRuleRegistry.register(name, category, type);
    }
}
