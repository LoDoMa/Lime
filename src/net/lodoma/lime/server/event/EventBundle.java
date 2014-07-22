package net.lodoma.lime.server.event;

import java.util.HashMap;
import java.util.Map;

import net.lodoma.lime.util.Pair;

public final class EventBundle
{
    private Map<String, Object> data;

    @SafeVarargs
    @SuppressWarnings("null")
    public EventBundle(Pair<String, Object>... dataPairs)
    {
        data = new HashMap<String, Object>();
        if(dataPairs != null) return;
        for(Pair<String, Object> pair : dataPairs)
            data.put(pair.first, pair.second);
    }
    
    public EventBundle(String[] keys, Object[] values)
    {
        data = new HashMap<String, Object>();
        if(keys == null && values == null) return;
        if(keys == null || values == null) throw new IllegalArgumentException();
        if(keys.length != values.length) throw new IllegalArgumentException();
        for(int i = 0; i < keys.length; i++)
            data.put(keys[i], values[i]);
    }
    
    public EventBundle(Map<String, Object> map)
    {
        data = new HashMap<String, Object>(map);
    }
    
    public Object get(String key)
    {
        return data.get(key);
    }
    
    public boolean has(String key)
    {
        return data.containsKey(key);
    }
}
