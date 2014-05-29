package net.lodoma.lime.mod.init;

import java.util.Map;

import net.lodoma.lime.util.DataBundle;

public class InitBundle extends DataBundle
{
    public static final String MODULE = "module";
    public static final String CLIENT = "client";
    public static final String SERVER = "server";
    
    public InitBundle(String[] names, Object[] values)
    {
        super(names, values);
    }
    
    public InitBundle(Map<String, Object> data)
    {
        super(data);
    }
}
