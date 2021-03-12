package net.stone_labs.strainsofascension;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.LiteralText;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

import static net.stone_labs.strainsofascension.StrainsOfAscensionData.horrorMessages;
import static net.stone_labs.strainsofascension.StrainsOfAscensionData.horrorSounds;

/*
L   H  DH	    Mining Fatigue  Weakness   Hunger  Slowness    Blindness    Nausea  Poison  Wither
1   40		    0
2   24	16	    0	            0
3   8	16	    0	            0	        0
4   -8	16	    0	            0	        1	    0
5   -24	16	    0	            1	        2	    0	        (0)
6   -32	8	    0	            1           3	    1	        (0)                                   + INSANITY
7   -48	16	    1	            1	        4	    2	         0	        0R	    0R                + INSANITY
8   -59	11	    1               1	        5	    2	         0	        0R	    1R	    1R        + INSANITY
NETHER          1               1           6       2           (0)         0R      2R      2R        + INSANITY

DURATION 120*20
 */

public final class StrainsOfAscensionEffects
{
    private StrainsOfAscensionEffects() {};

    public final static int effectDuration = 120 * 20 + 10;
    public final static int effectDurationBlindness = 3 * 10 + 10;
    public final static float effectRandomProbability = 1.0f / effectDuration;
    public final static boolean showIcon = false;

    public static boolean doBlindness = true;
    public static boolean allowNVCancelNether = true;
    public static int blindnessNVMultiplier = 20;

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
        setEffectNightVisionBlindness(player, true);
    }

    public static void setEffectLayer6(ServerPlayerEntity player)
    {
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, effectDuration, 0, true, false, showIcon));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, effectDuration, 1, true, false, showIcon));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, effectDuration, 3, true, false, showIcon));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, effectDuration, 1, true, false, showIcon));
        setEffectNightVisionBlindness(player, true);
    }

    public static void setEffectLayer7(ServerPlayerEntity player, Random random)
    {
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, effectDuration, 1, true, false, showIcon));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, effectDuration, 1, true, false, showIcon));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, effectDuration, 4, true, false, showIcon));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, effectDuration, 2, true, false, showIcon));
        if (random.nextFloat() < effectRandomProbability)
        {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, effectDuration, 0, true, false, showIcon));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, effectDuration, 0, true, false, showIcon));
        }
        setEffectNightVisionBlindness(player, false);
    }

    public static void setEffectLayer8(ServerPlayerEntity player, Random random)
    {
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, effectDuration, 1, true, false, showIcon));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, effectDuration, 1, true, false, showIcon));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, effectDuration, 5, true, false, showIcon));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, effectDuration, 2, true, false, showIcon));
        if (random.nextFloat() < effectRandomProbability)
        {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, effectDuration, 0, true, false, showIcon));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, effectDuration, 1, true, false, showIcon));
        }
        if (random.nextFloat() < effectRandomProbability)
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, effectDuration, 1, true, false, showIcon));
        setEffectNightVisionBlindness(player, false);
    }

    public static void setEffectLayerNether(ServerPlayerEntity player, Random random)
    {
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, effectDuration, 1, true, false, showIcon));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, effectDuration, 1, true, false, showIcon));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, effectDuration, 6, true, false, showIcon));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, effectDuration, 2, true, false, showIcon));
        if (random.nextFloat() < effectRandomProbability)
        {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, effectDuration, 0, true, false, showIcon));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, effectDuration, 2, true, false, showIcon));
        }
        if (random.nextFloat() < effectRandomProbability)
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, effectDuration, 2, true, false, showIcon));
        setEffectNightVisionBlindness(player, allowNVCancelNether);
    }

    public static void setEffectNightVisionBlindness(ServerPlayerEntity player, boolean allowNVCancel)
    {
        if (!doBlindness)
            return;

        @Nullable StatusEffectInstance nvEffect = player.getStatusEffect(StatusEffects.NIGHT_VISION);
        if (nvEffect != null)
        {
            int duration = nvEffect.getDuration();
            player.removeStatusEffect(StatusEffects.NIGHT_VISION);
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION,
                    duration - (blindnessNVMultiplier - 1), 0, true, true));
            if (allowNVCancel)
            {
                player.removeStatusEffect(StatusEffects.BLINDNESS);
                return;
            }
        }
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, effectDurationBlindness, 0, true, false, showIcon));
    }

    public static void playRandomHorrorMessage(ServerPlayerEntity player, Random random)
    {
        player.sendMessage(new LiteralText(horrorMessages.get(random.nextInt(horrorMessages.size()))), false);
    }

    public static void playRandomHorrorAudio(ServerPlayerEntity player, Random random)
    {
        player.playSound(horrorSounds.get(random.nextInt(horrorSounds.size())), SoundCategory.AMBIENT, random.nextFloat(), 0);
    }

    public static void setEffects(double height, ServerPlayerEntity player, Random random)
    {
        if (player.interactionManager.getGameMode() == GameMode.SPECTATOR
                || player.interactionManager.getGameMode() == GameMode.CREATIVE)
            return;

        if (player.world.getRegistryKey() == World.OVERWORLD)
        {
            if (height >= 40)
                return; // You can live... for now...

            // Do not spam messages and audio every tick
            if (height < -64)
                height = -64;

            if (height < -59)
                setEffectLayer8(player, random);
            else if (height < -48)
                setEffectLayer7(player, random);
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

            if (height < -32 && random.nextFloat() < ((-0.0001f) * height - 0.0032f))
                playRandomHorrorMessage(player, random);
            if (height < -32 && random.nextFloat() < ((-0.0002f) * height - 0.0064f))
                playRandomHorrorAudio(player, random);
        }
        else if (player.world.getRegistryKey() == World.NETHER)
        {
            setEffectLayerNether(player, random);

            if (random.nextFloat() < 0.0032f)
                playRandomHorrorMessage(player, random);
            if (random.nextFloat() < 0.0064f)
                playRandomHorrorAudio(player, random);

        }
    }
}
