package net.kalinovcic.libxbf.xbt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class XBTDouble extends XBTElement
{
    public double value;

    public XBTDouble() {}
    
    public XBTDouble(double value)
    {
        this.value = value;
    }
    
    @Override
    void read(DataInputStream dis) throws IOException
    {
        value = dis.readDouble();
    }
    
    @Override
    void write(DataOutputStream dos) throws IOException
    {
        dos.writeDouble(value);
    }
}
