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
import net.minecraft.text.LiteralText;
import net.minecraft.world.World;
import net.minecraft.world.WorldSaveHandler;
import net.minecraft.world.dimension.DimensionType;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
L   H  DH	    Mining Fatigue	Weakness	Hunger	Slowness	Blindness	Nausea	Poison	Wither
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

    public static final List<String> horrorMessages = new ArrayList<String>() {{
        add("LEAVE THIS PLACE");
        add("LEAVE OR DIE");
        add("I CAN HEAR THEM");
        add("HELP MEE");
        add("IT HURTS");
        add("die");
        add("WHERE ARE YOU");
        add("DIE!");
        add("DIE! DIE! DIE!");
        add("LEAVE!");
        add("GET OUT!");
        add("AAAAAAAAAAAAHHHH");
        add("HELP ME PLEASE");
        add("I WANNT TO LEAVE");
        add("PLEASE GET OUT");
        add("ITS IN MY MIND");
        add("I'M SCARED");
        add("PLEASE NO MORE");
        add("ESCAPE");
        add("THERE IS NO ESCAPE");
        add("YOU WILL DIE HERE");
        add("CRY! CRY! CRY!");
        add("I HAVE TO LEAVE");
        add("I NEED TO GET OUT");
        add("I WANT TO GO HOME");
        add("WE'LL DIE!");
        add("OH GOD PLEASE NO MORE");
        add("DEATH!!!");
        add("DEATH!");
        add("DEATH DEATH DEATH");
        add("IT IS TOO MUCH");
    }};

    private static void setEffects(double height, ServerPlayerEntity player, Random random)
    {
        if (player.world.getRegistryKey() != World.OVERWORLD)
            return;

        if (height >= 40)
            return; // You can live... for now...

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

        if (height < -32 && random.nextFloat() < ((-0.0024f)*height - 0.0768f))
            player.sendMessage(new LiteralText(horrorMessages.get(random.nextInt(horrorMessages.size()))), false);
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
