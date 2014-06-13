package net.lodoma.lime.world;

public class LockedPaletteModificationException extends RuntimeException
{
    private static final long serialVersionUID = 2927446192769779394L;

    public LockedPaletteModificationException()
    {
        super();
    }
    
    public LockedPaletteModificationException(String msg)
    {
        super(msg);
    }
}
