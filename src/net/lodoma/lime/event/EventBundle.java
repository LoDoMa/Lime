package net.lodoma.lime.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.lodoma.lime.script.LuaScript;
import net.lodoma.lime.util.Pair;

public final class EventBundle
{
    private Map<String, Object> data;
    
    private boolean luaSafe;

    @SafeVarargs
    @SuppressWarnings("null")
    public EventBundle(Pair<String, Object>... dataPairs)
    {
        data = new HashMap<String, Object>();
        luaSafe = true;
        
        if(dataPairs != null) return;
        
        for(Pair<String, Object> pair : dataPairs)
        {
            data.put(pair.first, pair.second);
            if(luaSafe && !LuaScript.safeType(pair.second))
                luaSafe = false;
        }
    }
    
    public EventBundle(String[] keys, Object[] values)
    {
        data = new HashMap<String, Object>();
        luaSafe = true;
        
        if(keys == null && values == null) return;
        if(keys == null || values == null) throw new IllegalArgumentException();
        if(keys.length != values.length) throw new IllegalArgumentException();
        
        for(int i = 0; i < keys.length; i++)
        {
            data.put(keys[i], values[i]);
            if(luaSafe && !LuaScript.safeType(values[i]))
                luaSafe = false;
        }
    }
    
    public EventBundle(Map<String, Object> map)
    {
        data = new HashMap<String, Object>(map);
    }
    
    public Object get(String key)
    {
        return data.get(key);
    }
    
    public List<String> getKeyList()
    {
        return new ArrayList<String>(data.keySet());
    }
    
    public boolean has(String key)
    {
        return data.containsKey(key);
    }
    
    public boolean isLuaSafe()
    {
        return luaSafe;
    }
}
