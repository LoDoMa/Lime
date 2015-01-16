package net.lodoma.lime.world.physics;

public class InvalidPhysicsComponentException extends Exception
{
    private static final long serialVersionUID = -2471915132382318064L;

    public InvalidPhysicsComponentException(String msg)
    {
        super(msg);
    }
    
    public InvalidPhysicsComponentException(Throwable cause)
    {
        super(cause);
    }
}
