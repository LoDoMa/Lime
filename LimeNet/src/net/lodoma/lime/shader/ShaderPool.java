package net.lodoma.lime.shader;

import java.util.HashMap;
import java.util.Map;

public class ShaderPool
{
    private Map<String, Shader> shaders = new HashMap<String, Shader>();
    
    public void addShader(String shaderKey, Shader shader)
    {
        if(shaders.containsKey(shaderKey))
            throw new DuplicateShaderException(shaderKey);
        shaders.put(shaderKey, shader);
    }
    
    public Shader getShader(String shaderKey)
    {
        if(!shaders.containsKey(shaderKey))
            throw new NullPointerException(shaderKey);
        return shaders.get(shaderKey);
    }
    
    public void removeShader(String shaderKey)
    {
        if(!shaders.containsKey(shaderKey))
            throw new NullPointerException(shaderKey);
        shaders.remove(shaderKey);
    }
    
    public boolean containsShader(String shaderKey)
    {
        return shaders.containsKey(shaderKey);
    }
}
