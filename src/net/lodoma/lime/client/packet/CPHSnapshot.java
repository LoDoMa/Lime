package net.lodoma.lime.client.packet;

import java.io.IOException;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientPacketHandler;
import net.lodoma.lime.util.HashHelper;
import net.lodoma.lime.util.Vector2;
import net.lodoma.lime.world.Snapshot;
import net.lodoma.lime.world.entity.EntityShape;

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
        
        int removedCount = inputStream.readInt();
        while ((removedCount--) != 0)
            snapshot.removed.add(inputStream.readInt());
        
        int shapeCount = inputStream.readInt();
        while ((shapeCount--) != 0)
        {
            int identifier = inputStream.readInt();
            int componentCount = inputStream.readInt();
            
            EntityShape shape = new EntityShape();
            shape.positionList = new Vector2[componentCount];
            shape.angleList = new float[componentCount];
            shape.radiusList = new float[componentCount];
            
            while ((componentCount--) != 0)
            {
                shape.positionList[componentCount] = new Vector2(inputStream.readFloat(), inputStream.readFloat());
                shape.angleList[componentCount] = inputStream.readFloat();
                shape.radiusList[componentCount] = inputStream.readFloat();
            }
            
            snapshot.entityData.put(identifier, shape);
        }
        
        client.world.applySnapshot(snapshot, client);
    }
}