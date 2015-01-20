package net.lodoma.lime.world.physics;

public class InvalidPhysicsJointException extends Exception
{
    private static final long serialVersionUID = -6762976932302131856L;

    public InvalidPhysicsJointException(String msg)
    {
        super(msg);
    }
    
    public InvalidPhysicsJointException(Throwable cause)
    {
        super(cause);
    }
}
