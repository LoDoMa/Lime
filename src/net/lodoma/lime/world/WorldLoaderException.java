package net.lodoma.lime.world;

public class WorldLoaderException extends Exception
{
    private static final long serialVersionUID = -1483737270835121441L;

    public WorldLoaderException(String msg)
    {
        super(msg);
    }
    
    public WorldLoaderException(Throwable cause)
    {
        super(cause);
    }
}
