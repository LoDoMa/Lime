package net.lodoma.lime.server.io.world;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import net.lodoma.lime.server.Server;
import net.lodoma.lime.server.ServerOutput;
import net.lodoma.lime.server.ServerUser;
import net.lodoma.lime.world.server.ServersideWorld;

public class SOInitialWorldData extends ServerOutput
{
    public SOInitialWorldData(Server server, String soName)
    {
        super(server, soName);
    }
    
    @Override
    protected void localHandle(ServerUser user, Object... args) throws IOException
    {
        ServersideWorld world = (ServersideWorld) server.getProperty("world");
        
        int width = world.getWidth();
        int height = world.getHeight();
        
        user.outputStream.writeInt(width);
        user.outputStream.writeInt(height);
        
        for(int y = 0; y < height; y++)
            for(int x = 0; x < width; x++)
            {
                user.outputStream.writeByte(world.getTileInfo(x, y));
                user.outputStream.writeByte(world.getTileShape(x, y));
                user.outputStream.writeShort(world.getTileMaterial(x, y));
            }
        
        Set<Short> paletteKeySet = world.getPaletteKeySet();
        user.outputStream.writeInt(paletteKeySet.size());
        
        for(short key : paletteKeySet)
        {
            UUID uuid = world.getPaletteMember(key).uuid;
            user.outputStream.writeShort(key);
            user.outputStream.writeLong(uuid.getLeastSignificantBits());
            user.outputStream.writeLong(uuid.getMostSignificantBits());
        }
        
        Set<Integer> entityIDSet = world.getEntityIDSet();
        user.outputStream.writeInt(entityIDSet.size());
        for(int entityID : entityIDSet)
        {
            user.outputStream.writeInt(entityID);
            user.outputStream.writeLong(world.getEntity(entityID).getHash());
        }
    }
}
