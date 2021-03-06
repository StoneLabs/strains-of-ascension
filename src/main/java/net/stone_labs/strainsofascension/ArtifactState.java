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

    public static final String NV_BONUS_TAG = "nvBonus";
    private int nvBonus = 0;

    public static final String ANTI_POISON_TAG = "antiPoison";
    private int antiPoisonLevel = 0;

    public static final String ANTI_WITHER_TAG = "antiWither";
    private int antiWitherLevel = 0;

    public void consider(ItemStack stack, boolean isEquip)
    {
        if (!stack.hasTag())
            return;

        //noinspection ConstantConditions
        if (!stack.getTag().contains("strainArtifact", NbtElement.STRING_TYPE))
            return;

        int artifactPower = stack.getTag().getInt("strainArtifactPower");

        switch (stack.getTag().getString("strainArtifact"))
        {
            case DEPTH_IMMUNITY_BONUS_TAG -> depthImmunityBonus = Math.max(artifactPower, depthImmunityBonus);
            case NV_BONUS_TAG -> nvBonus = isEquip ? Math.max(artifactPower, nvBonus) : nvBonus;
            case STRENGTH_OF_DEPTH_TAG -> strengthOfDepth = isEquip || strengthOfDepth;
            case ANTI_POISON_TAG -> antiPoisonLevel = Math.max(artifactPower, antiPoisonLevel);
            case ANTI_WITHER_TAG -> antiWitherLevel = Math.max(artifactPower, antiWitherLevel);
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

    public int getAntiPoisonLevel()
    {
        return antiPoisonLevel;
    }

    public int getAntiWitherLevel()
    {
        return antiWitherLevel;
    }

    public int getNVBonus()
    {
        return nvBonus;
    }

    public void DebugToPlayer(ServerPlayerEntity player)
    {
        String message = String.format("%s: %f %b %d %d %d",
                player.getEntityName(),
                depthImmunityBonus,
                strengthOfDepth,
                nvBonus,
                antiPoisonLevel,
                antiWitherLevel);

        if (player.server.getTicks() % 20 == 0)
            player.sendMessage(new LiteralText(message), false);
    }
}
