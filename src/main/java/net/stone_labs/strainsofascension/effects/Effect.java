package net.stone_labs.strainsofascension.effects;

import net.minecraft.server.network.ServerPlayerEntity;
import net.stone_labs.strainsofascension.StrainManager;
import net.stone_labs.strainsofascension.artifacts.ArtifactState;

public abstract class Effect
{
    public final int frequency;

    public Effect()
    {
        this.frequency = 10;
    }
    public Effect(int frequency)
    {
        this.frequency = frequency;
    }

    public abstract void effect(ServerPlayerEntity player, byte layer, ArtifactState artifactState);
    public abstract void apply(long tick, ServerPlayerEntity player, byte layer, boolean movingUp, ArtifactState artifactState);
}
