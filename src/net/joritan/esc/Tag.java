package net.joritan.esc;

public abstract class Tag
{
    private String name;

    public Tag()
    {
    }

    public Tag(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void load(ESCInputStream stream)
    {
        name = stream.readString();
    }

    public void store(ESCOutputStream stream)
    {
        stream.writeString(name);
    }
}
