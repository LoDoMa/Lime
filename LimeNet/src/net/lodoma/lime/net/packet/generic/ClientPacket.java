package net.lodoma.lime.net.packet.generic;

import java.nio.ByteBuffer;

import net.lodoma.lime.net.client.generic.GenericClient;

public abstract class ClientPacket
{
    private int id;
    private Class<?>[] types;
    
    public ClientPacket()
    {
        this.types = null;
    }
    
    public ClientPacket(Class<?>... types)
    {
        this.types = types;
    }
    
    final void setID(int id)
    {
        this.id = id;
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
        ByteBuffer byteBuffer = ByteBuffer.allocate(data.length + 4);
        byteBuffer.putInt(id);
        byteBuffer.put(data);
        byte[] toSend = byteBuffer.array();
        client.sendData(toSend);
    }
}
