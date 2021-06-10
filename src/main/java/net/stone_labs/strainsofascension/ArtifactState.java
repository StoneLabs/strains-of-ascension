package net.stone_labs.strainsofascension;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

import java.util.List;

@SuppressWarnings("FieldCanBeLocal")
public class ArtifactState
{
    public static final String DEPTH_IMMUNITY_BONUS_TAG = "depthImmunityBonus";
    private float depthImmunityBonus = 0;

    public static final String STRENGTH_OF_DEPTH_TAG = "strengthOfDepth";
    private boolean strengthOfDepth = false;

    public void consider(List<ItemStack> stacks)
    {
        for (ItemStack stack : stacks)
            consider(stack);
    }

    public void consider(ItemStack stack)
    {
        if (!stack.hasTag())
            return;

        //noinspection ConstantConditions
        if (!stack.getTag().contains("strainArtifact", NbtElement.STRING_TYPE))
            return;

        float artifactPower = stack.getTag().getFloat("strainArtifactPower");

        switch (stack.getTag().getString("strainArtifact"))
        {
            case DEPTH_IMMUNITY_BONUS_TAG -> depthImmunityBonus = Math.max(artifactPower, depthImmunityBonus);
            case STRENGTH_OF_DEPTH_TAG -> strengthOfDepth = true;
        }
    }

    public float getDepthImmunityBonus()
    {
        return depthImmunityBonus;
    }

    public boolean getStrengthOfDepth()
    {
        return strengthOfDepth;
    }

    public void DebugToPlayer(ServerPlayerEntity player)
    {
        String message = String.format("%s: %f %b",
                player.getEntityName(),
                depthImmunityBonus,
                strengthOfDepth);

        if (player.server.getTicks() % 20 == 0)
            player.sendMessage(new LiteralText(message), false);
    }
}
