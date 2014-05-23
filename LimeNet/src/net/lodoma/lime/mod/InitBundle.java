package net.lodoma.lime.mod;

import java.util.Map;

public class InitBundle extends DataBundle
{
    public static final String MODULE = "module";
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
