package net.lodoma.lime.client.packet;

import java.io.IOException;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientPacketHandler;
import net.lodoma.lime.shader.light.LightModifications;
import net.lodoma.lime.util.Color;
import net.lodoma.lime.util.HashHelper;
import net.lodoma.lime.util.Vector2;
import net.lodoma.lime.world.SnapshotSegment;
import net.lodoma.lime.world.physics.PhysicsComponentModifications;
import net.lodoma.lime.world.physics.PhysicsComponentShapeType;

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
        SnapshotSegment segment = new SnapshotSegment();

        float cameraPositionX = inputStream.readFloat();
        float cameraPositionY = inputStream.readFloat();
        float cameraRotation = inputStream.readFloat();
        float cameraScaleX = inputStream.readFloat();
        float cameraScaleY = inputStream.readFloat();
        
        client.worldRenderer.camera.translation.set(cameraPositionX, cameraPositionY);
        client.worldRenderer.camera.rotation = cameraRotation;
        client.worldRenderer.camera.scale.set(cameraScaleX, cameraScaleY);

        int createdComponentsAmount = inputStream.readInt();
        int removedComponentsAmount = inputStream.readInt();
        int modifiedComponentsAmount = inputStream.readInt();

        int createdLightsAmount = inputStream.readInt();
        int removedLightsAmount = inputStream.readInt();
        int modifiedLightsAmount = inputStream.readInt();
        
        segment.createdComponents = new int[createdComponentsAmount];
        segment.removedComponents = new int[removedComponentsAmount];
        segment.modifiedComponents = new long[modifiedComponentsAmount];
        segment.productComponents = new PhysicsComponentModifications[modifiedComponentsAmount];
        
        segment.createdLights = new int[createdLightsAmount];
        segment.removedLights = new int[removedLightsAmount];
        segment.modifiedLights = new long[modifiedLightsAmount];
        segment.productLights = new LightModifications[modifiedLightsAmount];

        for(int i = 0; i < createdComponentsAmount; i++)
            segment.createdComponents[i] = inputStream.readInt();
        for(int i = 0; i < removedComponentsAmount; i++)
            segment.removedComponents[i] = inputStream.readInt();
        for(int i = 0; i < modifiedComponentsAmount; i++)
        {
            long data = inputStream.readLong();
            segment.modifiedComponents[i] = data;
            
            PhysicsComponentModifications modifications = new PhysicsComponentModifications();
            segment.productComponents[i] = modifications;
            
            if ((data & SnapshotSegment.MODIFIED_POSITION) != 0)
            {
                float positionX = inputStream.readFloat();
                float positionY = inputStream.readFloat();
                
                modifications.positionModified = true;
                modifications.data.position = new Vector2(positionX, positionY);
            }
            
            if ((data & SnapshotSegment.MODIFIED_ROTATION) != 0)
            {
                float rotation = inputStream.readFloat();
                
                modifications.rotationModified = true;
                modifications.data.angle = rotation;
            }
            
            if ((data & SnapshotSegment.MODIFIED_SHAPE) != 0)
            {
                int typeOrdinal = inputStream.readInt();
                PhysicsComponentShapeType type = PhysicsComponentShapeType.values()[typeOrdinal];

                modifications.shapeModified = true;
                modifications.data.type = type;
                
                switch (type)
                {
                case CIRCLE:
                    float radius = inputStream.readFloat();
                    modifications.data.radius = radius;
                    break;
                case POLYGON:
                    int polygonN = inputStream.readInt();
                    modifications.data.vertices = new Vector2[polygonN];
                    for (int j = 0; j < polygonN; j++)
                    {
                        float vertexX = inputStream.readFloat();
                        float vertexY = inputStream.readFloat();
                        modifications.data.vertices[j] = new Vector2(vertexX, vertexY);
                    }
                    break;
                }
            }
        }

        for(int i = 0; i < createdLightsAmount; i++)
            segment.createdLights[i] = inputStream.readInt();
        for(int i = 0; i < removedLightsAmount; i++)
            segment.removedLights[i] = inputStream.readInt();
        for(int i = 0; i < modifiedLightsAmount; i++)
        {
            long data = inputStream.readLong();
            segment.modifiedLights[i] = data;
            
            LightModifications modifications = new LightModifications();
            segment.productLights[i] = modifications;
            
            if ((data & SnapshotSegment.MODIFIED_POSITION) != 0)
            {
                float positionX = inputStream.readFloat();
                float positionY = inputStream.readFloat();
                
                modifications.positionModified = true;
                modifications.data.position = new Vector2(positionX, positionY);
            }
            
            if ((data & SnapshotSegment.MODIFIED_SHAPE) != 0)
            {
                float radius = inputStream.readFloat();
                float colorR = inputStream.readFloat();
                float colorG = inputStream.readFloat();
                float colorB = inputStream.readFloat();
                float colorA = inputStream.readFloat();

                modifications.shapeModified = true;
                modifications.data.radius = radius;
                modifications.data.color = new Color(colorR, colorG, colorB, colorA);
            }
        }
        
        client.world.applySnapshot(segment, client);
    }
}