package net.stone_labs.strainsofascension.effects.artifacts;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.stone_labs.strainsofascension.artifacts.ArtifactState;
import net.stone_labs.strainsofascension.StrainManager;
import net.stone_labs.strainsofascension.artifacts.Artifacts;
import net.stone_labs.strainsofascension.effects.ConstantEffect;
import net.stone_labs.strainsofascension.effects.Effect;

public class DepthAgilityArtifact extends ConstantEffect
{
    @Override
    public void effect(ServerPlayerEntity player, byte layer, ArtifactState artifactState)
    {
        if (artifactState.GetPower(Artifacts.DEPTH_AGILITY) > 0 && layer > 0)
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, StrainManager.effectDurationBlindness, 0, true, false, StrainManager.showIcon));
    }
}
