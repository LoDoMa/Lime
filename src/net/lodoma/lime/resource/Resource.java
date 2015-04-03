package net.lodoma.lime.resource;

public abstract class Resource
{
    public String name;
    public ResourceType type;
    public int referenceCount;
    
    public abstract void update(double timeDelta);
    
    public abstract void create();
    public abstract void destroy();
    
    public abstract Object get();
}
