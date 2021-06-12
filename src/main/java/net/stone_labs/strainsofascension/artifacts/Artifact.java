package net.stone_labs.strainsofascension.artifacts;

public class Artifact
{
    public final int ID;
    public final String NAME;
    public final int MAX_VALUE;
    public final boolean NEEDS_EQUIPPED;

    protected Artifact(Integer ID, String NAME, int MAX_VALUE, boolean NEEDS_EQUIPPED)
    {
        this.ID = ID;
        this.NAME = NAME;
        this.MAX_VALUE = MAX_VALUE;
        this.NEEDS_EQUIPPED = NEEDS_EQUIPPED;
    }
}
