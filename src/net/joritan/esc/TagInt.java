package net.joritan.esc;

public class TagInt extends Tag
{
    private int data;

    public TagInt()
    {
        super();
    }

    public TagInt(String name, int data)
    {
        super(name);
        this.data = data;
    }

    @Override
    public void load(ESCInputStream stream)
    {
        super.load(stream);
        data = stream.readInt();
    }

    @Override
    public void store(ESCOutputStream stream)
    {
        super.store(stream);
        stream.writeInt(data);
    }
}
