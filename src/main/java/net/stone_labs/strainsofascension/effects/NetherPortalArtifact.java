package net.stone_labs.strainsofascension.effects;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import net.stone_labs.strainsofascension.artifacts.ArtifactState;
import net.stone_labs.strainsofascension.StrainManager;
import net.stone_labs.strainsofascension.artifacts.Artifacts;

public class NetherPortalArtifact implements StrainManager.Strain
{
    @Override
    public void effect(ServerPlayerEntity player, byte layer, ArtifactState artifactState)
    {
        if (player.getPos().y >= -48 && artifactState.GetPower(Artifacts.PORTAL_POWER) == 0 && !player.isCreative() && !player.isSpectator() &&player.world.getRegistryKey() == World.OVERWORLD)
            player.resetNetherPortalCooldown();
    }
}
