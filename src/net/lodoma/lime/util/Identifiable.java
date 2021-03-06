package net.lodoma.lime.util;

public interface Identifiable<I>
{
    public I getIdentifier();
    public void setIdentifier(I identifier);
}
