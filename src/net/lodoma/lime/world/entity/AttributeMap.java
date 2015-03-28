package net.lodoma.lime.world.entity;

import java.util.HashMap;
import java.util.Map;

import org.luaj.vm2.LuaValue;

public class AttributeMap
{
    public Map<String, LuaValue> values;
    
    public AttributeMap()
    {
        values = new HashMap<String, LuaValue>();
    }
}
