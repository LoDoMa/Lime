package net.lodoma.lime.shader;

public class ShaderCompilerError extends Error
{
    private static final long serialVersionUID = -469616682900704052L;
    
    public ShaderCompilerError()
    {
        super();
    }
    
    public ShaderCompilerError(String msg)
    {
        super(msg);
    }
}
