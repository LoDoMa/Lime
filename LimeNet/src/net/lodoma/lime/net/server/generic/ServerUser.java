package net.lodoma.lime.net.server.generic;

import java.net.InetAddress;
import java.util.HashSet;
import java.util.Set;

import net.lodoma.lime.net.packet.generic.NetStage;

public final class ServerUser
{
    public NetStage stage;

    public InetAddress address;
    public int port;
    
    public Set<Integer> dependencies;
    
    public ServerUser(NetStage stage, InetAddress address, int port)
    {
        this.stage = stage;
        
        this.address = address;
        this.port = port;
        
        this.dependencies = new HashSet<Integer>();
    }
}
