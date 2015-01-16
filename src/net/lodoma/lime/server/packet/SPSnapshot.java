package net.lodoma.lime.server.packet;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.Set;

import net.lodoma.lime.server.Server;
import net.lodoma.lime.server.ServerPacket;
import net.lodoma.lime.server.ServerUser;
import net.lodoma.lime.shader.light.LightData;
import net.lodoma.lime.util.HashHelper;
import net.lodoma.lime.world.Snapshot;
import net.lodoma.lime.world.entity.EntityShape;

public class SPSnapshot extends ServerPacket
{
    public static final String NAME = "Lime::Snapshot";
    public static final int HASH = HashHelper.hash32(NAME);
    
    public SPSnapshot(Server server)
    {
        super(server, HASH, Snapshot.class);
    }
    
    @Override
    protected void localWrite(ServerUser user, Object... args) throws IOException
    {
        Snapshot snapshot = (Snapshot) args[0];
        
        user.outputStream.writeBoolean(snapshot.isDelta);

        user.outputStream.writeInt(snapshot.removedEntities.size());
        for (Integer id : snapshot.removedEntities)
            user.outputStream.writeInt(id);
        
        user.outputStream.writeInt(snapshot.removedLights.size());
        for (Integer id : snapshot.removedLights)
            user.outputStream.writeInt(id);
        
        user.outputStream.writeInt(snapshot.entityData.size());
        Set<Entry<Integer, EntityShape>> entityEntrySet = snapshot.entityData.entrySet();
        for (Entry<Integer, EntityShape> entry : entityEntrySet)
        {
            user.outputStream.writeInt(entry.getKey());
            
            EntityShape shape = entry.getValue();
            user.outputStream.writeInt(shape.snapshots.length);
            for (int i = 0; i < shape.snapshots.length; i++)
            {
                user.outputStream.writeFloat(shape.snapshots[i].position.x);
                user.outputStream.writeFloat(shape.snapshots[i].position.y);
                user.outputStream.writeFloat(shape.snapshots[i].angle);
                user.outputStream.writeInt(shape.snapshots[i].type.ordinal());
                switch (shape.snapshots[i].type)
                {
                case CIRCLE:
                    user.outputStream.writeFloat(shape.snapshots[i].radius);
                    break;
                case POLYGON:
                    user.outputStream.writeInt(shape.snapshots[i].vertices.length);
                    for (int j = 0; j < shape.snapshots[i].vertices.length; j++)
                    {
                        user.outputStream.writeFloat(shape.snapshots[i].vertices[j].x);
                        user.outputStream.writeFloat(shape.snapshots[i].vertices[j].y);
                    }
                    break;
                }
            }
        };
        
        user.outputStream.writeInt(snapshot.lightData.size());
        Set<Entry<Integer, LightData>> lightEntrySet = snapshot.lightData.entrySet();
        for (Entry<Integer, LightData> entry : lightEntrySet)
        {
            user.outputStream.writeInt(entry.getKey());
            
            LightData data = entry.getValue();
            user.outputStream.writeFloat(data.position.x);
            user.outputStream.writeFloat(data.position.y);
            user.outputStream.writeFloat(data.radius);
            user.outputStream.writeFloat(data.color.getR());
            user.outputStream.writeFloat(data.color.getG());
            user.outputStream.writeFloat(data.color.getB());
            user.outputStream.writeFloat(data.color.getA());
            user.outputStream.writeFloat(data.angleRangeBegin);
            user.outputStream.writeFloat(data.angleRangeEnd);
        };
    }
}