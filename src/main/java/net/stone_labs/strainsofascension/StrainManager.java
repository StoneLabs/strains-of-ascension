package net.stone_labs.strainsofascension;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import net.stone_labs.strainsofascension.artifacts.Artifact;
import net.stone_labs.strainsofascension.artifacts.ArtifactState;
import net.stone_labs.strainsofascension.effects.*;
import net.stone_labs.strainsofascension.effects.artifacts.DepthAgilityArtifact;
import net.stone_labs.strainsofascension.effects.artifacts.DepthMendingArtifact;
import net.stone_labs.strainsofascension.effects.artifacts.NetherPortalArtifact;
import net.stone_labs.strainsofascension.effects.artifacts.StrengthOfDepthArtifact;
import net.stone_labs.strainsofascension.effects.strains.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public enum STRAINMODE
    {
        ASCENSION,
        DEPTH
    }
    public static STRAINMODE strainMode = STRAINMODE.ASCENSION;

    public static final List<Effect> EFFECTS = new ArrayList<>();

    public final static int effectDuration = 120 * 20 + 19;
    public final static int effectDurationBlindness = 3 * 20 + 19;
    public final static float effectRandomProbability = 1.0f / effectDuration;
    public static double localDifficultyEffectMultiplier;
    public static double lunarDifficultyEffectMultiplier;
    public static boolean showIcon = false;
    public static boolean doNether = true;
    public static boolean doCreative = false;
    public static boolean doSpectator = false;

    public static boolean debugHeight;


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

    public static double getEffectPlayerHeight(ServerPlayerEntity player, ArtifactState artifactState)
    {
        double localDifficultyImpact = localDifficultyEffectMultiplier * (player.world.getLocalDifficulty(player.getBlockPos()).getLocalDifficulty() / 6.75);
        double moonPhaseImpact = lunarDifficultyEffectMultiplier * (Math.abs((Math.abs((player.world.getLunarTime() - (24000*4.75)) / 24000.0) % 8L) / 2 - 2) - 1);
        double artifactHeightImpact = -artifactState.GetPower(Artifact.DEPTH_BONUS);
        double effectivePlayerHeight = player.getPos().y - localDifficultyImpact - moonPhaseImpact - artifactHeightImpact;


        if (debugHeight && player.server.getTicks() % 20 == 0)
        {
            String message = String.format("%s: %.1f - (%.1f) - (%.1f) - (%.1f) = %.1f",
                    player.getEntityName(),
                    player.getPos().y,
                    localDifficultyImpact,
                    moonPhaseImpact,
                    artifactHeightImpact,
                    effectivePlayerHeight);
            player.sendMessage(new LiteralText(message), true);
        }

        return effectivePlayerHeight;
    }

    public static byte getLayer(ServerPlayerEntity player, ArtifactState artifactState)
    {
        if (player.world.getRegistryKey() == World.OVERWORLD)
            return getOverworldLayer(getEffectPlayerHeight(player, artifactState));
        if (player.world.getRegistryKey() == World.NETHER)
            return doNether ? (byte)9 : (byte)0;
        return 0;
    }

    public static Map<ServerPlayerEntity, Double> lastKnownPlayerHeight = new HashMap<>();
    public static boolean hasMovedUp(ServerPlayerEntity player)
    {
        double height = player.getPos().y;
        if (player.world.getRegistryKey() == World.NETHER)
            height -= 1000;
        else if (player.world.getRegistryKey() == World.END)
            height += 1000;

        boolean moved = lastKnownPlayerHeight.getOrDefault(player, Double.MAX_VALUE) < height;
        lastKnownPlayerHeight.put(player, height);
        return moved;
    }
    public static void applyEffects(ServerPlayerEntity player, ArtifactState artifactState)
    {
        if (player.interactionManager.getGameMode() == GameMode.SPECTATOR && !doSpectator)
            return;

        if (player.interactionManager.getGameMode() == GameMode.CREATIVE && !doCreative)
            return;

        byte layer = getLayer(player, artifactState);
        boolean hasMovedUp = strainMode != STRAINMODE.DEPTH && hasMovedUp(player);

        for (Effect effect : EFFECTS)
            effect.apply(player.server.getTicks(), player, layer, hasMovedUp, artifactState);
    }

    static
    {
        EFFECTS.add(new FatigueStrain());
        EFFECTS.add(new WeaknessStrain());
        EFFECTS.add(new HungerStrain());
        EFFECTS.add(new SlownessStrain());
        EFFECTS.add(new BlindnessStrain());
        EFFECTS.add(new NightVisionStrain());
        EFFECTS.add(new PoisonNauseaStrain());
        EFFECTS.add(new WitherStrain());
        EFFECTS.add(new InsanityStrain());

        EFFECTS.add(new StrengthOfDepthArtifact());
        EFFECTS.add(new NetherPortalArtifact());
        EFFECTS.add(new DepthAgilityArtifact());
        EFFECTS.add(new DepthMendingArtifact());
    }
}
