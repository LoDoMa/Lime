package net.lodoma.lime.world;

public class LockedChunkModificationException extends RuntimeException
{
    private static final long serialVersionUID = -6504138406989606080L;
    
    public LockedChunkModificationException()
    {
        super();
    }
    
    public LockedChunkModificationException(String msg)
    {
        super(msg);
    }
}
