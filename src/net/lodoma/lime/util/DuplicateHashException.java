package net.lodoma.lime.util;

public class DuplicateHashException extends RuntimeException
{
    private static final long serialVersionUID = -625430202138206465L;
    
    public DuplicateHashException(String msg)
    {
        super(msg);
    }
    
    public DuplicateHashException(Throwable cause)
    {
        super(cause);
    }
}
