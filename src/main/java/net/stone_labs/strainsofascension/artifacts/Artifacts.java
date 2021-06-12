package net.stone_labs.strainsofascension.artifacts;

import java.util.*;

public class Artifacts
{
    private static final Map<String, Artifact> REGISTER = new HashMap<>();
    public static Artifact DEPTH_BONUS;
    public static Artifact NV_BONUS;
    public static Artifact POISON_BONUS;
    public static Artifact WITHER_BONUS;
    public static Artifact STRENGTH_OF_DEPTH;
    public static Artifact PORTAL_POWER;

    public static Artifact ByID(String id)
    {
        return REGISTER.get(id);
    }

    public static Collection<Artifact> GetCollection()
    {
        return REGISTER.values();
    }

    private static Artifact register(String id, String name, int maxValue, boolean needsEqupped)
    {
        Artifact artifact = new Artifact(name, maxValue, needsEqupped);
        REGISTER.put(id, artifact);

        return artifact;
    }

    static
    {
        DEPTH_BONUS = register("depthImmunityBonus", "Depth Bonus", 6, false);
        NV_BONUS = register("nvBonus", "Night Vision Bonus", 4, true);
        POISON_BONUS = register("antiPoison", "Poison Talisman", 6, false);
        WITHER_BONUS = register("antiWither", "Wither Talisman", 6, false);
        STRENGTH_OF_DEPTH = register("strengthOfDepth", "Strength of Depth", 1, true);
        PORTAL_POWER = register("portalPower", "Portal Power", 1, false);
    }
}
