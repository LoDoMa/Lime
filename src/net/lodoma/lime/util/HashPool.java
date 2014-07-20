package net.lodoma.lime.util;

import java.util.HashMap;
import java.util.Map;

public class HashPool<T>
{
    private Map<Long, T> elements;
    
    public HashPool()
    {
        elements = new HashMap<Long, T>();
    }
    
    public void add(String name, T object)
    {
        long hash = HashHelper.hash64(name);
        if(elements.containsKey(hash))
            throw new DuplicateHashException();
        elements.put(HashHelper.hash64(name), object);
    }
    
    public T get(String name)
    {
        return elements.get(HashHelper.hash64(name));
    }
    
    public T get(long hash)
    {
        return elements.get(hash);
    }
}
