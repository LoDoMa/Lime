package net.kalinovcic.libxbf.xbt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class XBTByte extends XBTElement
{
    public byte value;

    public XBTByte() {}
    
    public XBTByte(byte value)
    {
        this.value = value;
    }
    
    @Override
    void read(DataInputStream dis, int version) throws IOException
    {
        value = dis.readByte();
    }
    
    @Override
    void write(DataOutputStream dos, int version) throws IOException
    {
        dos.writeByte(value);
    }
}
