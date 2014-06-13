package net.lodoma.lime.shader;

public class DuplicateShaderException extends RuntimeException
{
    private static final long serialVersionUID = 7471394110164612364L;

    public DuplicateShaderException()
    {
        super();
    }
    
    public DuplicateShaderException(String msg)
    {
        super(msg);
    }
}
