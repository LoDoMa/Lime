package net.kalinovcic.libxbf.xbt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class XBTShort extends XBTElement
{
    public short value;

    public XBTShort() {}
    
    public XBTShort(short value)
    {
        this.value = value;
    }
    
    @Override
    void read(DataInputStream dis, int version) throws IOException
    {
        value = dis.readShort();
    }
    
    @Override
    void write(DataOutputStream dos, int version) throws IOException
    {
        dos.writeShort(value);
    }
}
