package net.lodoma.lime.shader.light;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.lodoma.lime.gui.Color;
import net.lodoma.lime.util.Vector2;

public class LightData
{
    public Vector2 position;
    public float radius;
    public Color color;
    
    public LightData()
    {
        position = new Vector2(0.0f, 0.0f);
        color = new Color(0.0f, 0.0f, 0.0f, 0.0f);
    }
    
    public void read(DataInputStream inputStream) throws IOException
    {
        position.set(inputStream.readFloat(), inputStream.readFloat());
        radius = inputStream.readFloat();
        color.set(inputStream.readFloat(), inputStream.readFloat(), inputStream.readFloat(), inputStream.readFloat());
    }
    
    public void write(DataOutputStream outputStream) throws IOException
    {
        outputStream.writeFloat(position.x);
        outputStream.writeFloat(position.y);
        outputStream.writeFloat(radius);
        outputStream.writeFloat(color.getR());
        outputStream.writeFloat(color.getG());
        outputStream.writeFloat(color.getB());
        outputStream.writeFloat(color.getA());
    }
}
