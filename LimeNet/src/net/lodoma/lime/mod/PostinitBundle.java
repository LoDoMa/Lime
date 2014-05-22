package net.lodoma.lime.mod;

import java.util.Map;

public class PostinitBundle extends DataBundle
{
    public PostinitBundle(String[] names, Object[] values)
    {
        super(names, values);
    }
    
    public PostinitBundle(Map<String, Object> data)
    {
        super(data);
    }
}
