package net.lodoma.lime.util;

public class HashPoolException extends RuntimeException
{
    private static final long serialVersionUID = -625430202138206465L;
    
    public HashPoolException(String msg)
    {
        super(msg);
    }
    
    public HashPoolException(Throwable cause)
    {
        super(cause);
    }
}
