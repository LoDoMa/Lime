package net.kalinovcic.libxbf.xbt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class XBTStringUTF8 extends XBTElement
{
    public String value;
    
    public XBTStringUTF8() {}
    
    public XBTStringUTF8(String value)
    {
        this.value = value;
    }
    
    @Override
    void read(DataInputStream dis) throws IOException
    {
        value = dis.readUTF();
    }
    
    @Override
    void write(DataOutputStream dos) throws IOException
    {
        dos.writeUTF(value);
    }
}
