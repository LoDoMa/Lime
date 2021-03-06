package net.kalinovcic.libxbf.xbt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class XBTLong extends XBTElement
{
    public long value;

    public XBTLong() {}
    
    public XBTLong(long value)
    {
        this.value = value;
    }
    
    @Override
    void read(DataInputStream dis, int version) throws IOException
    {
        value = dis.readLong();
    }
    
    @Override
    void write(DataOutputStream dos, int version) throws IOException
    {
        dos.writeLong(value);
    }
}
