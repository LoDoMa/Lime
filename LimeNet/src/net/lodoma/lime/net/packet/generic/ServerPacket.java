package net.lodoma.lime.net.packet.generic;

import java.nio.ByteBuffer;

import net.lodoma.lime.net.server.generic.GenericServer;
import net.lodoma.lime.net.server.generic.ServerUser;

public abstract class ServerPacket
{
    private int id;
    private Class<?>[] types;
    
    public ServerPacket()
    {
        this.types = null;
    }
    
    public ServerPacket(Class<?>... types)
    {
        this.types = types;
    }
    
    public final void setID(int id)
    {
        this.id = id;
    }
    
    protected abstract byte[] build(Object... args);
    
    public final void send(GenericServer server, ServerUser user, Object... args)
    {
        if(types != null)
        {
            if(args.length != types.length)
                throw new IllegalArgumentException();
            for(int i = 0; i < args.length; i++)
                if(!types[i].isInstance(args[i]))
                    throw new IllegalArgumentException();
        }
        
        byte[] data = build(args);
        ByteBuffer byteBuffer = ByteBuffer.allocate(data.length + 4);
        byteBuffer.putInt(id);
        byteBuffer.put(data);
        byte[] toSend = byteBuffer.array();
        server.sendData(toSend, user);
    }
}
