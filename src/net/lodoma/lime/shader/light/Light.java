package net.lodoma.lime.shader.light;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.lodoma.lime.util.Identifiable;

public interface Light extends Identifiable<Integer>
{
    public int getTypeHash();
    public void useProgram();
    public void render();
    
    public void write(DataOutputStream outputStream) throws IOException;
    public void read(DataInputStream inputStream) throws IOException;
}
