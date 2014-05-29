package net.lodoma.lime.client.generic.net.packet;

import java.nio.ByteBuffer;

import net.lodoma.lime.client.generic.net.GenericClient;

public abstract class ClientPacket
{
    private long id;
    private Class<?>[] types;
    
    public ClientPacket()
    {
        this.types = null;
    }
    
    public ClientPacket(Class<?>... types)
    {
        this.types = types;
    }
    
    public final void setID(long l)
    {
        this.id = l;
    }
    
    protected abstract byte[] build(Object... args);
    
    public final void send(GenericClient client, Object... args)
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
        ByteBuffer byteBuffer = ByteBuffer.allocate(data.length + 8);
        byteBuffer.putLong(id);
        byteBuffer.put(data);
        byte[] toSend = byteBuffer.array();
        client.sendData(toSend);
    }
}
