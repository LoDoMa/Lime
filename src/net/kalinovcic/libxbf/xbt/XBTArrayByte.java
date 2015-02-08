package net.kalinovcic.libxbf.xbt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class XBTArrayByte extends XBTElement
{
    public byte[] value;
    
    public XBTArrayByte()
    {
        
    }
    
    public XBTArrayByte(byte[] value)
    {
        this.value = value;
    }
    
    @Override
    void read(DataInputStream dis) throws IOException
    {
        int length = dis.readInt();
        value = new byte[length];
        dis.read(value);
    }
    
    @Override
    void write(DataOutputStream dos) throws IOException
    {
        dos.writeInt(value.length);
        dos.write(value);
    }
}
