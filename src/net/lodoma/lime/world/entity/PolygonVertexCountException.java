package net.lodoma.lime.world.entity;

public class PolygonVertexCountException extends RuntimeException
{
    private static final long serialVersionUID = -6885129326375730032L;

    public PolygonVertexCountException()
    {
        super();
    }
    
    public PolygonVertexCountException(String msg)
    {
        super(msg);
    }
}
