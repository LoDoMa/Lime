package net.lodoma.lime.client.logic;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.snapshot.Snapshot;
import net.lodoma.lime.snapshot.SnapshotData;

public abstract class ClientLogic
{
    protected Client client;
    public final int hash;
    
    public ClientLogic(Client client, int hash)
    {
        this.client = client;
        this.hash = hash;
    }

    public abstract void init();
    public abstract void destroy();

    public abstract void update();
    public abstract void render();
    
    public abstract SnapshotData createSnapshotData();
    public abstract void handleSnapshot(Snapshot snapshot);
}
