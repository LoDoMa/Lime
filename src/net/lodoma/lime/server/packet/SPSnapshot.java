package net.lodoma.lime.server.packet;

import java.io.IOException;
import java.nio.ByteBuffer;

import net.lodoma.lime.server.Server;
import net.lodoma.lime.server.ServerPacket;
import net.lodoma.lime.server.ServerUser;
import net.lodoma.lime.util.HashHelper;

public class SPSnapshot extends ServerPacket
{
    public static final String NAME = "Lime::Snapshot";
    public static final int HASH = HashHelper.hash32(NAME);
    
    public SPSnapshot(Server server)
    {
        super(server, HASH, ByteBuffer.class);
    }
    
    @Override
    protected void localWrite(ServerUser user, Object... args) throws IOException
    {
        ByteBuffer buffer = (ByteBuffer) args[0];
        
        user.outputStream.writeInt(buffer.capacity());
        user.outputStream.write(buffer.array());
    }
}