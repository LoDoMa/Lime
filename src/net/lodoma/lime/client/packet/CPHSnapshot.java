package net.lodoma.lime.client.packet;

import java.io.IOException;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientPacketHandler;
import net.lodoma.lime.shader.light.LightData;
import net.lodoma.lime.util.HashHelper;
import net.lodoma.lime.util.Vector2;
import net.lodoma.lime.world.Snapshot;
import net.lodoma.lime.world.entity.EntityShape;
import net.lodoma.lime.world.physics.PhysicsComponentShapeType;
import net.lodoma.lime.world.physics.PhysicsComponentSnapshot;

public class CPHSnapshot extends ClientPacketHandler
{
    public static final String NAME = "Lime::Snapshot";
    public static final int HASH = HashHelper.hash32(NAME);
    
    public CPHSnapshot(Client client)
    {
        super(client, HASH);
    }
    
    @Override
    protected void localHandle() throws IOException
    {
        Snapshot snapshot = new Snapshot();
        snapshot.isDelta = inputStream.readBoolean();
        
        int removedEntityCount = inputStream.readInt();
        while ((removedEntityCount--) != 0)
            snapshot.removedEntities.add(inputStream.readInt());
        
        int removedLightCount = inputStream.readInt();
        while ((removedLightCount--) != 0)
            snapshot.removedLights.add(inputStream.readInt());
        
        int shapeCount = inputStream.readInt();
        while ((shapeCount--) != 0)
        {
            int identifier = inputStream.readInt();
            int componentCount = inputStream.readInt();
            
            EntityShape shape = new EntityShape();
            shape.snapshots = new PhysicsComponentSnapshot[componentCount];;
            
            for (int i = 0; i < componentCount; i++)
            {
                shape.snapshots[i] = new PhysicsComponentSnapshot();
                shape.snapshots[i].position = new Vector2(inputStream.readFloat(), inputStream.readFloat());
                shape.snapshots[i].angle = inputStream.readFloat();
                shape.snapshots[i].type = PhysicsComponentShapeType.values()[inputStream.readInt()];
                switch (shape.snapshots[i].type)
                {
                case CIRCLE:
                    shape.snapshots[i].radius = inputStream.readFloat();
                    break;
                case POLYGON:
                    shape.snapshots[i].vertices = new Vector2[inputStream.readInt()];
                    for (int j = 0; j < shape.snapshots[i].vertices.length; j++)
                        shape.snapshots[i].vertices[j] = new Vector2(inputStream.readFloat(), inputStream.readFloat());
                    break;
                }
            }
            
            snapshot.entityData.put(identifier, shape);
        }
        
        int lightDataCount = inputStream.readInt();
        while ((lightDataCount--) != 0)
        {
            int identifier = inputStream.readInt();
            
            LightData data = new LightData();
            data.position.set(inputStream.readFloat(), inputStream.readFloat());
            data.radius = inputStream.readFloat();
            data.color.set(inputStream.readFloat(), inputStream.readFloat(), inputStream.readFloat(), inputStream.readFloat());
            data.angleRangeBegin = inputStream.readFloat();
            data.angleRangeEnd = inputStream.readFloat();
            
            snapshot.lightData.put(identifier, data);
        }
        
        client.world.applySnapshot(snapshot, client);
    }
}