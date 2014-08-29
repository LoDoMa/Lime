package net.lodoma.lime.physics.entity;

public class ModelLoaderException extends Exception
{
    private static final long serialVersionUID = 700544159133095067L;
    
    public ModelLoaderException(String msg)
    {
        super(msg);
    }
    
    public ModelLoaderException(Throwable cause)
    {
        super(cause);
    }
}
