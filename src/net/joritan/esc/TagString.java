package net.joritan.esc;

public class TagString extends Tag
{
    private String data;

    public TagString()
    {
        super();
    }

    public TagString(String name, String data)
    {
        super(name);
        this.data = data;
    }

    @Override
    public void load(ESCInputStream stream)
    {
        data = stream.readString();
    }

    @Override
    public void store(ESCOutputStream stream)
    {
        stream.writeString(data);
    }
}
