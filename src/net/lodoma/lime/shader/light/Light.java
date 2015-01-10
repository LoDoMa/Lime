package net.lodoma.lime.shader.light;

import static org.lwjgl.opengl.GL11.*;

import net.lodoma.lime.shader.Program;
import net.lodoma.lime.shader.UniformType;
import net.lodoma.lime.util.Identifiable;

public class Light implements Identifiable<Integer>
{
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
        Program.lightProgram.useProgram();
        
        Program.lightProgram.setUniform("lightColor", UniformType.FLOAT4, data.color.getR(), data.color.getG(), data.color.getB(), data.color.getA());
        Program.lightProgram.setUniform("lightPos", UniformType.FLOAT2, data.position.x, data.position.y);
        Program.lightProgram.setUniform("lightRadius", UniformType.FLOAT1, data.radius);
        
        Program.lightProgram.setUniform("lowerAngle", UniformType.FLOAT1, data.angleRangeBegin);
        Program.lightProgram.setUniform("upperAngle", UniformType.FLOAT1, data.angleRangeEnd);
        
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
