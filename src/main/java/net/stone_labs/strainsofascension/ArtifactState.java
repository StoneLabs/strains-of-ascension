package net.stone_labs.strainsofascension;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

@SuppressWarnings("FieldCanBeLocal")
public class ArtifactState
{
    public static final String DEPTH_IMMUNITY_BONUS_TAG = "depthImmunityBonus";
    private int depthImmunityBonus = 0;

    public static final String STRENGTH_OF_DEPTH_TAG = "strengthOfDepth";
    private boolean strengthOfDepth = false;

    public static final String NV_BONUS_TAG = "nvBonus";
    private int nvBonus = 0;

    public static final String ANTI_POISON_TAG = "antiPoison";
    private int antiPoisonLevel = 0;

    public static final String ANTI_WITHER_TAG = "antiWither";
    private int antiWitherLevel = 0;

    public static final String PORTAL_POWER_TAG = "portalPower";
    private boolean portalPower;

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
            case PORTAL_POWER_TAG -> portalPower = true;
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

    public int getAntiPoisonLevel()
    {
        return antiPoisonLevel;
    }

    public int getAntiWitherLevel()
    {
        return antiWitherLevel;
    }

    public boolean getPortalPower()
    {
        return portalPower;
    }

    public int getNVBonus()
    {
        return nvBonus;
    }

    public void Debug(ServerPlayerEntity player, boolean showToAll)
    {
        final String OFF_COLOR = "§f";
        final String MID_COLOR = "§d";
        final String MAX_COLOR = "§6";

        String message = String.format("""
                        §4%s:
                        §4┃§f %s%d§f - Depth Immunity
                        §4┃§f %s§f - Strength
                        §4┃§f %s%d§f - NVBonus
                        §4┃§f %s%d§f - Anti Poison
                        §4┃§f %s%d§f - Anti Wither
                        §4┖§f %s§f - Portal Power""",
                player.getEntityName(),
                depthImmunityBonus > 0 ? (depthImmunityBonus == 6 ? MAX_COLOR : MID_COLOR) : OFF_COLOR,
                depthImmunityBonus,
                strengthOfDepth ? "§61" : "§f0",
                nvBonus > 0 ? (nvBonus == 6 ? MAX_COLOR : MID_COLOR) : OFF_COLOR,
                nvBonus,
                antiPoisonLevel > 0 ? (antiPoisonLevel == 6 ? MAX_COLOR : MID_COLOR) : OFF_COLOR,
                antiPoisonLevel,
                antiWitherLevel > 0 ? (antiWitherLevel == 6 ? MAX_COLOR : MID_COLOR) : OFF_COLOR,
                antiWitherLevel,
                portalPower ? "§61" : "§f0");

        if (showToAll)
            for (ServerPlayerEntity serverPlayer : player.server.getPlayerManager().getPlayerList())
                serverPlayer.sendMessage(new LiteralText(message), false);
        else
            player.sendMessage(new LiteralText(message), false);
    }
}
