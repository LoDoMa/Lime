package net.lodoma.lime.physics;

public class EntityLoaderException extends Exception
{
    private static final long serialVersionUID = 4466118044761390601L;
    
    public EntityLoaderException(String msg)
    {
        super(msg);
    }
    
    public EntityLoaderException(Throwable cause)
    {
        super(cause);
    }
}
