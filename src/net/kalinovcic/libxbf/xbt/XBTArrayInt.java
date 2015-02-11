package net.kalinovcic.libxbf.xbt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class XBTArrayInt extends XBTElement
{
    public int[] value;
    
    public XBTArrayInt()
    {
        
    }
    
    public XBTArrayInt(int[] value)
    {
        this.value = value;
    }
    
    @Override
    void read(DataInputStream dis, int version) throws IOException
    {
        int length = dis.readInt();
        value = new int[length];
        for (int i = 0; i < length; i++)
            value[i] = dis.readInt();
    }
    
    @Override
    void write(DataOutputStream dos, int version) throws IOException
    {
        dos.writeInt(value.length);
        for (int i = 0; i < value.length; i++)
            dos.writeInt(value[i]);
    }
}
