package net.stone_labs.strainsofascension;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.mixin.gamerule.IntRuleAccessor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameRules;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;


public class StrainsOfAscension implements DedicatedServerModInitializer
{
    public static final Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "strainsofascension";
    public static final String MOD_NAME = "Strains of Ascension";
    public static final String VERSION = "2.1.0";

    public static class ServerTickEvent implements ServerTickEvents.EndTick
    {
        public static Random random = new Random();

        @Override
        public void onEndTick(MinecraftServer server)
        {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList())
                StrainsOfAscensionEffects.setEffects(player.getPos().y, player, random);
        }
    }

    @Override
    public void onInitializeServer()
    {
        ServerTickEvents.END_SERVER_TICK.register(new ServerTickEvent());
        LOGGER.log(Level.INFO, "Initialized {} version {}", MOD_NAME, VERSION);
    }

    public static final GameRules.Key<GameRules.BooleanRule> DO_BLINDNESS = register("doBlindnessStrain", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(true, (server, rule) ->
    {
        StrainsOfAscensionEffects.doBlindness = rule.get();
    }));
    public static final GameRules.Key<GameRules.IntRule> BLINDNESS_NV_MULTIPLIER = register("blindnessStrainNVSpeedMultiplier", GameRules.Category.PLAYER, GameRuleFactory.createIntRule(20, 0, (server, rule) ->
    {
        StrainsOfAscensionEffects.blindnessNVMultiplier = rule.get();
    }));

    private static <T extends GameRules.Rule<T>> GameRules.Key<T> register(String name, GameRules.Category category, GameRules.Type<T> type) {
        return GameRuleRegistry.register(name, category, type);
    }
}
