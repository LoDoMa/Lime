package net.lodoma.lime.client.packet;

import java.io.IOException;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientPacketHandler;
import net.lodoma.lime.shader.light.LightData;
import net.lodoma.lime.util.HashHelper;
import net.lodoma.lime.world.Snapshot;
import net.lodoma.lime.world.entity.EntityShape;
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

        int removedTerrainCount = inputStream.readInt();
        while ((removedTerrainCount--) != 0)
            snapshot.removedTerrain.add(inputStream.readInt());
        
        int removedEntityCount = inputStream.readInt();
        while ((removedEntityCount--) != 0)
            snapshot.removedEntities.add(inputStream.readInt());
        
        int removedLightCount = inputStream.readInt();
        while ((removedLightCount--) != 0)
            snapshot.removedLights.add(inputStream.readInt());

        int terrainCount = inputStream.readInt();
        client.world.terrain.componentSnapshots = new PhysicsComponentSnapshot[terrainCount];
        for (int i = 0; i < terrainCount; i++)
        {
            @SuppressWarnings("unused")
            int identifier = inputStream.readInt();
            client.world.terrain.componentSnapshots[i] = new PhysicsComponentSnapshot();
            client.world.terrain.componentSnapshots[i].read(inputStream);
        }
            
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
                shape.snapshots[i].read(inputStream);
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