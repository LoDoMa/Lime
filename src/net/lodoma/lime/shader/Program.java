package net.lodoma.lime.shader;

import java.io.File;
import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL20;

public class Program
{
    public static Shader menuVS, menuFS;
    public static Shader lightVS, lightFS;
    public static Shader worldVS, worldFS;
    public static Shader copyVS, copyFS;
    
    public static Program menuProgram;
    public static Program lightProgram;
    public static Program worldProgram;
    public static Program copyProgram;
    
    public static void createAll()
    {
        menuVS = new Shader(new File("shader/menu.vs"), ShaderType.VERTEX);
        menuFS = new Shader(new File("shader/menu.fs"), ShaderType.FRAGMENT);
        menuProgram = new Program(menuVS, menuFS);
        
        lightVS = new Shader(new File("shader/light.vs"), ShaderType.VERTEX);
        lightFS = new Shader(new File("shader/light.fs"), ShaderType.FRAGMENT);
        lightProgram = new Program(lightVS, lightFS);
        
        worldVS = new Shader(new File("shader/world.vs"), ShaderType.VERTEX);
        worldFS = new Shader(new File("shader/world.fs"), ShaderType.FRAGMENT);
        worldProgram = new Program(worldVS, worldFS);
        
        copyVS = new Shader(new File("shader/copy.vs"), ShaderType.VERTEX);
        copyFS = new Shader(new File("shader/copy.fs"), ShaderType.FRAGMENT);
        copyProgram = new Program(copyVS, copyFS);
    }
    
    public static void destroyAll()
    {
        menuProgram.deleteProgram();
        lightProgram.deleteProgram();
        worldProgram.deleteProgram();
        copyProgram.deleteProgram();

        menuVS.deleteShader();
        menuFS.deleteShader();
        lightVS.deleteShader();
        lightFS.deleteShader();
        worldVS.deleteShader();
        worldFS.deleteShader();
        copyVS.deleteShader();
        copyFS.deleteShader();
    }
    
    private int program;
    
    public Program(Shader... shaders)
    {
        createProgram();
        for(Shader shader : shaders)
            attachShader(shader);
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
    
    public void setUniform(String uniformName, UniformType type, Object... args)
    {
        int location = GL20.glGetUniformLocation(program, uniformName);
        if(location == -1)
            return;
        switch(type)
        {
        case INT1:
        {
            if(!(args[0] instanceof Integer)) throw new IllegalArgumentException();
            int v0 = (Integer) args[0];
            GL20.glUniform1i(location, v0);
            break;
        }
        case INT2:
        {
            if(!(args[0] instanceof Integer)) throw new IllegalArgumentException();
            if(!(args[1] instanceof Integer)) throw new IllegalArgumentException();
            int v0 = (Integer) args[0];
            int v1 = (Integer) args[1];
            GL20.glUniform2i(location, v0, v1);
            break;
        }
        case INT3:
        {
            if(!(args[0] instanceof Integer)) throw new IllegalArgumentException();
            if(!(args[1] instanceof Integer)) throw new IllegalArgumentException();
            if(!(args[2] instanceof Integer)) throw new IllegalArgumentException();
            int v0 = (Integer) args[0];
            int v1 = (Integer) args[1];
            int v2 = (Integer) args[2];
            GL20.glUniform3i(location, v0, v1, v2);
            break;
        }
        case INT4:
        {
            if(!(args[0] instanceof Integer)) throw new IllegalArgumentException();
            if(!(args[1] instanceof Integer)) throw new IllegalArgumentException();
            if(!(args[2] instanceof Integer)) throw new IllegalArgumentException();
            if(!(args[3] instanceof Integer)) throw new IllegalArgumentException();
            int v0 = (Integer) args[0];
            int v1 = (Integer) args[1];
            int v2 = (Integer) args[2];
            int v3 = (Integer) args[3];
            GL20.glUniform4i(location, v0, v1, v2, v3);
            break;
        }
        case FLOAT1:
        {
            if(!(args[0] instanceof Float)) throw new IllegalArgumentException();
            float v0 = (Float) args[0];
            GL20.glUniform1f(location, v0);
            break;
        }
        case FLOAT2:
        {
            if(!(args[0] instanceof Float)) throw new IllegalArgumentException();
            if(!(args[1] instanceof Float)) throw new IllegalArgumentException();
            float v0 = (Float) args[0];
            float v1 = (Float) args[1];
            GL20.glUniform2f(location, v0, v1);
            break;
        }
        case FLOAT3:
        {
            if(!(args[0] instanceof Float)) throw new IllegalArgumentException();
            if(!(args[1] instanceof Float)) throw new IllegalArgumentException();
            if(!(args[2] instanceof Float)) throw new IllegalArgumentException();
            float v0 = (Float) args[0];
            float v1 = (Float) args[1];
            float v2 = (Float) args[2];
            GL20.glUniform3f(location, v0, v1, v2);
            break;
        }
        case FLOAT4:
        {
            if(!(args[0] instanceof Float)) throw new IllegalArgumentException();
            if(!(args[1] instanceof Float)) throw new IllegalArgumentException();
            if(!(args[2] instanceof Float)) throw new IllegalArgumentException();
            if(!(args[3] instanceof Float)) throw new IllegalArgumentException();
            float v0 = (Float) args[0];
            float v1 = (Float) args[1];
            float v2 = (Float) args[2];
            float v3 = (Float) args[3];
            GL20.glUniform4f(location, v0, v1, v2, v3);
            break;
        }
        case MATRIX2:
        {
            if(!(args[0] instanceof Boolean)) throw new IllegalArgumentException();
            if(!(args[1] instanceof FloatBuffer)) throw new IllegalArgumentException();
            boolean transpose = (Boolean) args[0];
            FloatBuffer matrices = (FloatBuffer) args[1];
            GL20.glUniformMatrix2(location, transpose, matrices);
            break;
        }
        case MATRIX3:
        {
            if(!(args[0] instanceof Boolean)) throw new IllegalArgumentException();
            if(!(args[1] instanceof FloatBuffer)) throw new IllegalArgumentException();
            boolean transpose = (Boolean) args[0];
            FloatBuffer matrices = (FloatBuffer) args[1];
            GL20.glUniformMatrix3(location, transpose, matrices);
            break;
        }
        case MATRIX4:
        {
            if(!(args[0] instanceof Boolean)) throw new IllegalArgumentException();
            if(!(args[1] instanceof FloatBuffer)) throw new IllegalArgumentException();
            boolean transpose = (Boolean) args[0];
            FloatBuffer matrices = (FloatBuffer) args[1];
            GL20.glUniformMatrix4(location, transpose, matrices);
            break;
        }
        }
    }
}
