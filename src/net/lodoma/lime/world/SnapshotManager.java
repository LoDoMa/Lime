package net.lodoma.lime.world;

import net.lodoma.lime.server.ServerPacket;
import net.lodoma.lime.server.ServerUser;
import net.lodoma.lime.server.logic.UserManager;

// Server-side

public class SnapshotManager
{
    public World world;
    public UserManager userManager;
    
    public ServerPacket snapshotPacket;
    
    public Snapshot lastSnapshot;
    
    public SnapshotManager(World world, UserManager userManager)
    {
        this.world = world;
        this.userManager = userManager;
        
        lastSnapshot = new Snapshot(world);
    }
    
    public void send()
    {
        Snapshot snapshot = new Snapshot(world);
        lastSnapshot = snapshot;
        
        userManager.foreach((ServerUser user) -> {
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
