package net.kalinovcic.libxbf.xbt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class XBTInt extends XBTElement
{
    public int value;

    public XBTInt() {}
    
    public XBTInt(int value)
    {
        this.value = value;
    }
    
    @Override
    void read(DataInputStream dis) throws IOException
    {
        value = dis.readInt();
    }
    
    @Override
    void write(DataOutputStream dos) throws IOException
    {
        dos.writeInt(value);
    }
}
