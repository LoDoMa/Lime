package net.lodoma.lime.server.packet;

import java.io.IOException;
import java.nio.ByteBuffer;

import net.lodoma.lime.input.Input;
import net.lodoma.lime.server.Server;
import net.lodoma.lime.server.ServerPacketHandler;
import net.lodoma.lime.server.ServerUser;
import net.lodoma.lime.util.HashHelper;

public class SPHInputState extends ServerPacketHandler
{
    public static final String NAME = "Lime::InputState";
    public static final int HASH = HashHelper.hash32(NAME);
    
    public SPHInputState(Server server)
    {
        super(server, HASH);
    }
    
    @Override
    protected void localHandle(ServerUser user) throws IOException
    {
        ByteBuffer buffer = ByteBuffer.allocate(Input.STATE_SIZE);
        while (buffer.remaining() > 0)
            buffer.put(user.inputStream.readByte());
        
        Input.loadState(buffer);
        Input.update();
    }
}
