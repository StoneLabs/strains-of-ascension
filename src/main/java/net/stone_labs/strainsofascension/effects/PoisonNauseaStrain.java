package net.stone_labs.strainsofascension.effects;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.stone_labs.strainsofascension.StrainManager;

import java.util.Random;

public class PoisonNauseaStrain implements StrainManager.Strain
{
    public static boolean doPoisonNausea = true;
    public static boolean doNausea = true;

    Random random = new Random();

    @Override
    public void effect(ServerPlayerEntity player, byte layer)
    {
        if (!doPoisonNausea)
            return;

        if (layer < 7)
            return;

        if (random.nextFloat() < StrainManager.effectRandomProbability)
        {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, StrainManager.effectDuration, 0, true, false, StrainManager.showIcon));
            if (doNausea)
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, StrainManager.effectDuration, layer - 7, true, false, StrainManager.showIcon));
        }
    }
}
