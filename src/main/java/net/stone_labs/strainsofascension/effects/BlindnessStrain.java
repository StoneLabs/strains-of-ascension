package net.stone_labs.strainsofascension.effects;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.stone_labs.strainsofascension.ArtifactManager;
import net.stone_labs.strainsofascension.ArtifactState;
import net.stone_labs.strainsofascension.StrainManager;
import org.jetbrains.annotations.Nullable;

public class BlindnessStrain implements StrainManager.Strain
{
    public static boolean doBlindness = true;
    public static boolean allowNVCancelNether = true;

    @Override
    public void effect(ServerPlayerEntity player, byte layer, ArtifactState artifactState)
    {
        if (!doBlindness)
            return;

        if (layer < 5)
            return;

        @Nullable StatusEffectInstance nvEffect = player.getStatusEffect(StatusEffects.NIGHT_VISION);
        if (nvEffect != null)
        {
            int duration = nvEffect.getDuration();
            int newDuration = duration - Math.max(0, 5 - artifactState.getNVBonus());

            player.removeStatusEffect(StatusEffects.NIGHT_VISION);
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION,
                    newDuration, 0, true, true));

            if (isCancelable(layer))
            {
                player.removeStatusEffect(StatusEffects.BLINDNESS);
                return;
            }
        }
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, StrainManager.effectDurationBlindness, 0, true, false, StrainManager.showIcon));
    }

    public static boolean isCancelable(byte layer)
    {
        return layer == 6 || layer == 5 || (layer == 9 && allowNVCancelNether);
    }

    public static boolean isNVMilkSafe(byte layer)
    {
        return layer >= 5;
    }

    public static boolean clearAllExceptNVCancel(ServerPlayerEntity player)
    {
        if (!doBlindness)
            return player.clearStatusEffects();

        boolean ret;
        @Nullable StatusEffectInstance nvEffect = player.getStatusEffect(StatusEffects.NIGHT_VISION);
        ret = player.clearStatusEffects();

        if (isNVMilkSafe(StrainManager.getLayer(player, ArtifactManager.GetPlayerArtifactState(player.getInventory()))) && nvEffect != null)
            player.addStatusEffect(nvEffect);

        return ret;
    }
}
