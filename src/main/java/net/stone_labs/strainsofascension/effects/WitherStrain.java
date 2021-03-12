package net.stone_labs.strainsofascension.effects;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.stone_labs.strainsofascension.StrainManager;

import java.util.Random;

public class WitherStrain implements StrainManager.Strain
{
    Random random = new Random();

    @Override
    public void effect(ServerPlayerEntity player, byte layer)
    {
        if (layer < 8)
            return;

        if (random.nextFloat() < StrainManager.effectRandomProbability)
        {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, StrainManager.effectDuration, layer - 7, true, false, StrainManager.showIcon));
        }
    }
}
