package net.lodoma.lime.server.generic;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import net.lodoma.lime.packet.generic.NetStage;

public final class UserPool
{
    private Map<InetAddress, Map<Integer, ServerUser>> knownUsers = new HashMap<InetAddress, Map<Integer, ServerUser>>();
    
    public ServerUser getUser(InetAddress address, int port)
    {
        if(knownUsers.containsKey(address))
        {
            if(knownUsers.get(address).containsKey(port))
                return knownUsers.get(address).get(port);
            else
            {
                ServerUser user = new ServerUser(NetStage.PRIMITIVE, address, port);
                knownUsers.get(address).put(port, user);
                return user;
            }
        }
        else
        {
            ServerUser user = new ServerUser(NetStage.PRIMITIVE, address, port);
            knownUsers.put(address, new HashMap<Integer, ServerUser>());
            knownUsers.get(address).put(port, user);
            return user;
        }
    }
    
    public void removeUser(ServerUser user)
    {
        knownUsers.get(user.address).remove(user.port);
        if(knownUsers.get(user.address).isEmpty())
            knownUsers.remove(user.address);
    }
}
