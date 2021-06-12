package net.stone_labs.strainsofascension.effects;

import net.minecraft.server.network.ServerPlayerEntity;
import net.stone_labs.strainsofascension.artifacts.ArtifactState;

public abstract class Strain
{
    public int frequency = 10;

    public Strain()
    {

    }
    public Strain(int frequency)
    {
        this.frequency = frequency;
    }

    public abstract void effect(ServerPlayerEntity player, byte layer, ArtifactState artifactState);
}
