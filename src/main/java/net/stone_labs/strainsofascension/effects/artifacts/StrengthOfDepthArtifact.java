package net.stone_labs.strainsofascension.effects.artifacts;

import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.stone_labs.strainsofascension.artifacts.Artifact;
import net.stone_labs.strainsofascension.artifacts.ArtifactState;
import net.stone_labs.strainsofascension.effects.ConstantEffect;

public class StrengthOfDepthArtifact extends ConstantEffect
{
    @Override
    public void effect(ServerPlayerEntity player, byte layer, ArtifactState artifactState)
    {
        int targetMaxHealth = 20 + artifactState.GetPower(Artifact.STRENGTH_OF_DEPTH) * 2 * layer;
        EntityAttributeInstance maxHealth = player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);

        assert maxHealth != null;
        if (maxHealth.getBaseValue() != targetMaxHealth)
            maxHealth.setBaseValue(targetMaxHealth);
    }
}
