package net.lodoma.lime.util;

public class IdentityException extends RuntimeException
{
    private static final long serialVersionUID = 2666681511939808634L;
    
    public IdentityException(String message)
    {
        super(message);
    }
    
    public IdentityException(Throwable cause)
    {
        super(cause);
    }
    
    public IdentityException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
