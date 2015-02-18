package net.lodoma.lime.snapshot;

import net.lodoma.lime.server.logic.ServerLogic;

public class Snapshot
{
    public int logicID;
    public int snapshotID;
    public SnapshotData data;
    
    public Snapshot()
    {
        
    }
    
    public Snapshot(ServerLogic logic, int snapshotID, SnapshotData data)
    {
        this.logicID = logic.hash;
        this.snapshotID = snapshotID;
        this.data = data;
    }
}
