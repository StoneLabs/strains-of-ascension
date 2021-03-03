package net.stone_labs.strainsofascension;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.client.realms.dto.RealmsServer;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import net.minecraft.world.WorldSaveHandler;
import net.minecraft.world.dimension.DimensionType;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static net.stone_labs.strainsofascension.StrainsOfAscensionData.horrorMessages;
import static net.stone_labs.strainsofascension.StrainsOfAscensionData.horrorSounds;

/*
L   H  DH	    Mining Fatigue  Weakness   Hunger  Slowness    Blindness    Nausea  Poison  Wither
1   40		    0
2   24	16	    0	            0
3   8	16	    0	            0	        0
4   -8	16	    0	            0	        1	    0
5   -24	16	    0	            1	        2	    0	        0
6   -32	8	    0	            2           3	    1	        0                                  + INSANITY
7   -48	16	    1	            2	        4	    2	        0	        0	    0              + INSANITY
8   -59	11	    3	            2	        5	    2	        0	        0	    0	    0      + INSANITY

DURATION 2400
 */

public class StrainsOfAscension implements DedicatedServerModInitializer
{
    public final static int effectDuration = 2410;
    public final static boolean showIcon = false;

    public static void setEffectLayer1(ServerPlayerEntity player)
    {
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, effectDuration, 0, true, false, showIcon));
    }
    public static void setEffectLayer2(ServerPlayerEntity player)
    {
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, effectDuration, 0, true, false, showIcon));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, effectDuration, 0, true, false, showIcon));
    }
    public static void setEffectLayer3(ServerPlayerEntity player)
    {
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, effectDuration, 0, true, false, showIcon));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, effectDuration, 0, true, false, showIcon));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, effectDuration, 0, true, false, showIcon));
    }
    public static void setEffectLayer4(ServerPlayerEntity player)
    {
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, effectDuration, 0, true, false, showIcon));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, effectDuration, 0, true, false, showIcon));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, effectDuration, 1, true, false, showIcon));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, effectDuration, 0, true, false, showIcon));
    }
    public static void setEffectLayer5(ServerPlayerEntity player)
    {
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, effectDuration, 0, true, false, showIcon));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, effectDuration, 1, true, false, showIcon));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, effectDuration, 2, true, false, showIcon));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, effectDuration, 0, true, false, showIcon));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, effectDuration, 0, true, false, showIcon));
    }
    public static void setEffectLayer6(ServerPlayerEntity player)
    {
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, effectDuration, 0, true, false, showIcon));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, effectDuration, 2, true, false, showIcon));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, effectDuration, 3, true, false, showIcon));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, effectDuration, 1, true, false, showIcon));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, effectDuration, 0, true, false, showIcon));
    }
    public static void setEffectLayer7(ServerPlayerEntity player)
    {
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, effectDuration, 1, true, false, showIcon));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, effectDuration, 2, true, false, showIcon));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, effectDuration, 4, true, false, showIcon));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, effectDuration, 2, true, false, showIcon));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, effectDuration, 0, true, false, showIcon));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, effectDuration, 0, true, false, showIcon));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, effectDuration, 0, true, false, showIcon));
    }
    public static void setEffectLayer8(ServerPlayerEntity player)
    {
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, effectDuration, 2, true, false, showIcon));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, effectDuration, 2, true, false, showIcon));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, effectDuration, 5, true, false, showIcon));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, effectDuration, 2, true, false, showIcon));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, effectDuration, 0, true, false, showIcon));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, effectDuration, 0, true, false, showIcon));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, effectDuration, 0, true, false, showIcon));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, effectDuration, 0, true, false, showIcon));
    }

    private static void setEffects(double height, ServerPlayerEntity player, Random random)
    {
        if (player.world.getRegistryKey() != World.OVERWORLD)
            return;

        if (height >= 40)
            return; // You can live... for now...

        // Do not spam messages and audio every tick
        if (height < -64)
            height = -64;

        if (height < -59)
            setEffectLayer8(player);
        else if (height < -48)
            setEffectLayer7(player);
        else if (height < -32)
            setEffectLayer6(player);
        else if (height < -24)
            setEffectLayer5(player);
        else if (height < -8)
            setEffectLayer4(player);
        else if (height < 8)
            setEffectLayer3(player);
        else if (height < 24)
            setEffectLayer2(player);
        else if (height < 40)
            setEffectLayer1(player);

        if (height < -32 && random.nextFloat() < ((-0.0001f)*height - 0.0032f))
            player.sendMessage(new LiteralText(horrorMessages.get(random.nextInt(horrorMessages.size()))), false);
        if (height < -32 && random.nextFloat() < ((-0.0002f)*height - 0.0064f))
            player.playSound(horrorSounds.get(random.nextInt(horrorSounds.size())), SoundCategory.AMBIENT, random.nextFloat(), 0);
    }

    public static class ServerTickEvent implements ServerTickEvents.EndTick
    {
        public static Random random = new Random();

        @Override
        public void onEndTick(MinecraftServer server)
        {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList())
                setEffects(player.getPos().y, player, random);
        }
    }

    public static final Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "strainsofascension";
    public static final String MOD_NAME = "Strains of Ascension";
    public static final String VERSION = "1.0.0";

    @Override
    public void onInitializeServer()
    {
        ServerTickEvents.END_SERVER_TICK.register(new ServerTickEvent());
        LOGGER.log(Level.INFO, "Initialized {} version {}", MOD_NAME, VERSION);
    }
}
