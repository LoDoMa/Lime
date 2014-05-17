package net.joritan.esc;

public class TagFloat extends Tag
{
    private float data;

    public TagFloat()
    {
        super();
    }

    public TagFloat(String name, float data)
    {
        super(name);
        this.data = data;
    }

    @Override
    public void load(ESCInputStream stream)
    {
        super.load(stream);
        data = stream.readFloat();
    }

    @Override
    public void store(ESCOutputStream stream)
    {
        super.store(stream);
        stream.writeFloat(data);
    }
}
