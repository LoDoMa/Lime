package net.lodoma.lime.util;

import java.util.HashMap;
import java.util.Map;

public class HashPool64<T>
{
    private Map<Long, T> elements;
    
    public HashPool64()
    {
        elements = new HashMap<Long, T>();
    }
    
    public void add(long hash, T object)
    {
        if(elements.containsKey(hash))
            throw new HashPoolException("duplicate hash in 64-bit hash pool");
        elements.put(hash, object);
    }
    
    public T get(long hash)
    {
        return elements.get(hash);
    }
}
