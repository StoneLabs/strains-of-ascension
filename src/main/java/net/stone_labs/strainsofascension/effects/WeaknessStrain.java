package net.stone_labs.strainsofascension.effects;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.stone_labs.strainsofascension.artifacts.ArtifactState;
import net.stone_labs.strainsofascension.StrainManager;

public class WeaknessStrain extends Strain
{
    @Override
    public void effect(ServerPlayerEntity player, byte layer, ArtifactState artifactState)
    {
        if (layer >= 5)
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, StrainManager.effectDuration, 1, true, false, StrainManager.showIcon));
        else if (layer >= 2)
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, StrainManager.effectDuration, 0, true, false, StrainManager.showIcon));
    }
}
