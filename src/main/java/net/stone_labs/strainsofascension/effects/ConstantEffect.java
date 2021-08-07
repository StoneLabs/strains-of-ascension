package net.stone_labs.strainsofascension.effects;

import net.minecraft.server.network.ServerPlayerEntity;
import net.stone_labs.strainsofascension.StrainManager;
import net.stone_labs.strainsofascension.artifacts.ArtifactState;

public abstract class ConstantEffect extends Effect
{
    public ConstantEffect()
    {
        super();
    }
    public ConstantEffect(int frequency)
    {
        super(frequency);
    }

    @Override
    public void apply(long tick, ServerPlayerEntity player, byte layer, boolean movingUp, ArtifactState artifactState)
    {
        if (tick % frequency == 0)
            effect(player, layer, artifactState);
    }
}
