package net.lodoma.lime.client.io.chat;

import java.io.IOException;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientPacket;
import net.lodoma.lime.util.HashHelper;

/**
 * CPChatMessageSend is a ClientPacket sent when
 * a chat message is sent in the current chat session.
 * The packet takes the message as a parameter.
 * 
 * SPHChatMessageSent is the SPH for this packet.
 * 
 * @author Lovro Kalinovčić
 */
public class CPChatMessageSend extends ClientPacket
{
    /**
     * Unique name of this packet
     */
    public static final String NAME = "Lime::ChatMessageSend";
    
    /**
     * 32-bit hash of this packets name;
     */
    public static final int HASH = HashHelper.hash32(NAME);
    
    /**
     * 
     * @param client - the client that uses this packet
     */
    public CPChatMessageSend(Client client)
    {
        super(client, HASH, String.class);
    }

    @Override
    protected void localWrite(Object... args) throws IOException
    {
        outputStream.writeChars((String) args[0]);
        outputStream.writeChar(0);
    }
}
