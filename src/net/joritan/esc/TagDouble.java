package net.joritan.esc;

public class TagDouble extends Tag
{
    private double data;

    public TagDouble()
    {
        super();
    }

    public TagDouble(String name, double data)
    {
        super(name);
        this.data = data;
    }

    @Override
    public void load(ESCInputStream stream)
    {
        super.load(stream);
        data = stream.readDouble();
    }

    @Override
    public void store(ESCOutputStream stream)
    {
        super.store(stream);
        stream.writeDouble(data);
    }
}
