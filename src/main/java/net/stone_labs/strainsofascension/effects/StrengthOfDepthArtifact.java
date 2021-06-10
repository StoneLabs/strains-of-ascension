package net.stone_labs.strainsofascension.effects;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.stone_labs.strainsofascension.ArtifactState;
import net.stone_labs.strainsofascension.StrainManager;

public class StrengthOfDepthArtifact implements StrainManager.Strain
{
    @Override
    public void effect(ServerPlayerEntity player, byte layer, ArtifactState artifactState)
    {
        int targetMaxHealth = 20 + (artifactState.getStrengthOfDepth() ? 2 * layer : 0);
        EntityAttributeInstance maxHealth = player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);

        assert maxHealth != null;
        if (maxHealth.getBaseValue() != targetMaxHealth)
            maxHealth.setBaseValue(targetMaxHealth);
    }
}
