package net.lodoma.lime.shader;

import org.lwjgl.opengl.GL20;

public class Program
{
    private int program;
    
    public Program(ShaderPool pool, String... shaderKeys)
    {
        createProgram();
        for(String shaderKey : shaderKeys)
            attachShader(pool.getShader(shaderKey));
        linkProgram();
        validateProgram();
    }
    
    private void createProgram()
    {
        program = GL20.glCreateProgram();
    }
    
    private void attachShader(Shader shader)
    {
        GL20.glAttachShader(program, shader.shader);
    }
    
    private void linkProgram()
    {
        GL20.glLinkProgram(program);
    }
    
    private void validateProgram()
    {
        GL20.glValidateProgram(program);
    }
    
    public void useProgram()
    {
        GL20.glUseProgram(program);
    }
    
    public void deleteProgram()
    {
        GL20.glDeleteProgram(program);
    }
}
