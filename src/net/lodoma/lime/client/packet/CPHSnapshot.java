package net.lodoma.lime.client.packet;

import java.io.IOException;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientPacketHandler;
import net.lodoma.lime.client.logic.ClientLogicFactory;
import net.lodoma.lime.snapshot.Snapshot;
import net.lodoma.lime.util.HashHelper;

public class CPHSnapshot extends ClientPacketHandler
{
    public static final String NAME = "Lime::Snapshot";
    public static final int HASH = HashHelper.hash32(NAME);
    
    public CPHSnapshot(Client client)
    {
        super(client, HASH);
    }
    
    @Override
    protected void localHandle() throws IOException
    {
        Snapshot snapshot = new Snapshot();
        
        snapshot.logicID = inputStream.readInt();
        snapshot.snapshotID = inputStream.readInt();
        
        if (client.logic == null || client.logic.hash != snapshot.logicID)
        {
            if (client.logic != null)
                client.logic.destroy();
            client.logic = ClientLogicFactory.factoryPool.get(snapshot.logicID).newInstance(client);
            client.logic.init();
        }
        
        snapshot.data = client.logic.createSnapshotData();
        snapshot.data.read(client, inputStream);
        
        client.logic.handleSnapshot(snapshot);
    }
}