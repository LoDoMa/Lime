package net.lodoma.lime.world.physics;

public class InvalidPhysicsShapeException extends Exception
{
    private static final long serialVersionUID = 7617264009606992521L;

    public InvalidPhysicsShapeException(String msg)
    {
        super(msg);
    }
    
    public InvalidPhysicsShapeException(Throwable cause)
    {
        super(cause);
    }
}
