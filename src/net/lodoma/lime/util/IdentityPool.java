package net.lodoma.lime.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class IdentityPool<I, T extends Identifiable<I>>
{
    private Map<I, T> objects;
    
    public IdentityPool()
    {
        objects = new HashMap<I, T>();
    }
    
    public synchronized void add(T object)
    {
        objects.put(object.getIdentifier(), object);
    }
    
    public synchronized T get(I identifier)
    {
        return objects.get(identifier);
    }
    
    public synchronized void remove(I identifier)
    {
        objects.remove(identifier);
    }
    
    public synchronized Set<I> getIdentifierSet()
    {
        return new HashSet<I>(objects.keySet());
    }
    
    public synchronized List<T> getObjectList()
    {
        return new ArrayList<T>(objects.values());
    }
    
    public synchronized int size()
    {
        return objects.size();
    }
}
