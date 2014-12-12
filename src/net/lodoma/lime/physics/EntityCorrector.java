package net.lodoma.lime.physics;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class EntityCorrector
{
    public static void applyCorrection(Entity entity, DataInputStream input) throws IOException
    {
        entity.body.position.x = input.readFloat();
        entity.body.position.y = input.readFloat();
        entity.body.velocity.x = input.readFloat();
        entity.body.velocity.y = input.readFloat();
    }
    
    public static void outputCorrection(Entity entity, DataOutputStream output) throws IOException
    {
        output.writeFloat(entity.body.position.x);
        output.writeFloat(entity.body.position.y);
        output.writeFloat(entity.body.velocity.x);
        output.writeFloat(entity.body.velocity.y);
    }
}
