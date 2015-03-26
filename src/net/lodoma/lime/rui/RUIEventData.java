package net.lodoma.lime.rui;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public abstract class RUIEventData
{
    public abstract void read(DataInputStream dis);
    public abstract void write(DataOutputStream dos);
}
