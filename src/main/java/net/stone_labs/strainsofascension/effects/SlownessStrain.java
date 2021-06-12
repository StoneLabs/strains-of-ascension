package net.stone_labs.strainsofascension.effects;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.stone_labs.strainsofascension.artifacts.ArtifactState;
import net.stone_labs.strainsofascension.StrainManager;

public class SlownessStrain extends Strain
{
    @Override
    public void effect(ServerPlayerEntity player, byte layer, ArtifactState artifactState)
    {
        if (layer >= 7)
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, StrainManager.effectDuration, 2, true, false, StrainManager.showIcon));
        else if (layer >= 6)
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, StrainManager.effectDuration, 1, true, false, StrainManager.showIcon));
        else if (layer >= 4)
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, StrainManager.effectDuration, 0, true, false, StrainManager.showIcon));
    }
}
