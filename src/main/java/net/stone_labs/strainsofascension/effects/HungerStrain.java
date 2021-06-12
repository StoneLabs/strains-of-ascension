package net.stone_labs.strainsofascension.effects;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.stone_labs.strainsofascension.artifacts.ArtifactState;
import net.stone_labs.strainsofascension.StrainManager;

public class HungerStrain extends Strain
{
    @Override
    public void effect(ServerPlayerEntity player, byte layer, ArtifactState artifactState)
    {
        if (layer >= 9)
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, StrainManager.effectDuration, 6, true, false, StrainManager.showIcon));
        else if (layer >= 8)
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, StrainManager.effectDuration, 5, true, false, StrainManager.showIcon));
        else if (layer >= 7)
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, StrainManager.effectDuration, 4, true, false, StrainManager.showIcon));
        else if (layer >= 6)
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, StrainManager.effectDuration, 3, true, false, StrainManager.showIcon));
        else if (layer >= 5)
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, StrainManager.effectDuration, 2, true, false, StrainManager.showIcon));
        else if (layer >= 4)
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, StrainManager.effectDuration, 1, true, false, StrainManager.showIcon));
        else if (layer >= 3)
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, StrainManager.effectDuration, 0, true, false, StrainManager.showIcon));
    }
}
