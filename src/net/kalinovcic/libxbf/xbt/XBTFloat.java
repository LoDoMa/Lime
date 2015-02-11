package net.kalinovcic.libxbf.xbt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class XBTFloat extends XBTElement
{
    public float value;

    public XBTFloat() {}
    
    public XBTFloat(float value)
    {
        this.value = value;
    }
    
    @Override
    void read(DataInputStream dis, int version) throws IOException
    {
        value = dis.readFloat();
    }
    
    @Override
    void write(DataOutputStream dos, int version) throws IOException
    {
        dos.writeFloat(value);
    }
}
