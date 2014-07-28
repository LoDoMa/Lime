package net.lodoma.lime.physics.entity;

public class EntityLoaderException extends Exception
{
    private static final long serialVersionUID = 233423363579524061L;
    
    public EntityLoaderException(String msg)
    {
        super(msg);
    }
    
    public EntityLoaderException(Throwable cause)
    {
        super(cause);
    }
}
