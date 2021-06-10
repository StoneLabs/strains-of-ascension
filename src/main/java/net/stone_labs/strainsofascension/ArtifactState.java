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

        switch (stack.getTag().getString("strainArtifact"))
        {
            case DEPTH_IMMUNITY_BONUS_TAG -> {
                depthImmunityBonus += stack.getCount();
                if (depthImmunityBonus > DEPTH_IMMUNITY_BONUS_LIMIT)
                    depthImmunityBonus = DEPTH_IMMUNITY_BONUS_LIMIT;
            }
            case STRENGTH_OF_DEPTH_TAG -> strengthOfDepth = true;
        }
    }

    public int getDepthImmunityBonus()
    {
        return depthImmunityBonus;
    }

    public boolean getStrengthOfDepth()
    {
        return strengthOfDepth;
    }

    public void DebugToPlayer(ServerPlayerEntity player)
    {
        String message = String.format("%s: %d %b",
                player.getEntityName(),
                depthImmunityBonus,
                strengthOfDepth);

        if (player.server.getTicks() % 20 == 0)
            player.sendMessage(new LiteralText(message), false);
    }
}
