package net.lodoma.lime.server.packet;

import java.io.IOException;

import net.lodoma.lime.server.Server;
import net.lodoma.lime.server.ServerPacket;
import net.lodoma.lime.server.ServerUser;
import net.lodoma.lime.shader.light.LightData;
import net.lodoma.lime.util.HashHelper;
import net.lodoma.lime.world.SnapshotSegment;
import net.lodoma.lime.world.physics.PhysicsComponentSnapshot;

public class SPSnapshot extends ServerPacket
{
    public static final String NAME = "Lime::Snapshot";
    public static final int HASH = HashHelper.hash32(NAME);
    
    public SPSnapshot(Server server)
    {
        super(server, HASH, SnapshotSegment.class);
    }
    
    @Override
    protected void localWrite(ServerUser user, Object... args) throws IOException
    {
        SnapshotSegment segment = (SnapshotSegment) args[0];
        
        user.outputStream.writeFloat(user.camera.translation.x);
        user.outputStream.writeFloat(user.camera.translation.y);
        user.outputStream.writeFloat(user.camera.rotation);
        user.outputStream.writeFloat(user.camera.scale.x);
        user.outputStream.writeFloat(user.camera.scale.y);
        
        user.outputStream.writeInt(segment.createdComponents.length);
        user.outputStream.writeInt(segment.removedComponents.length);
        user.outputStream.writeInt(segment.modifiedComponents.length);
        
        user.outputStream.writeInt(segment.createdLights.length);
        user.outputStream.writeInt(segment.removedLights.length);
        user.outputStream.writeInt(segment.modifiedLights.length);

        for (int key : segment.createdComponents) user.outputStream.writeInt(key);
        for (int key : segment.removedComponents) user.outputStream.writeInt(key);
        for (long data : segment.modifiedComponents)
        {
            user.outputStream.writeLong(data);
            
            PhysicsComponentSnapshot compo = segment.full.componentData.get((int) (data & 0xFFFFFFFF));
            
            if ((data & SnapshotSegment.MODIFIED_POSITION) != 0)
            {
                user.outputStream.writeFloat(compo.position.x);
                user.outputStream.writeFloat(compo.position.y);
            }
            
            if ((data & SnapshotSegment.MODIFIED_ROTATION) != 0)
                user.outputStream.writeFloat(compo.angle);
            
            if ((data & SnapshotSegment.MODIFIED_SHAPE) != 0)
            {
                user.outputStream.writeInt(compo.type.ordinal());
                switch (compo.type)
                {
                case CIRCLE:
                    user.outputStream.writeFloat(compo.radius);
                    break;
                case POLYGON:
                    user.outputStream.writeInt(compo.vertices.length);
                    for (int i = 0; i < compo.vertices.length; i++)
                    {
                        user.outputStream.writeFloat(compo.vertices[i].x);
                        user.outputStream.writeFloat(compo.vertices[i].y);
                    }
                    break;
                }
            }
        }

        for (int key : segment.createdLights) user.outputStream.writeInt(key);
        for (int key : segment.removedLights) user.outputStream.writeInt(key);
        for (long data : segment.modifiedLights)
        {
            user.outputStream.writeLong(data);
            
            LightData light = segment.full.lightData.get((int) (data & 0xFFFFFFFF));
            
            if ((data & SnapshotSegment.MODIFIED_POSITION) != 0)
            {
                user.outputStream.writeFloat(light.position.x);
                user.outputStream.writeFloat(light.position.y);
            }
            
            if ((data & SnapshotSegment.MODIFIED_SHAPE) != 0)
            {
                user.outputStream.writeFloat(light.radius);
                
                user.outputStream.writeFloat(light.color.r);
                user.outputStream.writeFloat(light.color.g);
                user.outputStream.writeFloat(light.color.b);
                user.outputStream.writeFloat(light.color.a);
            }
        }
    }
}