package net.lodoma.lime.shader.light;

import static org.lwjgl.opengl.GL11.*;

import java.io.File;

import net.lodoma.lime.shader.Program;
import net.lodoma.lime.shader.Shader;
import net.lodoma.lime.shader.ShaderType;
import net.lodoma.lime.shader.UniformType;
import net.lodoma.lime.util.Identifiable;

public class Light implements Identifiable<Integer>
{
    public static Shader vertexShader;
    public static Shader fragmentShader;
    public static Program shaderProgram;
    
    public int identifier;
    
    public LightData data;
    
    public Light()
    {
        data = new LightData();
    }
    
    @Override
    public Integer getIdentifier()
    {
        return identifier;
    }
    
    @Override
    public void setIdentifier(Integer identifier)
    {
        this.identifier = identifier;
    }
    
    public void render()
    {
        if(shaderProgram == null)
        {
            vertexShader = Shader.getShader("lightVS", new File("shader/light.vs"), ShaderType.VERTEX);
            fragmentShader = Shader.getShader("lightFS", new File("shader/light.fs"), ShaderType.FRAGMENT);
            shaderProgram = Program.getProgram("light", vertexShader, fragmentShader);
        }
        shaderProgram.useProgram();
        
        shaderProgram.setUniform("lightColor", UniformType.FLOAT4, data.color.getR(), data.color.getG(), data.color.getB(), data.color.getA());
        shaderProgram.setUniform("lightPos", UniformType.FLOAT2, data.position.x, data.position.y);
        shaderProgram.setUniform("lightRadius", UniformType.FLOAT1, data.radius);
        
        shaderProgram.setUniform("lowerAngle", UniformType.FLOAT1, data.angleRangeBegin);
        shaderProgram.setUniform("upperAngle", UniformType.FLOAT1, data.angleRangeEnd);
        
        glBegin(GL_QUADS);
        {
            glVertex2f(data.position.x - data.radius, data.position.y - data.radius);
            glVertex2f(data.position.x + data.radius, data.position.y - data.radius);
            glVertex2f(data.position.x + data.radius, data.position.y + data.radius);
            glVertex2f(data.position.x - data.radius, data.position.y + data.radius);
        }
        glEnd();
    }
}
