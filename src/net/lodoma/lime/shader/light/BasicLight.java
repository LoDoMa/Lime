package net.lodoma.lime.shader.light;

import java.io.File;

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
    private static final String TYPE_NAME = "Lime::BasicLight";
    private static final int TYPE_HASH = HashHelper.hash32(TYPE_NAME);
    
    private static Shader vertexShader;
    private static Shader fragmentShader;
    
    private static Program shaderProgram;
    
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
    
    @Override
    public int getTypeHash()
    {
        return TYPE_HASH;
    }
    
    @Override
    public void useProgram()
    {
        if(shaderProgram == null)
        {
            vertexShader = new Shader(new File("shader/light.vs"), ShaderType.VERTEX);
            fragmentShader = new Shader(new File("shader/light.fs"), ShaderType.FRAGMENT);
            shaderProgram = new Program(vertexShader, fragmentShader);
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
}
