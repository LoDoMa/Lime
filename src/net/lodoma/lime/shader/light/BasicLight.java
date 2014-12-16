package net.lodoma.lime.shader.light;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

import net.lodoma.lime.gui.Color;
import net.lodoma.lime.shader.Program;
import net.lodoma.lime.shader.Shader;
import net.lodoma.lime.shader.ShaderType;
import net.lodoma.lime.shader.UniformType;
import net.lodoma.lime.util.HashHelper;
import net.lodoma.lime.util.Vector2;
import static org.lwjgl.opengl.GL11.*;

public class BasicLight implements Light
{
    public static final String NAME = "Lime::BasicLight";
    public static final int HASH = HashHelper.hash32(NAME);
    
    private static Shader vertexShader;
    private static Shader fragmentShader;
    
    private static Program shaderProgram;
    
    private int identifier;
    
    private Vector2 position;
    private float radius;
    private Color lightColor;
    private float angleFrom;
    private float angleTo;
    
    public BasicLight(Vector2 position, float radius, Color lightColor, float angleFrom, float angleTo)
    {
        this.position = position;
        this.radius = radius;
        this.lightColor = lightColor;
        this.angleFrom = (float) (Math.toRadians(angleFrom) - Math.PI);
        this.angleTo = (float) (Math.toRadians(angleTo) - Math.PI);
    }
    
    public BasicLight(Vector2 position, float radius, Color lightColor)
    {
        this(position, radius, lightColor, -1.0f, 361.0f);
    }
    
    public BasicLight(DataInputStream inputStream) throws IOException
    {
        read(inputStream);
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
    
    @Override
    public int getTypeHash()
    {
        return HASH;
    }
    
    @Override
    public void useProgram()
    {
        if(shaderProgram == null)
        {
            vertexShader = Shader.getShader("lightVS", new File("shader/light.vs"), ShaderType.VERTEX);
            fragmentShader = Shader.getShader("lightFS", new File("shader/light.fs"), ShaderType.FRAGMENT);
            shaderProgram = Program.getProgram("light", vertexShader, fragmentShader);
        }
        
        shaderProgram.useProgram();
    }

    @Override
    public void render()
    {
        shaderProgram.setUniform("lightColor", UniformType.FLOAT4, lightColor.getR(), lightColor.getG(), lightColor.getB(), lightColor.getA());
        shaderProgram.setUniform("lightPos", UniformType.FLOAT2, position.x, position.y);
        shaderProgram.setUniform("lightRadius", UniformType.FLOAT1, radius);
        
        shaderProgram.setUniform("lowerAngle", UniformType.FLOAT1, angleFrom);
        shaderProgram.setUniform("upperAngle", UniformType.FLOAT1, angleTo);
        
        glBegin(GL_QUADS);
        {
            glVertex2f(position.x - radius, position.y - radius);
            glVertex2f(position.x + radius, position.y - radius);
            glVertex2f(position.x + radius, position.y + radius);
            glVertex2f(position.x - radius, position.y + radius);
        }
        glEnd();
    }
    
    @Override
    public void write(DataOutputStream outputStream) throws IOException
    {
        outputStream.writeFloat(position.x);
        outputStream.writeFloat(position.y);
        outputStream.writeFloat(radius);
        outputStream.writeFloat(lightColor.getR());
        outputStream.writeFloat(lightColor.getG());
        outputStream.writeFloat(lightColor.getB());
        outputStream.writeFloat(lightColor.getA());
        outputStream.writeFloat(angleFrom);
        outputStream.writeFloat(angleTo);
    }
    
    @Override
    public void read(DataInputStream inputStream) throws IOException
    {
        position = new Vector2();
        position.x = inputStream.readFloat();
        position.y = inputStream.readFloat();
        radius = inputStream.readFloat();
        
        lightColor = new Color(inputStream.readFloat(),
                               inputStream.readFloat(),
                               inputStream.readFloat(),
                               inputStream.readFloat());
        
        angleFrom = inputStream.readFloat();
        angleTo = inputStream.readFloat();
    }
}
