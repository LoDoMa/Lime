package net.kalinovcic.libxbf.xbt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.kalinovcic.libxbf.commons.StringITA2;

public class XBTStringITA2 extends XBTElement
{
    public StringITA2 value;
    
    public XBTStringITA2() {}
    
    public XBTStringITA2(String value)
    {
        this(new StringITA2(value));
    }
    
    public XBTStringITA2(StringITA2 value)
    {
        this.value = value;
    }
    
    @Override
    void read(DataInputStream dis, int version) throws IOException
    {
        value = new StringITA2(dis);
    }
    
    @Override
    void write(DataOutputStream dos, int version) throws IOException
    {
        value.write(dos);
    }
}
