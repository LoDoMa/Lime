package net.lodoma.lime.net.server.generic;

import java.net.InetAddress;

public final class ServerUser
{
    public NetStage stage;

    public InetAddress address;
    public int port;
    
    public int dependencies;
    
    public ServerUser(NetStage stage, InetAddress address, int port)
    {
        this.stage = stage;
        
        this.address = address;
        this.port = port;
        
        this.dependencies = 0;
    }
}
