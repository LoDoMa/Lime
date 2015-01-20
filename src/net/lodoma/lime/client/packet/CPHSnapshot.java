package net.lodoma.lime.client.packet;

import java.io.IOException;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientPacketHandler;
import net.lodoma.lime.shader.light.LightData;
import net.lodoma.lime.util.HashHelper;
import net.lodoma.lime.world.Snapshot;
import net.lodoma.lime.world.physics.PhysicsComponentSnapshot;

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
        snapshot.isDelta = inputStream.readBoolean();

        int removedComponentsCount = inputStream.readInt();
        while ((removedComponentsCount--) != 0)
            snapshot.removedComponents.add(inputStream.readInt());
        
        int removedLightCount = inputStream.readInt();
        while ((removedLightCount--) != 0)
            snapshot.removedLights.add(inputStream.readInt());
        
        int componentDataCount = inputStream.readInt();
        for (int i = 0; i < componentDataCount; i++)
        {
            int identifier = inputStream.readInt();
            PhysicsComponentSnapshot compoSnapshot = new PhysicsComponentSnapshot();
            compoSnapshot.read(inputStream);
            snapshot.componentData.put(identifier, compoSnapshot);
        }
        
        int lightDataCount = inputStream.readInt();
        while ((lightDataCount--) != 0)
        {
            int identifier = inputStream.readInt();
            LightData lightData = new LightData();
            lightData.read(inputStream);
            snapshot.lightData.put(identifier, lightData);
        }
        
        client.world.applySnapshot(snapshot, client);
    }
}