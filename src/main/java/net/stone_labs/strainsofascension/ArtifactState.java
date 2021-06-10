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
    public static final int DEPTH_IMMUNITY_BONUS_LIMIT = 3;
    private int depthImmunityBonus = 0;

    public void setDepthImmunityBonus(int value)
    {
        depthImmunityBonus += value;

        if (depthImmunityBonus > DEPTH_IMMUNITY_BONUS_LIMIT)
            depthImmunityBonus = DEPTH_IMMUNITY_BONUS_LIMIT;
    }
    public int getDepthImmunityBonus()
    {
        return depthImmunityBonus;
    }

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

        switch (stack.getTag().getString("strainArtifact"))
        {
            case DEPTH_IMMUNITY_BONUS_TAG:
                setDepthImmunityBonus(stack.getCount());
                break;
        }
    }

    public void DebugToPlayer(ServerPlayerEntity player)
    {
        String message = String.format("%s: %d",
                player.getEntityName(),
                depthImmunityBonus);
        player.sendMessage(new LiteralText(message), false);
    }
}
