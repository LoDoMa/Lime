package net.joritan.esc;

public class TagLong extends Tag
{
    private long data;

    public TagLong()
    {
        super();
    }

    public TagLong(String name, long data)
    {
        super(name);
        this.data = data;
    }

    @Override
    public void load(ESCInputStream stream)
    {
        super.load(stream);
        data = stream.readLong();
    }

    @Override
    public void store(ESCOutputStream stream)
    {
        super.store(stream);
        stream.writeLong(data);
    }
}
