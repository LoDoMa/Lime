package net.lodoma.lime.server.logic;

import java.io.DataOutputStream;

import net.lodoma.lime.server.Server;
import net.lodoma.lime.server.ServerUser;

public abstract class ServerLogic
{
    protected Server server;
    
    public ServerLogic(Server server)
    {
        this.server = server;
    }

    public abstract void init();
    public abstract void destroy();

    public abstract void update();

    public abstract boolean acceptUser(ServerUser user);
    public abstract boolean respondBroadcast();
    public abstract void sendSnapshot(DataOutputStream outputStream);
}
