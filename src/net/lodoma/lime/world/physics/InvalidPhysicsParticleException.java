package net.lodoma.lime.world.physics;

public class InvalidPhysicsParticleException extends Exception
{
    private static final long serialVersionUID = 6660954967912552390L;
    
    public InvalidPhysicsParticleException(String msg)
    {
        super(msg);
    }
    
    public InvalidPhysicsParticleException(Throwable cause)
    {
        super(cause);
    }
}
