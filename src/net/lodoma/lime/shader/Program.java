package net.lodoma.lime.shader;

import java.io.File;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import net.lodoma.lime.Lime;
import net.lodoma.lime.util.OsHelper;

import org.lwjgl.opengl.GL20;

public class Program
{
    public static Shader copyVS, copyFS;

    public static Shader basicVS, basicFS;
    public static Shader shadowMapVS, shadowMapFS;
    public static Shader renderLightVS, renderLightFS;
    public static Shader brightnessVS, brightnessFS;
    
    public static Program copyProgram;

    public static Program basicProgram;
    public static Program shadowMapProgram;
    public static Program renderLightProgram;
    public static Program brightnessProgram;
    
    public static void createAll()
    {
        Lime.LOGGER.D("About to create shaders");
        
        copyVS = new Shader(new File(OsHelper.JARPATH + "shader/copy.vs"), ShaderType.VERTEX);
        copyFS = new Shader(new File(OsHelper.JARPATH + "shader/copy.fs"), ShaderType.FRAGMENT);
        basicVS = new Shader(new File(OsHelper.JARPATH + "shader/Basic.vs"), ShaderType.VERTEX);
        basicFS = new Shader(new File(OsHelper.JARPATH + "shader/Basic.fs"), ShaderType.FRAGMENT);
        shadowMapVS = new Shader(new File(OsHelper.JARPATH + "shader/ShadowMap.vs"), ShaderType.VERTEX);
        shadowMapFS = new Shader(new File(OsHelper.JARPATH + "shader/ShadowMap.fs"), ShaderType.FRAGMENT);
        renderLightVS = new Shader(new File(OsHelper.JARPATH + "shader/RenderLight.vs"), ShaderType.VERTEX);
        renderLightFS = new Shader(new File(OsHelper.JARPATH + "shader/RenderLight.fs"), ShaderType.FRAGMENT);
        brightnessVS = new Shader(new File(OsHelper.JARPATH + "shader/Brightness.vs"), ShaderType.VERTEX);
        brightnessFS = new Shader(new File(OsHelper.JARPATH + "shader/Brightness.fs"), ShaderType.FRAGMENT);

        Lime.LOGGER.D("About to programs shaders");
        
        copyProgram = new Program(copyVS, copyFS);
        basicProgram = new Program(basicVS, basicFS);
        shadowMapProgram = new Program(shadowMapVS, shadowMapFS);
        renderLightProgram = new Program(renderLightVS, renderLightFS);
        brightnessProgram = new Program(brightnessVS, brightnessFS);

        Lime.LOGGER.D("About to delete shader objects");

        copyVS.deleteShader();
        copyFS.deleteShader();
        basicVS.deleteShader();
        basicFS.deleteShader();
        shadowMapVS.deleteShader();
        shadowMapFS.deleteShader();
        renderLightVS.deleteShader();
        renderLightFS.deleteShader();
        brightnessVS.deleteShader();
        brightnessFS.deleteShader();
    }
    
    public static void destroyAll()
    {
        Lime.LOGGER.D("About to delete programs");
        
        copyProgram.deleteProgram();
        basicProgram.deleteProgram();
        shadowMapProgram.deleteProgram();
        renderLightProgram.deleteProgram();
        brightnessProgram.deleteProgram();
    }
    
    private int program;
    private Map<String, Integer> uniformLocations;
    
    public Program(Shader... shaders)
    {
        createProgram();
        
        for(Shader shader : shaders)
            attachShader(shader);
        linkProgram();
        validateProgram();
        
        uniformLocations = new HashMap<String, Integer>();
    }
    
    private void createProgram()
    {
        program = GL20.glCreateProgram();
        Lime.LOGGER.F("Created program " + program);
    }
    
    private void attachShader(Shader shader)
    {
        Lime.LOGGER.F("Attaching shader " + shader.shader + " to program " + program);
        GL20.glAttachShader(program, shader.shader);
    }
    
    private void linkProgram()
    {
        Lime.LOGGER.F("Linking program " + program);
        GL20.glLinkProgram(program);
    }
    
    private void validateProgram()
    {
        Lime.LOGGER.F("Validating program " + program);
        GL20.glValidateProgram(program);
    }
    
    public void useProgram()
    {
        GL20.glUseProgram(program);
    }
    
    public void deleteProgram()
    {
        Lime.LOGGER.F("Deleting program " + program);
        GL20.glDeleteProgram(program);
    }
    
    // NOTE: This method is slow and really doesn't have a good use. Deprecated for now.
    @Deprecated
    public boolean hasUniform(String uniformName)
    {
        return GL20.glGetUniformLocation(program, uniformName) != -1;
    }
    
    public void setUniform(String uniformName, UniformType type, Object... args)
    {
        Integer location = uniformLocations.get(uniformName);
        if (location == null)
        {
            location = GL20.glGetUniformLocation(program, uniformName);
            if(location == -1)
                return;
            uniformLocations.put(uniformName, location);
        }
        
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
