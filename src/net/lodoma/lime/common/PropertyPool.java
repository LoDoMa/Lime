package net.lodoma.lime.common;

public interface PropertyPool
{
    public Object getProperty(String name);
    public void setProperty(String name, Object value);
    public void removeProperty(String name);
    public boolean hasProperty(String name);
}
