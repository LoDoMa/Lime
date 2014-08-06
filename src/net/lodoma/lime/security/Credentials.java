package net.lodoma.lime.security;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.lodoma.lime.util.StringHelper;

public final class Credentials
{
    private final String username;
    
    public Credentials(String username)
    {
        this.username = username;
    }
    
    public Credentials(InputStream is) throws IOException
    {
        username = StringHelper.read(is);
    }
    
    public void write(OutputStream os) throws IOException
    {
        StringHelper.write(username, os);
    }
    
    public String getUsername()
    {
        return username;
    }
}
