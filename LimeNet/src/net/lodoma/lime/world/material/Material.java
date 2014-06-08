package net.lodoma.lime.world.material;

public abstract class Material
{
    private boolean solid;
    
    public final boolean isSolid()
    {
        return solid;
    }
    
    protected final void setSolid(boolean solid)
    {
        this.solid = solid;
    }
}
