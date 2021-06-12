package net.stone_labs.strainsofascension.effects;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.stone_labs.strainsofascension.artifacts.ArtifactState;
import net.stone_labs.strainsofascension.StrainManager;
import net.stone_labs.strainsofascension.artifacts.Artifacts;

import java.util.Random;

public class WitherStrain implements StrainManager.Strain
{
    public static boolean doWither = true;

    Random random = new Random();

    @Override
    public void effect(ServerPlayerEntity player, byte layer, ArtifactState artifactState)
    {
        if (!doWither)
            return;

        if (layer < 8)
            return;

        double artifactMultiplier = Math.max(1 - 0.1 * artifactState.GetPower(Artifacts.WITHER_BONUS), 0.4);
        if (random.nextFloat() < StrainManager.effectRandomProbability * artifactMultiplier)
        {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, StrainManager.effectDuration, layer - 7, true, false, StrainManager.showIcon));
        }
    }
}
