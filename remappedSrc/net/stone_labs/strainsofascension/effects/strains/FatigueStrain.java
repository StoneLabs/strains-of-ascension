package net.stone_labs.strainsofascension.effects.strains;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.stone_labs.strainsofascension.artifacts.ArtifactState;
import net.stone_labs.strainsofascension.StrainManager;
import net.stone_labs.strainsofascension.effects.BasicEffect;
import net.stone_labs.strainsofascension.effects.ConstantEffect;
import net.stone_labs.strainsofascension.effects.Effect;

public class FatigueStrain extends BasicEffect
{
    @Override
    public void effect(ServerPlayerEntity player, byte layer, ArtifactState artifactState)
    {
        if (layer >= 7)
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, StrainManager.effectDuration, 1, true, false, StrainManager.showIcon));
        else if (layer >= 1)
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, StrainManager.effectDuration, 0, true, false, StrainManager.showIcon));
    }
}
