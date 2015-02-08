package net.kalinovcic.libxbf.xbt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class XBTArrayShort extends XBTElement
{
    public short[] value;
    
    public XBTArrayShort()
    {
        
    }
    
    public XBTArrayShort(short[] value)
    {
        this.value = value;
    }
    
    @Override
    void read(DataInputStream dis) throws IOException
    {
        int length = dis.readInt();
        value = new short[length];
        for (int i = 0; i < length; i++)
            value[i] = dis.readShort();
    }
    
    @Override
    void write(DataOutputStream dos) throws IOException
    {
        dos.writeInt(value.length);
        for (int i = 0; i < value.length; i++)
            dos.writeShort(value[i]);
    }
}
