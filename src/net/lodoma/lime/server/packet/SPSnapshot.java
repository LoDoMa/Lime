package net.lodoma.lime.server.packet;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.Set;

import net.lodoma.lime.server.Server;
import net.lodoma.lime.server.ServerPacket;
import net.lodoma.lime.server.ServerUser;
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
        
        user.outputStream.writeInt(snapshot.removed.size());
        for (Integer id : snapshot.removed)
            user.outputStream.writeInt(id);
        
        user.outputStream.writeInt(snapshot.entityData.size());
        Set<Entry<Integer, EntityShape>> entrySet = snapshot.entityData.entrySet();
        for (Entry<Integer, EntityShape> entry : entrySet)
        {
            user.outputStream.writeInt(entry.getKey());
            
            EntityShape shape = entry.getValue();
            user.outputStream.writeInt(shape.positionList.length);
            for (int i = 0; i < shape.positionList.length; i++)
            {
                user.outputStream.writeFloat(shape.positionList[i].x);
                user.outputStream.writeFloat(shape.positionList[i].y);
                user.outputStream.writeFloat(shape.angleList[i]);
                user.outputStream.writeFloat(shape.radiusList[i]);
            }
        };
    }
}