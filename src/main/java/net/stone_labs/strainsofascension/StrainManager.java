package net.stone_labs.strainsofascension;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import net.stone_labs.strainsofascension.effects.*;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

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
    public final static int effectDurationBlindness = 3 * 10 + 10;
    public final static float effectRandomProbability = 1.0f / effectDuration;
    public final static boolean showIcon = false;


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

    public static byte getLayer(ServerPlayerEntity player)
    {
        if (player.world.getRegistryKey() == World.OVERWORLD)
            return getOverworldLayer(player.getPos().y);
        if (player.world.getRegistryKey() == World.NETHER)
            return 9;
        return 0;
    }

    public static void applyEffects(ServerPlayerEntity player)
    {
        if (player.interactionManager.getGameMode() == GameMode.SPECTATOR
                || player.interactionManager.getGameMode() == GameMode.CREATIVE)
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
