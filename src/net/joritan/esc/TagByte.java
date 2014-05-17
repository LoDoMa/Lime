package net.joritan.esc;

public class TagByte extends Tag
{
    private byte data;

    public TagByte()
    {
        super();
    }

    public TagByte(String name, byte data)
    {
        super(name);
        this.data = data;
    }

    @Override
    public void load(ESCInputStream stream)
    {
        super.load(stream);
        data = stream.readByte();
    }

    @Override
    public void store(ESCOutputStream stream)
    {
        super.store(stream);
        stream.writeByte(data);
    }
}
