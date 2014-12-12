package net.lodoma.lime.physics;

public class EntityFactoryException extends Exception
{
    private static final long serialVersionUID = 4466118044761390601L;
    
    public EntityFactoryException(String msg)
    {
        super(msg);
    }
    
    public EntityFactoryException(Throwable cause)
    {
        super(cause);
    }
}
