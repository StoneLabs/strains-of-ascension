package net.stone_labs.strainsofascension.effects.strains;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.stone_labs.strainsofascension.artifacts.ArtifactState;
import net.stone_labs.strainsofascension.StrainManager;
import net.stone_labs.strainsofascension.artifacts.Artifacts;
import net.stone_labs.strainsofascension.effects.BasicEffect;

import java.util.Random;

public class PoisonNauseaStrain extends BasicEffect
{
    public static boolean doPoisonNausea = true;
    public static boolean doNausea = true;

    Random random = new Random();

    @Override
    public void effect(ServerPlayerEntity player, byte layer, ArtifactState artifactState)
    {
        if (!doPoisonNausea)
            return;

        if (layer < 7)
            return;

        // Expected number of jumps till getting poison effect in ascension mode (~5 ticks / jump):
        // jumps = 1/(1 - (1 - 1.0/(10*x + 1))^5) for x elem {0, 6}
        double probabilitySTV =
                StrainManager.strainMode == StrainManager.STRAINMODE.ASCENSION ?
                        1.0 / (10 * artifactState.GetPower(Artifacts.POISON_BONUS) + 1) :
                        StrainManager.effectRandomProbability * frequency * (1 - 0.1 * artifactState.GetPower(Artifacts.POISON_BONUS));

        if (random.nextFloat() < probabilitySTV)
        {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, StrainManager.effectDuration, 0, true, false, StrainManager.showIcon));
            if (doNausea)
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, StrainManager.effectDuration, layer - 7, true, false, StrainManager.showIcon));
        }
    }
}
