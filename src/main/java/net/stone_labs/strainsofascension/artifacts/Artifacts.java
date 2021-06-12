package net.stone_labs.strainsofascension.artifacts;

import java.util.*;

public class Artifacts
{
    private static final Map<Integer, Artifact> REGISTER = new HashMap<>();
    public static Artifact DEPTH_BONUS;
    public static Artifact NV_BONUS;
    public static Artifact POISON_BONUS;
    public static Artifact WITHER_BONUS;
    public static Artifact STRENGTH_OF_DEPTH;
    public static Artifact PORTAL_POWER;

    public static Artifact ByID(Integer id)
    {
        return REGISTER.get(id);
    }

    public static Collection<Artifact> GetCollection()
    {
        return REGISTER.values();
    }

    private static Artifact register(Integer id, String name, int maxValue, boolean needsEqupped)
    {
        Artifact artifact = new Artifact(id, name, maxValue, needsEqupped);
        REGISTER.put(id, artifact);

        return artifact;
    }

    static
    {
        DEPTH_BONUS = register(1, "Depth Bonus", 6, false);
        NV_BONUS = register(2, "Night Vision Bonus", 4, true);
        POISON_BONUS = register(3, "Poison Talisman", 6, false);
        WITHER_BONUS = register(4, "Wither Talisman", 6, false);
        STRENGTH_OF_DEPTH = register(5, "Strength of Depth", 1, true);
        PORTAL_POWER = register(6, "Portal Power", 1, false);
    }
}
