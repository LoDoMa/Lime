package net.lodoma.lime.world;

import net.lodoma.lime.server.Server;
import net.lodoma.lime.server.ServerPacket;
import net.lodoma.lime.server.ServerUser;

// Server-side

public class SnapshotManager
{
    public Server server;
    
    public ServerPacket snapshotPacket;
    
    public Snapshot lastSnapshot;
    
    public SnapshotManager(Server server)
    {
        this.server = server;
        
        lastSnapshot = new Snapshot(server);
    }
    
    public void send()
    {
        Snapshot snapshot = new Snapshot(server);
        lastSnapshot = snapshot;
        
        server.userManager.foreach((ServerUser user) -> {
            if (user.fullSnapshot)
            {
                snapshotPacket.write(user, snapshot);
                user.fullSnapshot = false;
            }
            else
            {
                // TODO: This should be a delta snapshot
                snapshotPacket.write(user, snapshot);
            }
        });
    }
}
