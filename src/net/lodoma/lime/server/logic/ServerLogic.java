package net.lodoma.lime.server.logic;

import net.lodoma.lime.server.Server;
import net.lodoma.lime.server.ServerUser;

public abstract class ServerLogic
{
    protected Server server;
    public final int hash;
    
    public ServerLogic(Server server, int hash)
    {
        this.server = server;
        this.hash = hash;
    }

    public abstract void init();
    public abstract void destroy();

    public abstract void update(double timeDelta);

    public abstract boolean acceptUser(ServerUser user);
    public abstract boolean respondBroadcast();
    public abstract void sendSnapshots();
}
