package net.lodoma.lime.util;

public class Pair<T1, T2>
{
    public final T1 first;
    public final T2 second;
    
    public Pair(T1 first, T2 second)
    {
        this.first = first;
        this.second = second;
    }
    
    @Override
    public Pair<T1, T2> clone()
    {
        return new Pair<T1, T2>(first, second);
    }
}
