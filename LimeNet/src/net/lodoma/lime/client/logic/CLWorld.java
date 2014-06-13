package net.lodoma.lime.client.logic;

import net.lodoma.lime.client.generic.net.GenericClient;
import net.lodoma.lime.client.generic.net.packet.ClientPacketPool;
import net.lodoma.lime.world.ClientsideWorld;
import net.lodoma.lime.world.client.packet.CPHWorldChunk;
import net.lodoma.lime.world.client.packet.CPHWorldDimensions;
import net.lodoma.lime.world.client.packet.CPHWorldPalette;
import net.lodoma.lime.world.client.packet.CPWorldChunksRequest;
import net.lodoma.lime.world.client.packet.CPWorldDimensionRequest;
import net.lodoma.lime.world.client.packet.CPWorldPaletteRequest;

public class CLWorld implements ClientLogic
{
    private GenericClient client;
    private ClientPacketPool packetPool;
    private ClientsideWorld world;
    
    @Override
    public void baseInit(GenericClient client)
    {
        this.client = client;
    }
    
    @Override
    public void propertyInit()
    {
        client.setProperty("world", new ClientsideWorld(client));
    }
    
    @Override
    public void fetchInit()
    {
        packetPool = (ClientPacketPool) client.getProperty("packetPool");
        world = (ClientsideWorld) client.getProperty("world");
        world.fetch();
    }
    
    @Override
    public void generalInit()
    {
        packetPool.addPacket("Lime::WorldDimensionRequest", new CPWorldDimensionRequest());
        packetPool.addHandler("Lime::WorldDimensions", new CPHWorldDimensions());
        
        packetPool.addPacket("Lime::WorldPaletteRequest", new CPWorldPaletteRequest());
        packetPool.addHandler("Lime::WorldPalette", new CPHWorldPalette());

        packetPool.addPacket("Lime::WorldChunksRequest", new CPWorldChunksRequest());
        packetPool.addHandler("Lime::WorldChunk", new CPHWorldChunk());
    }
    
    @Override
    public void clean()
    {
        
    }
    
    @Override
    public void logic()
    {
        world.update(0);
    }
}
