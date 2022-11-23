package net.stone_labs.strainsofascension.artifacts;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

import java.util.HashMap;
import java.util.Map;

public class ArtifactState
{
    private final Map<Artifact, Integer> artifacts = new HashMap<>();

    private void setArtifact(Artifact artifact, int power)
    {
        int oldPower = artifacts.getOrDefault(artifact, 0);
        artifacts.put(artifact, Math.max(oldPower, power));
    }

    public int GetPower(Artifact artifact)
    {
        return Math.min(artifact.MAX_VALUE, artifacts.getOrDefault(artifact, 0));
    }

    @SuppressWarnings("ConstantConditions")
    public void consider(ItemStack stack, boolean isEquip)
    {
        if (!stack.hasNbt())
            return;

        NbtCompound tag = stack.getNbt();

        Artifact artifact = Artifact.ByID(tag.getInt("artifact"));
        if (artifact == null)
            return;

        int artifactPower = tag.getInt("artifactPower");

        if (!artifact.NEEDS_EQUIPPED || isEquip)
            setArtifact(artifact, artifactPower);
    }

    public void Debug(ServerPlayerEntity player, boolean showToAll)
    {
        final String OFF_COLOR = "§f";
        final String MID_COLOR = "§d";
        final String MAX_COLOR = "§6";

        StringBuilder message = new StringBuilder(String.format("§4%s:", player.getEntityName()));
        for (Artifact artifact : Artifact.values())
        {
            int artifactPower = this.GetPower(artifact);

            message.append(String.format("\n§4┃§f %s%d§f - %s",
                    artifactPower > 0 ? (artifactPower == artifact.MAX_VALUE ? MAX_COLOR : MID_COLOR) : OFF_COLOR,
                    artifactPower,
                    artifact.NAME));
        }

        if (showToAll)
            for (ServerPlayerEntity serverPlayer : player.server.getPlayerManager().getPlayerList())
                serverPlayer.sendMessage(Text.literal(message.toString()), false);
        else
            player.sendMessage(Text.literal(message.toString()), false);
    }
}
