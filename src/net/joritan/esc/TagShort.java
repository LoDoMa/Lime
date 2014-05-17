package net.joritan.esc;

public class TagShort extends Tag
{
    private short data;

    public TagShort()
    {
        super();
    }

    public TagShort(String name, short data)
    {
        super(name);
        this.data = data;
    }

    @Override
    public void load(ESCInputStream stream)
    {
        super.load(stream);
        data = stream.readShort();
    }

    @Override
    public void store(ESCOutputStream stream)
    {
        super.store(stream);
        stream.writeShort(data);
    }
}
