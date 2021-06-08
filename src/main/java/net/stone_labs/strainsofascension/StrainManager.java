package net.stone_labs.strainsofascension;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import net.stone_labs.strainsofascension.effects.*;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static net.stone_labs.strainsofascension.StrainData.horrorMessages;

/*
L   H  DH	    Mining Fatigue  Weakness   Hunger  Slowness    Blindness    Nausea  Poison  Wither
0   *
1   40		    0
2   24	16	    0	            0
3   8	16	    0	            0	        0
4   -8	16	    0	            0	        1	    0
5   -24	16	    0	            1	        2	    0	        (0)
6   -32	8	    0	            1           3	    1	        (0)                                   + INSANITY
7   -48	16	    1	            1	        4	    2	         0	        0R	    0R                + INSANITY
8   -59	11	    1               1	        5	    2	         0	        0R	    1R	    1R        + INSANITY
9   NETHER      1               1           6       2           (0)         0R      2R      2R        + INSANITY

DURATION 120*20
 */

public final class StrainManager
{
    public interface Strain
    {
        void effect(ServerPlayerEntity player, byte layer);
    }

    public static final List<Strain> strains = new ArrayList<Strain>();

    public final static int effectDuration = 120 * 20 + 10;
    public final static int effectDurationBlindness = 3 * 20 + 10;
    public final static float effectRandomProbability = 1.0f / effectDuration;
    public static double localDifficultyEffectMultiplier;
    public static boolean showIcon = false;
    public static boolean doNether = true;
    public static boolean doCreative = false;
    public static boolean doSpectator = false;


    public static byte getOverworldLayer(double height)
    {
        if (height < -59)
            return 8;
        if (height < -48)
            return 7;
        if (height < -32)
            return 6;
        if (height < -24)
            return 5;
        if (height < -8)
            return 4;
        if (height < 8)
            return 3;
        if (height < 24)
            return 2;
        if (height < 40)
            return 1;
        return 0;
    }

    public static double getEffectPlayerHeight(ServerPlayerEntity player)
    {
        double localDifficultyImpact = localDifficultyEffectMultiplier * (player.world.getLocalDifficulty(player.getBlockPos()).getLocalDifficulty() / 6.75);
        double moonPhaseImpact = 1 * (Math.abs((Math.abs((player.world.getLunarTime() - (24000*4.75)) / 24000.0) % 8L) / 2 - 2) - 1);
        double effectivePlayerHeight = player.getPos().y - localDifficultyImpact - moonPhaseImpact;

        if (player.server.getTicks() % 20 == 0)
        {
            String message = String.format("%s: %.1f - (%.1f) - (%.1f) = %.1f",
                    player.getEntityName(),
                    player.getPos().y,
                    localDifficultyImpact,
                    moonPhaseImpact,
                    effectivePlayerHeight);
            player.sendMessage(new LiteralText(message), true);
        }

        return effectivePlayerHeight;
    }

    public static byte getLayer(ServerPlayerEntity player)
    {
        if (player.world.getRegistryKey() == World.OVERWORLD)
            return getOverworldLayer(getEffectPlayerHeight(player));
        if (player.world.getRegistryKey() == World.NETHER)
            return doNether ? (byte)9 : (byte)0;
        return 0;
    }

    public static void applyEffects(ServerPlayerEntity player)
    {
        if (player.interactionManager.getGameMode() == GameMode.SPECTATOR && !doSpectator)
            return;

        if (player.interactionManager.getGameMode() == GameMode.CREATIVE && !doCreative)
            return;

        byte layer = getLayer(player);

        for (Strain strain : strains)
            strain.effect(player, layer);
    }

    static
    {
        strains.add(new FatiqueStrain());
        strains.add(new WeaknessStrain());
        strains.add(new HungerStrain());
        strains.add(new SlownessStrain());
        strains.add(new BlindnessStrain());
        strains.add(new PoisonNauseaStrain());
        strains.add(new WitherStrain());
        strains.add(new InsanityStrain());
    }
}
