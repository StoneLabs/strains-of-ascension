package net.stone_labs.strainsofascension;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameRules;
import net.stone_labs.strainsofascension.effects.BlindnessStrain;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class StrainsOfAscension implements DedicatedServerModInitializer
{
    public static final Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "strainsofascension";
    public static final String MOD_NAME = "Strains of Ascension";
    public static final String VERSION = "2.2.1";

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
            BlindnessStrain.doBlindness = server.getGameRules().get(DO_BLINDNESS).get();
            BlindnessStrain.allowNVCancelNether = server.getGameRules().get(ALLOW_NV_CANCEL_NETHER).get();
            BlindnessStrain.NVCancelMultiplier = server.getGameRules().get(BLINDNESS_NV_MULTIPLIER).get();
        });
    }

    public static final GameRules.Key<GameRules.BooleanRule> DO_BLINDNESS = register("doBlindnessStrain", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(true, (server, rule) ->
    {
        BlindnessStrain.doBlindness = rule.get();
    }));
    public static final GameRules.Key<GameRules.BooleanRule> ALLOW_NV_CANCEL_NETHER = register("blindnessStrainNVCancelNetherAllowed", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(true, (server, rule) ->
    {
        BlindnessStrain.allowNVCancelNether = rule.get();
    }));
    public static final GameRules.Key<GameRules.IntRule> BLINDNESS_NV_MULTIPLIER = register("blindnessStrainNVSpeedMultiplier", GameRules.Category.PLAYER, GameRuleFactory.createIntRule(5, 0, (server, rule) ->
    {
        BlindnessStrain.NVCancelMultiplier = rule.get();
    }));

    private static <T extends GameRules.Rule<T>> GameRules.Key<T> register(String name, GameRules.Category category, GameRules.Type<T> type)
    {
        return GameRuleRegistry.register(name, category, type);
    }
}
