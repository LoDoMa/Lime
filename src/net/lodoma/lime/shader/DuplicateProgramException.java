package net.lodoma.lime.shader;

public class DuplicateProgramException extends RuntimeException
{
    private static final long serialVersionUID = 4462479480726841895L;
    
    public DuplicateProgramException()
    {
        super();
    }
    
    public DuplicateProgramException(String msg)
    {
        super(msg);
    }
}
