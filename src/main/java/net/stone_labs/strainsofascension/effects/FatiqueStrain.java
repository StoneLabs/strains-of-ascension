package net.stone_labs.strainsofascension.effects;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.stone_labs.strainsofascension.StrainManager;

public class FatiqueStrain implements StrainManager.Strain
{
    @Override
    public void effect(ServerPlayerEntity player, byte layer)
    {
        if (layer >= 7)
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, StrainManager.effectDuration, 1, true, false, StrainManager.showIcon));
        else if (layer >= 1)
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, StrainManager.effectDuration, 0, true, false, StrainManager.showIcon));
    }
}
