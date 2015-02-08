package net.kalinovcic.libxbf.xbt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class XBTArrayFloat extends XBTElement
{
    public float[] value;
    
    public XBTArrayFloat()
    {
        
    }
    
    public XBTArrayFloat(float[] value)
    {
        this.value = value;
    }
    
    @Override
    void read(DataInputStream dis) throws IOException
    {
        int length = dis.readInt();
        value = new float[length];
        for (int i = 0; i < length; i++)
            value[i] = dis.readFloat();
    }
    
    @Override
    void write(DataOutputStream dos) throws IOException
    {
        dos.writeInt(value.length);
        for (int i = 0; i < value.length; i++)
            dos.writeFloat(value[i]);
    }
}
