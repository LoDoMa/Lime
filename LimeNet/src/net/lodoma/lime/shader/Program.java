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
    
    public boolean hasUniform(String uniformName)
    {
        return GL20.glGetUniformLocation(program, uniformName) != -1;
    }
    
    public void setUniformI1(String uniformName, int v0)
    {
        int location = GL20.glGetUniformLocation(program, uniformName);
        if(location == -1)
            throw new InvalidUniformNameException(uniformName);
        GL20.glUniform1i(location, v0);
    }
}
