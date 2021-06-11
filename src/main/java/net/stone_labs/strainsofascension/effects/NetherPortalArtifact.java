package net.stone_labs.strainsofascension.effects;

import net.minecraft.block.NetherPortalBlock;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import net.stone_labs.strainsofascension.ArtifactState;
import net.stone_labs.strainsofascension.StrainManager;

public class NetherPortalArtifact implements StrainManager.Strain
{
    @Override
    public void effect(ServerPlayerEntity player, byte layer, ArtifactState artifactState)
    {
        if (!player.isCreative() && !player.isSpectator() && player.getPos().y < -48 && player.world.getRegistryKey() == World.OVERWORLD)
            player.resetNetherPortalCooldown();
    }
}
