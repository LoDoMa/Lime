package net.lodoma.lime.shader;

public class InvalidUniformNameException extends RuntimeException
{
    private static final long serialVersionUID = -5955094610987329672L;
    
    public InvalidUniformNameException()
    {
        super();
    }
    
    public InvalidUniformNameException(String msg)
    {
        super(msg);
    }
}
