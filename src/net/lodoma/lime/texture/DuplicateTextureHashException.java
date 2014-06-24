package net.lodoma.lime.texture;

public class DuplicateTextureHashException extends RuntimeException
{
    private static final long serialVersionUID = 1L;
    
    public DuplicateTextureHashException()
    {
        super();
    }
    
    public DuplicateTextureHashException(String msg)
    {
        super(msg);
    }
}
