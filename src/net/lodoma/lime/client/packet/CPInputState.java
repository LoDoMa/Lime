package net.lodoma.lime.client.packet;

import java.io.IOException;
import java.nio.ByteBuffer;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientPacket;
import net.lodoma.lime.util.HashHelper;

public class CPInputState extends ClientPacket
{
    public static final String NAME = "Lime::InputState";
    public static final int HASH = HashHelper.hash32(NAME);
    
    public CPInputState(Client client)
    {
        super(client, HASH, ByteBuffer.class);
    }

    @Override
    protected void localWrite(Object... args) throws IOException
    {
        ByteBuffer state = (ByteBuffer) args[0];
        outputStream.write(state.array());
    }
}
