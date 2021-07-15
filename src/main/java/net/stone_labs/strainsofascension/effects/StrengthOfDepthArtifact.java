package net.stone_labs.strainsofascension.effects;

import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.stone_labs.strainsofascension.artifacts.ArtifactState;
import net.stone_labs.strainsofascension.artifacts.Artifacts;

public class StrengthOfDepthArtifact extends Strain
{
    @Override
    public void effect(ServerPlayerEntity player, byte layer, ArtifactState artifactState)
    {
        int targetMaxHealth = 20 + artifactState.GetPower(Artifacts.STRENGTH_OF_DEPTH) * 2 * layer;
        EntityAttributeInstance maxHealth = player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);

        assert maxHealth != null;
        if (maxHealth.getBaseValue() != targetMaxHealth)
            maxHealth.setBaseValue(targetMaxHealth);
    }
}
