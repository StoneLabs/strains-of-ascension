package net.stone_labs.strainsofascension.effects.strains;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.stone_labs.strainsofascension.artifacts.ArtifactState;
import net.stone_labs.strainsofascension.StrainManager;
import net.stone_labs.strainsofascension.effects.BasicEffect;

public class BlindnessStrain extends BasicEffect
{
    public static boolean doBlindness = true;

    @Override
    public void effect(ServerPlayerEntity player, byte layer, ArtifactState artifactState)
    {
        if (layer < 5)
            return;

        if (!NightVisionStrain.CanCancelBlindness(layer) || player.getStatusEffect(StatusEffects.NIGHT_VISION) == null)
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, StrainManager.effectDurationBlindness, 0, true, false, StrainManager.showIcon));
    }
}
