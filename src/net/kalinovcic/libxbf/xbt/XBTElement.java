package net.kalinovcic.libxbf.xbt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

abstract class XBTElement
{
    XBTElement next;
    String name;

    public XBTElement name(String name)
    {
        this.name = name;
        return this;
    }
    
    abstract void read(DataInputStream dis, int version) throws IOException;
    abstract void write(DataOutputStream dos, int version) throws IOException;
}
