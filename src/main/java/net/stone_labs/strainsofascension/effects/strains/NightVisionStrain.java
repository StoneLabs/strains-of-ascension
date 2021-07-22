package net.stone_labs.strainsofascension.effects.strains;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.stone_labs.strainsofascension.StrainManager;
import net.stone_labs.strainsofascension.artifacts.ArtifactManager;
import net.stone_labs.strainsofascension.artifacts.ArtifactState;
import net.stone_labs.strainsofascension.artifacts.Artifacts;
import net.stone_labs.strainsofascension.effects.ConstantEffect;
import org.jetbrains.annotations.Nullable;

public class NightVisionStrain extends ConstantEffect
{
    public static boolean allowNVCancelNether = true;

    public NightVisionStrain()
    {
        super(1);
    }

    @Override
    public void effect(ServerPlayerEntity player, byte layer, ArtifactState artifactState)
    {
        if (!BlindnessStrain.doBlindness)
            return;

        if (layer < 5)
            return;

        @Nullable StatusEffectInstance nvEffect = player.getStatusEffect(StatusEffects.NIGHT_VISION);
        if (nvEffect != null)
        {
            int duration = nvEffect.getDuration();
            int newDuration = duration - Math.max(0, 5 - artifactState.GetPower(Artifacts.NV_BONUS));

            player.setStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION,
                    newDuration, 0, true, true), null);

            if (CanCancelBlindness(layer))
                player.removeStatusEffect(StatusEffects.BLINDNESS);
        }
    }

    public static boolean CanCancelBlindness(byte layer)
    {
        return layer == 6 || layer == 5 || (layer == 9 && allowNVCancelNether);
    }

    public static boolean IsNVMilkSafe(byte layer)
    {
        return layer >= 5;
    }

    public static boolean ClearAllExceptNV(ServerPlayerEntity player)
    {
        if (!BlindnessStrain.doBlindness)
            return player.clearStatusEffects();

        boolean ret;
        @Nullable StatusEffectInstance nvEffect = player.getStatusEffect(StatusEffects.NIGHT_VISION);
        ret = player.clearStatusEffects();

        if (IsNVMilkSafe(StrainManager.getLayer(player, ArtifactManager.GetPlayerArtifactState(player.getInventory()))) && nvEffect != null)
            player.addStatusEffect(nvEffect);

        return ret;
    }
}
