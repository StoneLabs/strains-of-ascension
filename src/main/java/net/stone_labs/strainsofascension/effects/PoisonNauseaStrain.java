package net.stone_labs.strainsofascension.effects;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.stone_labs.strainsofascension.ArtifactState;
import net.stone_labs.strainsofascension.StrainManager;

import java.util.Random;

public class PoisonNauseaStrain implements StrainManager.Strain
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

        double artifactMultiplier = Math.max(1 - 0.1 * artifactState.getAntiPoisonLevel(), 0.4);
        if (random.nextFloat() < StrainManager.effectRandomProbability * artifactMultiplier)
        {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, StrainManager.effectDuration, 0, true, false, StrainManager.showIcon));
            if (doNausea)
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, StrainManager.effectDuration, layer - 7, true, false, StrainManager.showIcon));
        }
    }
}
