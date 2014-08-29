package net.lodoma.lime.physics.entity;

public class HitboxLoaderException extends Exception
{
    private static final long serialVersionUID = -8290756328283579160L;
    
    public HitboxLoaderException(String msg)
    {
        super(msg);
    }
    
    public HitboxLoaderException(Throwable cause)
    {
        super(cause);
    }
}
