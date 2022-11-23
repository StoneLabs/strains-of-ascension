package net.stone_labs.strainsofascension.artifacts;

public enum Artifact
{
    DEPTH_BONUS(1, "Depth Bonus", 6, false),
    NV_BONUS(2, "Night Vision Bonus", 4, true),
    POISON_BONUS(3, "Poison Talisman", 6, false),
    WITHER_BONUS(4, "Wither Talisman", 6, false),
    STRENGTH_OF_DEPTH(5, "Strength of Depth", 1, true),
    PORTAL_POWER(6, "Portal Power", 1, false),
    DEPTH_AGILITY(7, "Depth Agility", 1, false),
    DEPTH_MENDING(8, "Depth Mending", 4, false);

    public final int ID;
    public final String NAME;
    public final int MAX_VALUE;
    public final boolean NEEDS_EQUIPPED;

    private Artifact(Integer id, String name, int maxValue, boolean needsEquipped)
    {
        this.ID = id;
        this.NAME = name;
        this.MAX_VALUE = maxValue;
        this.NEEDS_EQUIPPED = needsEquipped;
    }

    public static Artifact ByID(int id)
    {
        Artifact[] artifacts = values();

        for (Artifact value : artifacts)
            if (value.ID == id)
                return value;

        return null;
    }
}
