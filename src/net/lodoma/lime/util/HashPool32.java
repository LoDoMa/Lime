package net.lodoma.lime.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

// NOTE: HashPool32 is NOT thread safe. It should be synchronized later.
// NOTE: Also, HashPool32 isn't different from IdentityPool at all
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
            throw new HashPoolException("duplicate hash in 32-bit hash pool");
        elements.put(hash, object);
    }
    
    public T get(int hash)
    {
        return elements.get(hash);
    }
    
    public void remove(int hash)
    {
        if(!elements.containsKey(hash))
            throw new HashPoolException("remove element with non-existing hash in 32-bit hash pool");
        elements.remove(hash);
    }
    
    public void foreach(Consumer<T> consumer)
    {
        List<T> objects = new ArrayList<T>(elements.values());
        for (T object : objects)
            consumer.accept(object);
    }
}
