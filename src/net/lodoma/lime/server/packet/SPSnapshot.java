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
import net.lodoma.lime.world.physics.PhysicsComponentSnapshot;

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

        user.outputStream.writeFloat(user.camera.translation.x);
        user.outputStream.writeFloat(user.camera.translation.y);
        user.outputStream.writeFloat(user.camera.rotation);
        user.outputStream.writeFloat(user.camera.scale.x);
        user.outputStream.writeFloat(user.camera.scale.y);
        
        user.outputStream.writeInt(snapshot.removedComponents.size());
        for (Integer id : snapshot.removedComponents)
            user.outputStream.writeInt(id);
        
        user.outputStream.writeInt(snapshot.removedLights.size());
        for (Integer id : snapshot.removedLights)
            user.outputStream.writeInt(id);

        user.outputStream.writeInt(snapshot.componentData.size());
        Set<Entry<Integer, PhysicsComponentSnapshot>> componentEntrySet = snapshot.componentData.entrySet();
        for (Entry<Integer, PhysicsComponentSnapshot> entry : componentEntrySet)
        {
            user.outputStream.writeInt(entry.getKey());
            entry.getValue().write(user.outputStream);
        }
        
        user.outputStream.writeInt(snapshot.lightData.size());
        Set<Entry<Integer, LightData>> lightEntrySet = snapshot.lightData.entrySet();
        for (Entry<Integer, LightData> entry : lightEntrySet)
        {
            user.outputStream.writeInt(entry.getKey());
            entry.getValue().write(user.outputStream);
        };
    }
}