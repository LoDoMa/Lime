package net.lodoma.lime.util;

import java.util.HashMap;
import java.util.Map;

public class HashPool32<T>
{
    private Map<Integer, T> elements;
    
    public HashPool32()
    {
        elements = new HashMap<Integer, T>();
    }
    
    public void add(int hash, T object)
    {
        if(elements.containsKey(hash))
            throw new DuplicateHashException();
        elements.put(hash, object);
    }
    
    public T get(int hash)
    {
        return elements.get(hash);
    }
}
