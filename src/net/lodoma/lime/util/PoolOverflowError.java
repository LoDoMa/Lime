package net.lodoma.lime.util;

public class PoolOverflowError extends Error
{
    private static final long serialVersionUID = 1147810643993209781L;
    
    public PoolOverflowError(String msg)
    {
        super(msg);
    }
}
