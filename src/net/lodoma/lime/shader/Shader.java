package net.lodoma.lime.shader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import net.lodoma.lime.Lime;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class Shader
{
    int shader;
    private ShaderType type;
    
    public Shader(File sourceFile, ShaderType type)
    {
        this.type = type;
        
        createShader();
        Lime.LOGGER.F("Created shader " + shader + "; type = " + type.name() + ", file = " + sourceFile.getPath());

        String source = loadSource(sourceFile);
        setShaderSource(source);
        compileShader();
    }
    
    private String loadSource(File sourceFile)
    {
        StringBuilder builder = new StringBuilder();
        try(BufferedReader reader = new BufferedReader(new FileReader(sourceFile)))
        {
            String line;
            while((line = reader.readLine()) != null)
            {
                builder.append(line);
                builder.append("\n");
            }
        }
        catch(IOException e)
        {
            Lime.LOGGER.C("Failed to load shader " + shader + " source; file = " + sourceFile.getPath());
            Lime.LOGGER.log(e);
            Lime.forceExit(e);
        }
        
        return builder.toString();
    }
    
    private void createShader()
    {
        shader = GL20.glCreateShader(type.GLENUM);
    }
    
    private void setShaderSource(String source)
    {
        GL20.glShaderSource(shader, source);
    }
    
    private void compileShader()
    {
        GL20.glCompileShader(shader);
        if(GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE)
        {
            String infoLog = GL20.glGetShaderInfoLog(shader, 1024);

            Lime.LOGGER.C("Failed to compile shader " + shader + "\n" + infoLog);
            Lime.forceExit(null);
        }
        Lime.LOGGER.F("Compiled shader " + shader);
    }
    
    public void deleteShader()
    {
        GL20.glDeleteShader(shader);
        Lime.LOGGER.F("Deleted shader " + shader);
    }
}
