package net.lodoma.lime.world.physics;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.lodoma.lime.util.Color;

// Shape attachments - data that stays with physical shapes after creation
public class PhysicsShapeAttachments
{
    public String name;
    
    public final Color color = new Color();
    
    public boolean compareVisual(PhysicsShapeAttachments other)
    {
        if (!color.equals(other.color)) return false;
        return true;
    }

    public void readVisual(DataInputStream in) throws IOException
    {
        color.r = in.readFloat();
        color.g = in.readFloat();
        color.b = in.readFloat();
        color.a = in.readFloat();
    }
    
    public void writeVisual(DataOutputStream out) throws IOException
    {
        out.writeFloat(color.r);
        out.writeFloat(color.g);
        out.writeFloat(color.b);
        out.writeFloat(color.a);
    }
}
