package net.stone_labs.strainsofascension.effects;

import net.minecraft.server.network.ServerPlayerEntity;
import net.stone_labs.strainsofascension.StrainManager;
import net.stone_labs.strainsofascension.artifacts.ArtifactState;

public abstract class BasicEffect extends Effect
{
    public BasicEffect()
    {
        super();
    }
    public BasicEffect(int frequency)
    {
        super(frequency);
    }

    @Override
    public void apply(long tick, ServerPlayerEntity player, byte layer, boolean movingUp, ArtifactState artifactState)
    {
        if (StrainManager.strainMode == StrainManager.STRAINMODE.DEPTH)
        {
            if (tick % frequency == 0)
                effect(player, layer, artifactState);
        }
        else if (StrainManager.strainMode == StrainManager.STRAINMODE.ASCENSION)
        {
            if (movingUp)
                effect(player, layer, artifactState);
        }
    }
}
