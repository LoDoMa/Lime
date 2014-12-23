package net.lodoma.lime.world;

import java.nio.ByteBuffer;
import java.util.function.Consumer;

import net.lodoma.lime.server.ServerPacket;
import net.lodoma.lime.server.ServerUser;
import net.lodoma.lime.server.logic.UserManager;

// Server-side

public class SnapshotManager
{
    public World world;
    public UserManager userManager;
    
    public ServerPacket snapshotPacket;
    
    public SnapshotManager(World world)
    {
        this.world = world;
    }
    
    public void send()
    {
        long time_t1 = System.nanoTime();
        
        userManager.foreach(new Consumer<ServerUser>()
        {
            @Override
            public void accept(ServerUser user)
            {
                if (user.fullSnapshot)
                {
                    // Sending full snapshot
                    if (fullSnapshot == null)
                        fullSnapshot = world.buildFullSnapshot();
                    snapshotPacket.write(user, fullSnapshot);
                    
                    user.fullSnapshot = false;
                }
                else
                {
                    // Sending delta snapshot
                    if (deltaSnapshot == null)
                        deltaSnapshot = world.buildDeltaSnapshot();
                    snapshotPacket.write(user, deltaSnapshot);
                }
            }
        });
        
        // Remove snapshot references so that GC can destroy them
        fullSnapshot = null;
        deltaSnapshot = null;
        
        world.snapshotUpdate();
        
        long time_t2 = System.nanoTime();
        long time_td = time_t2 - time_t1;
        double time_ts = time_td / 1000000000.0;
        // System.out.println("snapshot sending time: " + time_ts);
    }
    
    private ByteBuffer fullSnapshot;
    private ByteBuffer deltaSnapshot;
}
